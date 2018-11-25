package app.domain;

import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Stage {
    private final Shape shape;

    @JsonCreator
    public Stage(@JsonProperty("shape") Shape shape) {
        this.shape = Objects.requireNonNull(shape);
    }

    public Shape getShape() {
        return shape;
    }
}
