package app.domain.shape;

import app.domain.Mode;

public final class ShapeBuilderFactory {
    public static ShapeBuilder create(Mode mode) {
        switch (mode) {
            case IrregularSeatedSection:
                return new Polygon.Builder(63, 63, 76, 255);
            case IrregularStandingSection:
                return new Polygon.Builder(63, 63, 76, 255);
            case Stage:
                return new Rectangle.Builder(139, 69, 19, 255);
            case RegularSeatedSection:
                return new Rectangle.Builder(63, 63, 76, 255);
        }
        throw new RuntimeException();
    }
}
