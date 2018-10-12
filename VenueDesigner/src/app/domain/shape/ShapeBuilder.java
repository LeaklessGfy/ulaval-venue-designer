package app.domain.shape;

public interface ShapeBuilder extends Shape {
    void addPoint(Point point);
    boolean isComplete();
    Shape build();
    void correctLastPoint();
}
