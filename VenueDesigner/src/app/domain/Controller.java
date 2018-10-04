package app.domain;

import app.domain.shape.Mode;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Controller {
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final Point cursor = new Point(-1, -1);

    private Mode mode = Mode.Rectangle;
    private UIPanel panel;

    public Controller setDrawingPanel(UIPanel p_DrawingPanel){
        panel = Objects.requireNonNull(p_DrawingPanel);
        return this;
    }

    public int getXCursor () {
        return  cursor.x;
    }

    public int getYCursor () {
        return  cursor.y;
    }

    public void mouseClicked(int x, int y) {
        if (!shapes.isEmpty() && !lastShape().isValid()) {
            lastShape().addPoint(new Point(x, y));
            panel.repaint();
            return;
        }

        Shape shape = mode.build();
        shapes.add(shape);
        shape.addPoint(new Point(x, y));

        if (lastShape().isValid()) {
            for (Shape s : shapes) {
                s.setSelected(false);
            }
        } else {
            panel.repaint();
        }
    }

    public void mouseMoved(int x, int y){
        if (shapes.isEmpty() || lastShape().isValid()) {
            return;
        }

        cursor.set(x, y);
        panel.repaint();
    }

    public boolean toggleMode(Mode p_mode) {
        Objects.requireNonNull(p_mode);

        if (mode.equals(p_mode)) {
            return false;
        }

        mode = p_mode;

        return true;
    }

    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    private Shape lastShape() {
        return shapes.get(shapes.size() - 1);
    }
}
