package app.gui;

import app.domain.Controller;
import app.domain.shape.Point;
import app.domain.shape.Polygon;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import app.domain.shape.ShapeVisitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;

public final class GUIVisitor implements ShapeVisitor<Graphics2D> {
    static class Coordinates {
        private final List<app.domain.shape.Point> points;
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

    GUIVisitor(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    @Override
    public void visit(Graphics2D g, Rectangle rectangle) {
        draw(g, getCoordinates(rectangle), rectangle);
    }

    @Override
    public void visitTemporary(Graphics2D g, Rectangle rectangle) {
        Coordinates coordinates = getCoordinates(rectangle);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.size);
        app.domain.shape.Point last = coordinates.points.get(coordinates.points.size() - 1);

        g.drawRect(last.x, last.y,controller.getXCursor() - last.x,controller.getYCursor() - last.y);
    }

    @Override
    public void visit(Graphics2D g, Polygon polygon) {
        draw(g, getCoordinates(polygon), polygon);
    }

    @Override
    public void visitTemporary(Graphics2D g, Polygon polygon) {
        Coordinates coordinates = getCoordinates(polygon);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.size);
        app.domain.shape.Point last = coordinates.points.get(coordinates.points.size() - 1);

        g.drawLine(last.x, last.y, controller.getXCursor(), controller.getYCursor());
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

    private void draw(Graphics2D g, Coordinates coordinates, Shape shape) {
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
}
