package app.domain.shape;

import java.util.Vector;

public interface ShapeBuilder {
    void addPoint(Point point);
    boolean isComplete();
    Shape build();
    void correctLastPoint();
    Vector<Point> getPoints();
    <T> void accept(T g, Painter<T> painter);
}
