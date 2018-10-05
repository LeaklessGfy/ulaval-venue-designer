package app.domain.shape;

import java.util.Objects;
import java.util.Vector;

abstract class AbstractShape implements Shape {
    private final Vector<Point> points;
    private boolean selected;

    public AbstractShape(Vector<Point> points) {
        this.points = Objects.requireNonNull(points);
        this.selected = false;
    }

    public void setSelected(boolean bool) {
        selected = bool;
    }

    public boolean isSelected() {
        return selected;
    }

    public Vector<Point> getPoints() {
        return points;
    }
}
