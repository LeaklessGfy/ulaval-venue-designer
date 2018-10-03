package app.domain.painter;

import app.domain.Controller;
import app.domain.shape.Mode;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Vector;

public class Painter {
    private final Controller m_controller;

    public Painter(final Controller p_controller) {
        m_controller = p_controller;
    }

    public void paint(Graphics2D g){
        for (Shape shape : m_controller.getShapes()) {
            paintShape(g, shape);
        }
    }

    public void paintShape(Graphics2D g, Shape shape){
        Vector<Point> points = shape.getPoints();
        int nPoints = points.size();
        int[] xCoords = new int[nPoints];
        int[] yCoords = new int[nPoints];

        for (int i = 0; i < nPoints; i++) {
            xCoords[i] = points.get(i).x;
            yCoords[i] = points.get(i).y;
        }

        g.setStroke(new BasicStroke(2));

        if (shape.isValid()) {
            java.awt.Shape s = new Polygon(xCoords, yCoords, nPoints);

            if (!shape.getSelected()) {
                g.setColor(Color.lightGray);
            } else {
                g.setColor(Color.GREEN);
            }

            g.draw(s);
            g.setColor(Color.darkGray);
            g.fill(s);
        } else {
            g.setColor(Color.lightGray);
            g.drawPolyline(xCoords,yCoords,nPoints);
            Point last = points.lastElement();

            if (m_controller.getMode().equals(Mode.Polygon)) {
                g.drawLine(last.x, last.y, m_controller.getXCursor(), m_controller.getYCursor());
            } else if (m_controller.getMode().equals(Mode.Rectangle)){
                g.drawRect(last.x, last.y,m_controller.getXCursor() - last.x,m_controller.getYCursor() - last.y);
            }
        }
    }
}
