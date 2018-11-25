package app.domain.shape;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbstractShape.class, name = "AbstractShape")})
public interface Shape {
    void setSelected(boolean selected);
    boolean isSelected();
    Vector<Point> getPoints();
    int[] getColor();
    void move(int x, int y, Point offset);
    Point computeCentroid();
    <T> void accept(T g, Painter<T> painter);
    Shape clone();
}