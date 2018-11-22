package app.domain.shape;

import java.util.Vector;

public interface Shape {
    void setSelected(boolean selected);
    boolean isSelected();
    Vector<Point> getPoints();
    void move(int x, int y);
    <T> void accept(T g, Painter<T> painter);
}