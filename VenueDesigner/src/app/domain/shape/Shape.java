package app.domain.shape;

import app.domain.Drawable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbstractShape.class, name = "AbstractShape")
})
public interface Shape extends Drawable {
    Vector<Point> getPoints();
    int[] getColor();
    boolean isSelected();
    void setSelected(boolean selected);
    void move(int x, int y);
    void move(int x, int y, Point offset);
    void rotate(double thetaRadian);
    Point computeCentroid();
    Shape clone();
}