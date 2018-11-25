package app.gui;

import app.domain.Controller;
import app.domain.shape.Painter;
import app.domain.shape.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Vector;

public final class GUIPainter implements Painter<Graphics2D> {
    private final Controller controller;

    GUIPainter(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    void draw(Graphics2D g) {
        controller.getRoom().ifPresent(r -> r.accept(g, this));
        controller.getCurrent().ifPresent(s -> s.accept(g, this));
    }

    @Override
    public void draw(Graphics2D g, Rectangle rectangle) {
        drawFinal(g, rectangle);
    }

    @Override
    public void draw(Graphics2D g, Polygon polygon) {
        drawFinal(g, polygon);
    }

    @Override
    public void draw(Graphics2D g, Rectangle.Builder rectangle) {
        Coordinates coordinates = GUIUtils.getCoordinates(rectangle.getPoints(), new Point(0, 0));

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        app.domain.shape.Point last = coordinates.points.lastElement();

        int startX = last.x;
        int startY = last.y;
        int endX = controller.getXCursor() - last.x;
        int endY = controller.getYCursor() - last.y;

        if (endX < 0) {
            startX = controller.getXCursor();
            endX = last.x - controller.getXCursor();
        }

        if (endY < 0) {
            startY = controller.getYCursor();
            endY = last.y - controller.getYCursor();
        }

        g.drawRect(startX, startY, endX, endY);
    }

    @Override
    public void draw(Graphics2D g, Polygon.Builder polygon) {
        Coordinates coordinates = GUIUtils.getCoordinates(polygon.getPoints(), new Point(0, 0));

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        app.domain.shape.Point last = coordinates.points.lastElement();

        g.drawLine(last.x, last.y, controller.getXCursor(), controller.getYCursor());
    }

    private void drawFinal(Graphics2D g, Shape shape) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape.getPoints(), controller.getOffset());
        java.awt.Polygon polygon = new java.awt.Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);

        int[] color = shape.getColor();
        g.draw(polygon);
        g.setColor(new Color(color[0], color[1], color[2]));
        g.fill(polygon);
        if (shape.isSelected()) {
            Vector<Point> points = shape.getPoints();
            Point centroid = shape.computeCentroid();
            int[] xSel = new int[points.size()];
            int [] ySel = new int[points.size()];
            int i=0;
            for (Point point: points)
            {
                if (point.x-centroid.x>0){
                    xSel[i] = point.x + controller.getOffset().x+2;
                }
                else {
                    xSel[i] = point.x + controller.getOffset().x-2;
                }
                if (point.y-centroid.y>0){
                    ySel[i] = points.elementAt(i).y + controller.getOffset().y+2;
                }
                else {
                    ySel[i] = point.y + controller.getOffset().y-2;
                }
                i++;
            }
            java.awt.Polygon polygonSel = new java.awt.Polygon(xSel, ySel, points.size());
            g.setColor(Color.GREEN);
            g.draw(polygonSel);
            g.fill(polygonSel);
        }
    }
}
