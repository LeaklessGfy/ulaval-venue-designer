package app.domain.shape;

import java.util.Vector;

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