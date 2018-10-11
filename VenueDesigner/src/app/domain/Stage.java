package app.domain;

import app.domain.shape.Shape;

import java.util.Objects;

public final class Stage {
    private final Shape shape;

    public Stage(Shape shape) {
        this.shape = Objects.requireNonNull(shape);
    }

    public Shape getShape() {
        return shape;
    }
}
