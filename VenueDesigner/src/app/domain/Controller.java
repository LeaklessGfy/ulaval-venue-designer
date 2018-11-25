package app.domain;

import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import app.domain.shape.ShapeBuilder;
import app.domain.shape.ShapeBuilderFactory;
import app.domain.section.SectionFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Controller {
    private final Collider collider;
    private final Point cursor = new Point(-1, -1);
    private final HashMap<Mode, BiConsumer<Integer, Integer>> clickActions = new HashMap<>();

    private Room room;
    private Mode mode = Mode.None;
    private UIPanel ui;
    private ShapeBuilder current;

    public Controller(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
    }

    public static Controller create(Collider collider) {
        Controller controller = new Controller(collider);
        controller.clickActions.put(Mode.Stage, (x, y) -> {
            if (controller.current == null) {
                controller.current = new Rectangle.Builder();
            }

            controller.current.addPoint(new Point(x, y));
            if (controller.current.isComplete()) {

                /* TODO: check for room existence ? */
                controller.room.setStage(new Stage(controller.current.build()));
                controller.current = null;
            }
        });

        return controller;
    }

    public Optional<Room> getRoom() {
        return Optional.ofNullable(this.room);
    }

    public void setDrawingPanel(UIPanel ui) {
        this.ui = Objects.requireNonNull(ui);
    }

    public void createRoom(int roomWidth, int roomHeight, int vitalSpaceWidth, int vitalSpaceHeight) {
        VitalSpace vitalSpace = new VitalSpace(vitalSpaceWidth, vitalSpaceHeight);
        this.room = new Room(roomWidth, roomHeight, vitalSpace);
    }

    public void load(Path path) {
        this.room = null;
    }

    public int getXCursor () {
        return  cursor.x;
    }

    public int getYCursor () {
        return  cursor.y;
    }

    public void mouseDragged(int x, int y) {
        if (mode == Mode.None) {
            for (Section s : room.getSections()) {
                Shape currentShape = s.getShape();
                if (currentShape.isSelected())
                {
                    s.move(x,y);
                }
            }
            ui.repaint();
        }
    }
    public void mouseClicked(int x, int y) {
        if (room == null) {
            return;
        }
        if (mode == Mode.None) {
            room.getStage().ifPresent(r -> r.getShape().setSelected(collider.hasCollide(x, y, r.getShape())));
            for (Section s : room.getSections()) {
                Shape currentShape = s.getShape();
                currentShape.setSelected(collider.hasCollide(x, y, currentShape));
            }
            ui.repaint();
            return;
        }
        if (current == null) {
            current = ShapeBuilderFactory.create(mode);
        }

        current.addPoint(new Point(x, y));

        if (current.isComplete()) {
            createShape();
        }

        ui.repaint();
    }

    public void mouseMoved(int x, int y) {
        cursor.set(x, y);
        ui.repaint();
    }

    public boolean toggleMode(Mode mode) {
        if (room == null) {
            return false;
        }
        current = null;
        if (this.mode == mode) {
            this.mode = Mode.None;
            return false;
        }
        this.mode = Objects.requireNonNull(mode);
        return true;
    }

    public Optional<Shape> getCurrent() {
        return Optional.ofNullable(current);
    }

    public Mode getMode() {
        return mode;
    }

    private void createShape() {
        current.correctLastPoint();
        if (mode == Mode.Stage) {
            room.setStage(new Stage(current.build()));
        } else {
            room.addSection(SectionFactory.create(mode, current.build()));
        }
        current = null;
    }
}
