package app.domain.shape;

public interface Painter<T> {
    void draw(T g, Rectangle rectangle);
    void draw(T g, Polygon polygon);
}
