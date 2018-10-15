package app.domain.shape;

import java.util.Vector;

public interface Shape {
    void setSelected(boolean selected);
    boolean isSelected();

    Vector<Point> getPoints();

    <T> void accept(T g, Painter<T> painter);
}