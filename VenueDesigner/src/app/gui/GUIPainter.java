package app.gui;

import app.domain.Controller;
import app.domain.Room;
import app.domain.seat.Seat;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.StandingSection;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Polygon;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Vector;

final class GUIPainter implements Painter<Graphics2D> {
    private final Controller controller;

    GUIPainter(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    void draw(Graphics2D g) {
        g.scale(controller.getScale(), controller.getScale());
        controller.getRoom().accept(g, this);
        controller.getCurrent().ifPresent(s -> s.accept(g, this));
        drawGrid(g);
    }

    @Override
    public void draw(Graphics2D g, Room room) {
        room.getShape().accept(g, this);
        room.getStage().ifPresent(stage -> draw(g, stage));
        room.getSections().forEach(section -> section.accept(g, this));
    }

    @Override
    public void draw(Graphics2D g, SeatedSection seatedSection) {
        seatedSection.getShape().accept(g, this);
        for (Seat[] seats : seatedSection.getSeats()) {
            for (Seat seat : seats) {
                if (!seat.isSelected()) {
                    draw(g, seat);
                } else {
                    drawShapeColor(g, seat.getShape(), Color.GREEN, Color.GREEN);
                }
            }
        }
        if(seatedSection.getShape().isSelected()){ drawFinalPerimeter(g,seatedSection.getShape());}
        numberSeats(g, seatedSection);

    }

    @Override
    public void draw(Graphics2D g, StandingSection standingSection) {
        standingSection.getShape().accept(g, this);
        drawFinalPerimeter(g,standingSection.getShape());
    }

    @Override
    public void draw(Graphics2D g, Seat seat) {
        seat.getShape().accept(g, this);
        drawFinalPerimeter(g,seat.getShape());
    }

    @Override
    public void draw(Graphics2D g, Stage stage) {
        stage.getShape().accept(g, this);
        drawFinalPerimeter(g,stage.getShape());
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

        double startX = last.x;
        double startY = last.y;
        double endX = controller.getXCursor() - last.x;
        double endY = controller.getYCursor() - last.y;

        if (endX < 0) {
            startX = controller.getXCursor();
            endX = last.x - controller.getXCursor();
        }

        if (endY < 0) {
            startY = controller.getYCursor();
            endY = last.y - controller.getYCursor();
        }

        g.drawRect((int)Math.round(startX), (int)Math.round(startY), (int)Math.round(endX), (int)Math.round(endY));
    }

    @Override
    public void draw(Graphics2D g, Polygon.Builder polygon) {
        Coordinates coordinates = GUIUtils.getCoordinates(polygon.getPoints(), new Point(0, 0));

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.lightGray);
        g.drawPolyline(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        app.domain.shape.Point last = coordinates.points.lastElement();

        g.drawLine((int)Math.round(last.x), (int)Math.round(last.y), (int)Math.round(controller.getXCursor()), (int)Math.round(controller.getYCursor()));
    }

    private void drawFinal(Graphics2D g, Shape shape) {
        Color stroke = new Color(0,0,0,0);
        if (shape.isSelected()) {
            stroke = Color.GREEN;
        }
        int[] color = shape.getColor();
        Color fill = new Color(color[0], color[1], color[2], color[3]);
        drawShapeColor(g, shape, stroke, fill);
    }

    private void drawFinalPerimeter(Graphics2D g, Shape shape) {
        Color stroke = Color.lightGray;
        if (shape.isSelected()) {
            stroke = Color.GREEN;
        }
        Color fill = new Color(0,0,0,0);
        drawShapeColor(g, shape, stroke, fill);
    }

    private void drawShapeColor(Graphics2D g, Shape shape, Color stroke, Color fill) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape.getPoints(), controller.getOffset());
        java.awt.Polygon polygon = new java.awt.Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        int lineWidth;
        if (controller.getScale() >= 0.8) {
            lineWidth = 2;
        } else {
            lineWidth = (int)(-10 * controller.getScale() + 10);
        }
        g.setStroke(new BasicStroke(lineWidth));
        g.setColor(stroke);
        g.draw(polygon);
        g.setColor(fill);
        g.fill(polygon);
    }

    private void numberSeats(Graphics2D g, SeatedSection section) {
        if (section.getSeats().length==0){return;}
        g.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON
                );
        Font font = Font.decode("Arial");
        double maxWidth = section.getVitalSpace().getWidth()/2.0;
        double x_space = Math.max(section.getSeats()[0][0].getShape().getPoints().elementAt(0).x,
                section.getSeats()[0][0].getShape().getPoints().elementAt(2).x)-
                Math.min(section.getSeats()[0][0].getShape().getPoints().elementAt(0).x,
                        section.getSeats()[0][0].getShape().getPoints().elementAt(2).x);
        double y_space = Math.max(section.getSeats()[0][0].getShape().getPoints().elementAt(1).y,
                section.getSeats()[0][0].getShape().getPoints().elementAt(3).y)-
                Math.min(section.getSeats()[0][0].getShape().getPoints().elementAt(1).y,
                        section.getSeats()[0][0].getShape().getPoints().elementAt(3).y);
        int i = 1;
        int j = 1;
        for (Seat[] row : section.getSeats()) {
            double dx = row[0].getShape().getPoints().elementAt(0).x-row[0].getShape().getPoints().elementAt(1).x;
            double dy = row[0].getShape().getPoints().elementAt(0).y-row[0].getShape().getPoints().elementAt(1).y;
            double x=Math.min(row[0].getShape().getPoints().elementAt(0).x,row[0].getShape().getPoints().elementAt(2).x)+x_space/3.0+dx;
            double y=Math.min(row[0].getShape().getPoints().elementAt(1).y,row[0].getShape().getPoints().elementAt(3).y)+2*y_space/3.0+dy;
            Point p = new Point(x,y);
            String rowNumber = String.valueOf(j);
            Rectangle2D bounds = g.getFontMetrics(font).getStringBounds(rowNumber,g);
            font = font.deriveFont((float)(font.getSize2D()*maxWidth/Math.max(bounds.getWidth(),bounds.getHeight())));
            drawText(g, p, rowNumber, Color.YELLOW, font);
            for (Seat seat : row) {
                x=(int)Math.round(Math.min(seat.getShape().getPoints().elementAt(0).x,seat.getShape().getPoints().elementAt(2).x)+x_space/3.0);
                y=(int)Math.round(Math.min(seat.getShape().getPoints().elementAt(1).y,seat.getShape().getPoints().elementAt(3).y)+2*y_space/3.0);
                p = new Point(x,y);
                String seatNumber = String.valueOf(i);
                bounds = g.getFontMetrics(font).getStringBounds(seatNumber,g);
                font = font.deriveFont((float)(font.getSize2D()*maxWidth/Math.max(bounds.getWidth(),bounds.getHeight())));
                drawText(g, p, seatNumber, Color.WHITE, font);
                i++;
            }
            j++;
        }
    }

    private void drawText(Graphics2D g, Point point, String string, Color color, Font font){
        g.setFont(font);
        g.setColor(color);
        g.drawString(string, (int)Math.round(point.x+controller.getOffset().x), (int)Math.round(point.y+controller.getOffset().y));
    }

    private void drawGrid(Graphics2D g){
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.DARK_GRAY);

        boolean isGridOn = controller.isGridOn();
        double delta = controller.getDelta();
        Coordinates perimeterCoords = GUIUtils.getCoordinates(controller.getRoom().getShape().getPoints(),controller.getOffset());
        double width = Math.abs(perimeterCoords.xCoords[1]-perimeterCoords.xCoords[0]);
        double height = Math.abs(perimeterCoords.yCoords[1]-perimeterCoords.yCoords[2]);
        if (!isGridOn)return;
        double i = 0;
        double j = 0;
        while (i<= width){
            Vector<Point> points = new Vector<>();
            points.add(new Point(i,0));
            points.add(new Point(i,height));
            drawLine(g,points);
            i+= delta;
        }
        while (j<=height){
            Vector<Point> points = new Vector<>();
            points.add(new Point(0,j));
            points.add(new Point(width,j));
            drawLine(g,points);
            j+= delta;
        }
    }

    private void drawLine(Graphics2D g, Vector<Point> points){
        Coordinates coordinates = GUIUtils.getCoordinates(points, controller.getOffset());
        g.drawLine(coordinates.xCoords[0], coordinates.yCoords[0],coordinates.xCoords[1],coordinates.yCoords[1]);
    }
}
