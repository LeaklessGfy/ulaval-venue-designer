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
    private final Point offset = new Point(0, 0);
    private final HashMap<Mode, BiConsumer<Integer, Integer>> clickActions = new HashMap<>();

    private Room room;
    private Mode mode = Mode.None;
    private UIPanel ui;
    private ShapeBuilder current;

    public Controller(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
        this.room = new Room(500, 500, new VitalSpace(1, 1));
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
        int dx = (x - cursor.x);
        int dy = (y - cursor.y);
        cursor.set(x, y);

        if (mode == Mode.None) {
            if (room.getStage().isPresent()) {
                Stage s = room.getStage().get();
                Shape shape = s.getShape();
                if (shape.isSelected()) {
                    shape.move(x, y);
                    ui.repaint();
                    return;
                }
            }
            for (Section section : room.getSections()) {
                Shape shape = section.getShape();
                if (shape.isSelected()) {
                    section.move(x, y);
                    ui.repaint();
                    return;
                }
            }
            offset.offset(dx, dy);
            ui.repaint();
        }
    }
    public void mouseClicked(int x, int y) {
        if (room == null) {
            return;
        }
        if (mode == Mode.None) {
            room.getStage().ifPresent(r -> r.getShape().setSelected(collider.hasCollide(x - offset.x, y - offset.y, r.getShape())));
            for (Section s : room.getSections()) {
                s.getShape().setSelected(collider.hasCollide(x - offset.x, y - offset.y, s.getShape()));
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

    public Point getOffset() {
        return offset;
    }

    private void createShape() {
        current.correctLastPoint();
        Shape shape = current.build();
        current = null;
        for (Point p : shape.getPoints()) {
            p.x -= offset.x;
            p.y -= offset.y;
        }
        if (!room.validShape(shape)) {
            return;
        }
        if (mode == Mode.Stage) {
            room.setStage(new Stage(shape));
        } else {
            room.addSection(SectionFactory.create(mode, shape));
        }
    }
}
