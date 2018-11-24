package app.gui;

import app.domain.Controller;
import app.domain.Seat;
import app.domain.Room;
import app.domain.section.Section;
import app.domain.shape.Painter;
import app.domain.shape.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

public final class GUIPainter implements Painter<Graphics2D> {
    private final Controller controller;

    GUIPainter(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    void draw(Graphics2D g) {
        controller.getCurrent().ifPresent(s -> s.accept(g, this));

        if (controller.getRoom() != null) {
            draw(g, controller.getRoom());
        }

        // TODO: Delete get shapes because it's not on controller to do that
        for (Shape shape : controller.getShapes()) {
            shape.accept(g, this);
        }

        controller.getStage().ifPresent(s -> s.getShape().accept(g, this));

        for (Section section : controller.getSections()) {
            section.getShape().accept(g, this);
            for (Seat seat: section.getSeats()) {
                seat.getShape().accept(g,this);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, Room room) {
        DrawingPanel drawingPanel = (DrawingPanel)controller.getDrawingPanel();
        int startX = (drawingPanel.getWidth()/ 2) - (room.getWidth() / 2);
        int startY = (drawingPanel.getHeight() / 2) - (room.getHeight() / 2);
        g.drawRect(startX, startY, room.getWidth(), room.getHeight());
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
        Coordinates coordinates = GUIUtils.getCoordinates(rectangle);

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
        Coordinates coordinates = GUIUtils.getCoordinates(polygon);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        app.domain.shape.Point last = coordinates.points.lastElement();

        g.drawLine(last.x, last.y, controller.getXCursor(), controller.getYCursor());
    }

    private void drawFinal(Graphics2D g, Shape shape) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape);
        java.awt.Polygon polygon = new java.awt.Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());

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
