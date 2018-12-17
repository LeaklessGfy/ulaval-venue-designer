package app.domain;

import app.domain.seat.Seat;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
import app.domain.shape.Painter;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Room implements Drawable {
    private final ArrayList<Section> sections;
    @JsonProperty
    private Shape shape;

    private double width;
    private double height;
    private VitalSpace vitalSpace;
    private boolean grid;
    @JsonProperty
    private Stage stage;

    public Room(double width, double height, VitalSpace vitalSpace) {
        this.shape = Rectangle.create(0, 0, width, height, new int[]{20, 38, 52, 255});
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
        this.sections = new ArrayList<>();
    }

    @JsonCreator
    public Room( @JsonProperty("shape") Shape shape,
                 @JsonProperty("width") double width,
                 @JsonProperty("height") double height,
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

    public Room (Room room) {
        this.shape = room.shape;
        this.width = room.width;
        this.height = room.height;
        this.vitalSpace = room.vitalSpace;
        this.sections = new ArrayList<>();
        for (Section section : room.sections) {
            if (section instanceof SeatedSection){
                this.sections.add(new SeatedSection((SeatedSection)section));
            }
            else {
                this.sections.add(new StandingSection((StandingSection)section));
            }
        }
        if (room.getStage().isPresent()) {
            this.stage = new Stage(room.getStage().get());
        }
        this.grid = room.grid;
    }

    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
        this.shape = Rectangle.create(0, 0, width, height, new int[]{20, 38, 52, 255});
    }

    public double getWidth() { return this.width; }

    public double getHeight() { return this.height; }

    public void setWidth(double width) { this.width = width; }

    public void setHeight(double height) { this.height = height; }

    public VitalSpace getVitalSpace() { return vitalSpace; }

    public Shape getShape() {
        return shape;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @JsonIgnore
    public Optional<Stage> getStage() { return Optional.ofNullable(stage); }

    public void addSection(Section section) {
        sections.add(Objects.requireNonNull(section));
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }

    @JsonIgnore
    public boolean isStageSet(){
        return !(stage ==  null);
    }

    public boolean isSameRoom(Room room){
        if(this.shape.isSameShape(room.shape) &&
                this.width == room.width &&
                this.height == room.height &&
                this.vitalSpace == room.vitalSpace &&
                this.stage == room.stage) {
            if (this.stage != null && room.stage != null) {
                if (this.stage.isSameStage(room.stage)) {
                    if (this.sections.size() == room.sections.size()) {
                        for (int i = 0; i < sections.size(); i++) {
                            Section oldSection = this.sections.get(i);
                            Section newSection = room.sections.get(i);
                            if (oldSection.isSameSection(newSection)) {
                                if (oldSection instanceof SeatedSection) {
                                    SeatedSection oldSeatedSection = (SeatedSection) oldSection;
                                    SeatedSection newSeatedSection = (SeatedSection) newSection;
                                    if (oldSeatedSection.getSeats().length == newSeatedSection.getSeats().length) {
                                        for (int row = 0; row < oldSeatedSection.getSeats().length; row++) {
                                            for (int col = 0; col < oldSeatedSection.getSeats()[row].length; col++) {
                                                if (!oldSeatedSection.getSeats()[row][col].isSameSeat(newSeatedSection.getSeats()[row][col])) {
                                                    return false;
                                                }
                                            }
                                        }
                                    } else {
                                        return false;
                                    }
                                }
                            }
                            else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }
}
