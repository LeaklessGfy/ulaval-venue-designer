package app.domain;

import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import app.domain.shape.ShapeBuilder;
import app.domain.shape.ShapeBuilderFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Controller {
    private final Collider collider;
    private final ArrayList<Shape> shapes = new ArrayList<>();
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

    public void setDrawingPanel(UIPanel ui) {
        this.ui = Objects.requireNonNull(ui);
    }

    public void create(int width, int heigth, VitalSpace vitalSpace) {
        this.room = new Room(width, heigth, vitalSpace);
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

    public void mouseClicked(int x, int y) {
        if (mode == Mode.None) {
            for (Shape s : shapes) {
                s.setSelected(collider.hasCollide(x, y, s));
            }
            ui.repaint();
            return;
        }

        if (current == null) {
            current = ShapeBuilderFactory.create(mode);
        }

        current.addPoint(new Point(x, y));
        if (current.isComplete()) {
            shapes.add(current.build());
            current = null;
        }

        ui.repaint();
    }

    public void mouseMoved(int x, int y) {
        cursor.set(x, y);
        ui.repaint();
    }

    public boolean toggleMode(Mode mode) {
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

    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    public Optional<Stage> getStage() {
        if (room != null) {
            return room.getStage();
        }
        return Optional.empty();
    }

    public List<Section> getSections() {
        if (room != null) {
            return room.getSections();
        }
        return Collections.emptyList();
    }
}
