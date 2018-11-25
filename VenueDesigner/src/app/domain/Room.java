package app.domain;

import app.domain.section.Section;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Room {
    private final ArrayList<Section> sections = new ArrayList<>();
    private final Shape shape;

    private int width;
    private int height;
    private double scale = 1.0;
    private VitalSpace vitalSpace;
    private boolean grid;
    private Stage stage;

    public Room(int width, int height, VitalSpace vitalSpace) {
        this.shape = Rectangle.create(0, 0, width, height);
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public void setWidth(int width) { this.width = width; }

    public void setHeight(int height) { this.height = height; }

    public VitalSpace getVitalSpace() { return vitalSpace; }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    public Optional<Stage> getStage() {
        return Optional.ofNullable(stage);
    }

    public void addSection(Section section) {
        sections.add(Objects.requireNonNull(section));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public boolean validShape(Shape s) {
        int x = shape.getPoints().firstElement().x;
        int y = shape.getPoints().firstElement().y;
        for (Point p : s.getPoints()) {
            if (p.x < x || p.x > x + width) {
                return false;
            }
            if (p.y < y || p.y > y + height) {
                return false;
            }
        }
        return true;
    }

    public <T> void accept(T g, Painter<T> painter) {
        shape.accept(g, painter);
        if (stage != null) {
            stage.getShape().accept(g, painter);
        }
        for (Section s : sections) {
            s.accept(g, painter);
        }
    }
}
