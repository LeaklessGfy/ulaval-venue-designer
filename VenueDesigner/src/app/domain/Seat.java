package app.domain;

import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;

import java.util.Vector;

public final class Seat {
    private final int column;
    private final int row;
    private final Shape shape;

    private int price;
    private int color;

    public Seat(int column, int row, VitalSpace vs, Point p0) {
        this.column = column;
        this.row = row;
        Vector<Point> points = new Vector<>();
        points.add(new Point(p0.x+(column)*vs.getWidth(), p0.y+(row)*vs.getHeight()));
        points.add(new Point(p0.x+(column+1)*vs.getWidth(), p0.y+(row)*vs.getHeight()));
        points.add(new Point(p0.x+(column+1)*vs.getWidth(), p0.y+(row+1)*vs.getHeight()));
        points.add(new Point(p0.x+(column)*vs.getWidth(), p0.y+(row+1)*vs.getHeight()));
        shape = new Rectangle(points, new int[4]);
    }

    public Shape getShape(){
        return shape;
    }

    public boolean isSelected() {
        return shape.isSelected();
    }

    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }

    public void move(int x, int y, Point offset){
        shape.move(x, y, offset);
    }
}
