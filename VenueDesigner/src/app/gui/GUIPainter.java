package app.gui;

import app.domain.Controller;
import app.domain.shape.Painter;
import app.domain.shape.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;

public final class GUIPainter implements Painter<Graphics2D> {
    static class Coordinates {
        private final List<Point> points;
        private final int size;
        private final int[] xCoords;
        private final int[] yCoords;

        Coordinates(List<app.domain.shape.Point> points, int size, int[] xCoords, int[] yCoords) {
            this.points = points;
            this.size = size;
            this.xCoords = xCoords;
            this.yCoords = yCoords;
        }
    }

    private final Controller controller;

    GUIPainter(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    void draw(Graphics2D g) {
        for (Shape shape : controller.getShapes()) {
            shape.accept(g, this);
        }
    }

    @Override
    public void draw(Graphics2D g, Rectangle rectangle) {
        if (rectangle.isValid()) {
            drawFinal(g, rectangle);
        } else {
            drawTemporary(g, rectangle);
        }
    }

    @Override
    public void draw(Graphics2D g, Polygon polygon) {
        if (polygon.isValid()) {
            drawFinal(g, polygon);
        } else {
            drawTemporary(g, polygon);
        }
    }

    private Coordinates getCoordinates(Shape shape) {
        List<Point> points = shape.getPoints();

        int size = points.size();
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];
        int i = 0;

        for (app.domain.shape.Point point : points) {
            xCoords[i] = point.x;
            yCoords[i] = point.y;
            i++;
        }

        return new Coordinates(points, size, xCoords, yCoords);
    }

    private void drawFinal(Graphics2D g, Shape shape) {
        Coordinates coordinates = getCoordinates(shape);
        java.awt.Polygon polygon = new java.awt.Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.size);

        g.setStroke(new BasicStroke(2));
        if (!shape.isSelected()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(Color.GREEN);
        }
        g.draw(polygon);
        g.setColor(Color.darkGray);
        g.fill(polygon);
    }

    private void drawTemporary(Graphics2D g, Rectangle rectangle) {
        Coordinates coordinates = getCoordinates(rectangle);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.size);
        app.domain.shape.Point last = coordinates.points.get(coordinates.points.size() - 1);

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

    private void drawTemporary(Graphics2D g, Polygon polygon) {
        Coordinates coordinates = getCoordinates(polygon);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.size);
        app.domain.shape.Point last = coordinates.points.get(coordinates.points.size() - 1);

        g.drawLine(last.x, last.y, controller.getXCursor(), controller.getYCursor());
    }
}
