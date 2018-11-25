package app.domain.section;

import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SeatedSection.class, name = "SeatedSection"),
        @JsonSubTypes.Type(value = StandingSection.class, name = "StandingSection")
})
abstract class AbstractSection implements Section {
    private String name;
    private int elevation;
    private Shape shape;

    AbstractSection(String name, int elevation, Shape shape) {
        this.name = name;
        this.elevation = elevation;
        this.shape = shape;
    }

    AbstractSection(Shape shape) {
        this(null, 0, shape);
    }

    @Override
    public String getName() {
        return name == null ? "" : name;
    }

    @Override
    public int getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    @Override
    public boolean isSelected() {
        return shape.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }

    @Override
    public Shape getShape() {
        return shape;
    }
}
