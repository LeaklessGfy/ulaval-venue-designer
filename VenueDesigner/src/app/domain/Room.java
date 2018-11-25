package app.domain;

import app.domain.section.Section;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Room {
    private ArrayList<Section> sections = new ArrayList<>();
    @JsonProperty
    private final Shape shape;

    private int width;
    private int height;
    private double scale = 1.0;
    private VitalSpace vitalSpace;
    private boolean grid;
    @JsonProperty
    private Stage stage;

    public Room(int width, int height, VitalSpace vitalSpace) {
        this.shape = Rectangle.create(0, 0, width, height, new int[]{20, 38, 52});
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
    }

    @JsonCreator
    public Room( @JsonProperty("shape") Shape shape,
                 @JsonProperty("width") int width,
                 @JsonProperty("height") int height,
                 @JsonProperty("vitalSpace") VitalSpace vitalSpace,
                 @JsonProperty("sections") ArrayList<Section> sections,
                 @JsonProperty("stage") Stage stage) {
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
        this.sections = sections;
        this.stage = stage;
    }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public void setWidth(int width) { this.width = width; }

    public void setHeight(int height) { this.height = height; }

    public VitalSpace getVitalSpace() { return vitalSpace; }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    @JsonIgnore
    public Optional<Stage> getStage() { return Optional.ofNullable(stage); }

    public void addSection(Section section) {
        sections.add(Objects.requireNonNull(section));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public boolean validShape(Shape s, Point offset) {
        int x = shape.getPoints().firstElement().x;
        int y = shape.getPoints().firstElement().y;
        for (Point p : s.getPoints()) {
            if (p.x - offset.x < x || p.x - offset.x > x + width) {
                return false;
            }
            if (p.y - offset.y < y || p.y - offset.y > y + height) {
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
