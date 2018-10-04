package app.domain.shape;

public final class ShapeFactory {
    public static Shape create(Mode mode) {
        switch (mode) {
            case Rectangle:
                return new Rectangle();
            case Polygon:
                return new Polygon();
        }
        throw new RuntimeException();
    }
}
