package app.gui;

import app.domain.Controller;
import app.domain.Room;
import app.domain.Seat;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
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
    public void draw(Graphics2D g, Room room) {
        room.getShape().accept(g, this);
        room.getStage().ifPresent(stage -> draw(g, stage));
        room.getSections().forEach(section -> section.accept(g, this));
    }

    @Override
    public void draw(Graphics2D g, SeatedSection seatedSection) {
        for (Seat seat : seatedSection.getSeats()) {
            if (!seat.isSelected()) {
                draw(g, seat);
            } else {
                drawShapeColor(g, seat.getShape(), Color.GREEN, Color.GREEN);
            }
        }
        seatedSection.getShape().accept(g, this);
    }

    @Override
    public void draw(Graphics2D g, StandingSection standingSection) {
        standingSection.getShape().accept(g, this);
    }

    @Override
    public void draw(Graphics2D g, Seat seat) {
        seat.getShape().accept(g, this);
    }

    @Override
    public void draw(Graphics2D g, Stage stage) {
        stage.getShape().accept(g, this);
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
        Color stroke = Color.lightGray;
        if (shape.isSelected()) {
            stroke = Color.GREEN;
        }
        int[] color = shape.getColor();
        Color fill = new Color(color[0], color[1], color[2], color[3]);
        drawShapeColor(g, shape, stroke, fill);
    }

    private void drawShapeColor(Graphics2D g, Shape shape, Color stroke, Color fill) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape.getPoints(), controller.getOffset());
        java.awt.Polygon polygon = new java.awt.Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());

        g.setStroke(new BasicStroke(2));
        g.setColor(stroke);
        g.draw(polygon);
        g.setColor(fill);
        g.fill(polygon);
    }
}
