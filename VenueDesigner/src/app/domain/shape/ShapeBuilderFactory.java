package app.domain.shape;

import app.domain.Mode;

public final class ShapeBuilderFactory {
    public static ShapeBuilder create(Mode mode) {
        switch (mode) {
            case Polygon:
                return new Polygon.Builder();
            case Stage:
                return new Rectangle.Builder(139, 69, 19, 255);
            case RegularSeatedSection:
                return new Rectangle.Builder(0, 0, 0, 0);
            case RegularStandingSection:
                return new Rectangle.Builder(0,0,0,0);
        }
        throw new RuntimeException();
    }
}
