package app.domain;

import app.domain.shape.Shape;

import java.util.List;

public interface Painter<T> {
    void draw(T canvas, List<Shape> shapes);
}
