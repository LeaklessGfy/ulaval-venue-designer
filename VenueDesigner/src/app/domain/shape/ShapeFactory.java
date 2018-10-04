package app.domain.shape;

import java.util.Objects;

public final class ShapeFactory {
    public static Shape create(Mode mode) {
        switch (Objects.requireNonNull(mode)) {
            case Rectangle:
                return new Rectangle();
            case Polygon:
                return new Polygon();
        }
        throw new RuntimeException();
    }
}
