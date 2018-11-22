package app.domain.shape;

import java.util.Vector;

public interface Shape {
    void setSelected(boolean selected);
    boolean isSelected();
    Vector<Point> getPoints();
    void move(Point p);
    <T> void accept(T g, Painter<T> painter);
}