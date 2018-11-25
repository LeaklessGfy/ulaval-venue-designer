package app.domain.shape;

public interface Painter<T> {
    void draw(T g, Rectangle rectangle);
    void draw(T g, Polygon polygon);
    void draw(T g, Rectangle.Builder rectangle);
    void draw(T g, Polygon.Builder polygon);
}
