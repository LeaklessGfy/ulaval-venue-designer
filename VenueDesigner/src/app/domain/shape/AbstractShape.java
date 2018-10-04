package app.domain.shape;

import java.util.Vector;

abstract class AbstractShape implements Shape {
    final Vector<Point> points = new Vector<>();
    private boolean valid = false;
    private boolean selected = false;

    public void setSelected(boolean bool) {
        selected = bool;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setValid(boolean bool) {
        valid = bool;
    }

    public boolean isValid() {
        return valid;
    }

    public Vector<Point> getPoints() {
        return points;
    }
}
