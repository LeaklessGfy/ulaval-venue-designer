package app.domain.shape;

import app.domain.Mode;

public final class ShapeBuilderFactory {
    public static ShapeBuilder create(Mode mode) {
        switch (mode) {
            case Polygon:
                return new Polygon.Builder();
            case Stage:
            case RegularSeatedSection:
                return new Rectangle.Builder();
        }
        throw new RuntimeException();
    }
}
