package app.domain.shape;

import app.domain.Mode;

public final class ShapeBuilderFactory {
    public static ShapeBuilder create(Mode mode) {
        switch (mode) {
            case Rectangle:
                return new Rectangle.Builder();
            case Polygon:
                return new Polygon.Builder();
        }
        throw new RuntimeException();
    }
}
