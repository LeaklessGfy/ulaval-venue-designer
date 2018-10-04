package app.domain.shape;

import java.util.List;

public interface Shape {
    void addPoint(Point point);

    boolean isValid();
    boolean isSelected();
    List<Point> getPoints();

    void setSelected(boolean selected);

    <T> void accept(T canvas, ShapeVisitor<T> visitor);
}