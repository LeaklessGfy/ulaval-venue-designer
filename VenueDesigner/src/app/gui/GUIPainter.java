package app.gui;

import app.domain.Controller;
import app.domain.Room;
import app.domain.Seat;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.StandingSection;
import app.domain.shape.Painter;
import app.domain.shape.*;
import app.domain.shape.Point;
import app.domain.shape.Polygon;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

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
        numberSeats(g, seatedSection);
        for (Seat[] seats : seatedSection.getSeats()) {
            for (Seat seat : seats) {
                if (!seat.isSelected()) {
                    draw(g, seat);
                } else {
                    drawShapeColor(g, seat.getShape(), Color.GREEN, Color.GREEN);
                }
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
    private void numberSeats(Graphics2D g, SeatedSection section) {
        g.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON
                );
        Font font = Font.decode("Arial");
        int maxWidth = section.getVitalSpace().getWidth()-2;
        int maxHeight = section.getVitalSpace().getHeight()-2;
        int i=1;
        int j = 1;
        for (Seat[] row : section.getSeats()) {
            Point p = new Point(0,0);
            switch (section.getZone()){
                case Bas:
                    p = new Point(row[0].getShape().getPoints().elementAt(3).x+1-section.getVitalSpace().getWidth(),
                            row[0].getShape().getPoints().elementAt(3).y-1);
                    break;
                case Gauche:
                    p = new Point(row[0].getShape().getPoints().elementAt(2).x+1,
                            row[0].getShape().getPoints().elementAt(2).y-1-section.getVitalSpace().getWidth());
                    break;
                case Haut:
                    p = new Point(row[0].getShape().getPoints().elementAt(1).x+1+section.getVitalSpace().getWidth(),
                            row[0].getShape().getPoints().elementAt(1).y-1);
                    break;
                case Droit:
                    p = new Point(row[0].getShape().getPoints().elementAt(0).x+1,
                            row[0].getShape().getPoints().elementAt(0).y-1+section.getVitalSpace().getWidth());}
            String rowNumber = String.valueOf(j);
            Rectangle2D bounds = g.getFontMetrics(font).getStringBounds(rowNumber,g);
            font = font.deriveFont((float)(font.getSize2D()*maxWidth/Math.max(bounds.getWidth(),bounds.getHeight())));
            drawText(g, p, rowNumber, Color.WHITE, font);
            for (Seat seat : row) {
                //print seat number
                Point p0 = new Point(0,0);
                switch (section.getZone()){
                    case Bas:
                        p0 = new Point(seat.getShape().getPoints().elementAt(3).x+1,seat.getShape().getPoints().elementAt(3).y-1);
                        break;
                    case Gauche:
                        p0 = new Point(seat.getShape().getPoints().elementAt(2).x+1,seat.getShape().getPoints().elementAt(2).y-1);
                        break;
                    case Haut:
                        p0 = new Point(seat.getShape().getPoints().elementAt(1).x+1,seat.getShape().getPoints().elementAt(1).y-1);
                        break;
                    case Droit:
                        p0 = new Point(seat.getShape().getPoints().elementAt(0).x+1,seat.getShape().getPoints().elementAt(0).y-1);
                }
                String seatNumber = String.valueOf(i);
                bounds = g.getFontMetrics(font).getStringBounds(seatNumber,g);
                font = font.deriveFont((float)(font.getSize2D()*maxWidth/Math.max(bounds.getWidth(),bounds.getHeight())));
                drawText(g, p0, seatNumber, Color.WHITE, font);
                i++;
            }
            j++;
        }
    }
    private void drawText(Graphics2D g, Point point, String string, Color color, Font font){
        g.setFont(font);
        g.setColor(color);
        g.drawString(string, point.x+controller.getOffset().x, point.y+controller.getOffset().y);
    }
}
