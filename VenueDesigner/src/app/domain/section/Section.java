package app.domain.section;

import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;

public interface Section {
    String getName();
    int getElevation();
    Shape getShape();
    void move(int x, int y, Point offset);
    <T> void accept(T g, Painter<T> painter);
}
