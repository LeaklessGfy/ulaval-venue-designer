package app.domain.section;

import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbstractSection.class, name = "AbstractSection")})
public interface Section {
    String getName();
    int getElevation();
    Shape getShape();
    void move(int x, int y, Point offset);
    <T> void accept(T g, Painter<T> painter);
}
