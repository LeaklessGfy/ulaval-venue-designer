package app.domain;

import app.domain.shape.Mode;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.awt.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Controller {
    private final ArrayList<Shape> m_shapes = new ArrayList<>();

    private Mode m_mode = Mode.Rectangle;
    private UIPanel m_drawingPanel;
    private Point m_cursor = new Point(-1, -1);

    public void setDrawingPanel(UIPanel p_DrawingPanel){
        m_drawingPanel = Objects.requireNonNull(p_DrawingPanel);
    }

    public int getXCursor () {
        return  m_cursor.x;
    }

    public int getYCursor () {
        return  m_cursor.y;
    }

    public void mouseClicked(int x, int y) {
        if (!m_shapes.isEmpty() && !lastShape().isValid()) {
            lastShape().addPoint(new Point(x, y));
            m_drawingPanel.repaint();
            return;
        }

        Shape shape = m_mode.build();
        m_shapes.add(shape);
        shape.addPoint(new Point(x, y));

        if (lastShape().isValid()) {
            for (Shape s : m_shapes) {
                s.setSelected(false);
            }
        } else {
            for (Shape s : m_shapes) {
                int nPoints = s.getPoints().size();
                int[] xCoords = new int[nPoints];
                int[] yCoords = new int[nPoints];
                int i = 0;
                for (Point point : s.getPoints()) {
                    xCoords[i] = point.x;
                    yCoords[i] = point.y;
                    i++;
                }
                java.awt.Shape sh = new Polygon(xCoords, yCoords, nPoints);
            }

            m_drawingPanel.repaint();
        }
    }

    public void mouseMoved(int x, int y){
        if (m_shapes.isEmpty() || lastShape().isValid()) {
            return;
        }

        m_cursor.set(x, y);
        m_drawingPanel.repaint();
    }

    public boolean toggleMode(Mode p_mode) {
        Objects.requireNonNull(p_mode);

        if (m_mode.equals(p_mode)) {
            return false;
        }

        m_mode = p_mode;

        return true;
    }

    public Mode getMode() {
        return m_mode;
    }

    public List<Shape> getShapes() {
        return Collections.unmodifiableList(m_shapes);
    }

    private Shape lastShape() {
        return m_shapes.get(m_shapes.size() - 1);
    }
}
