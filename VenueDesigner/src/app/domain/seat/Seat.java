package app.domain.seat;

import app.domain.VitalSpace;
import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Vector;

public final class Seat implements Selection {
    private final int column;
    private final int row;
    private final Shape shape;
    private double price;
    private int number;

    public Seat(int column, int row, VitalSpace vs, Point p0, int number) {
        this.column = column;
        this.row = row;
        Vector<Point> points = new Vector<>();
        points.add(new Point(p0.x+(column)*vs.getWidth(), p0.y+(row)*vs.getHeight()));
        points.add(new Point(p0.x+(column+1)*vs.getWidth(), p0.y+(row)*vs.getHeight()));
        points.add(new Point(p0.x+(column+1)*vs.getWidth(), p0.y+(row+1)*vs.getHeight()));
        points.add(new Point(p0.x+(column)*vs.getWidth(), p0.y+(row+1)*vs.getHeight()));
        shape = new Rectangle(points, new int[4]);
        price = 0.0;
        this.number = number;
    }

    public Seat(int row, int column, VitalSpace vs, Point p0, double theta, int number, boolean autoPositioning) {
        this.column = column;
        this.row = row;
        double x = 0;
        double y = 0;
        if (autoPositioning){
            x = (row)*vs.getHeight();
            y = (column)*vs.getWidth();
        }
        double x1 = x*Math.cos(-theta) - y*Math.sin(-theta);
        double y1 = x*Math.sin(-theta) + y*Math.cos(-theta);
        int[] color = {0,0,0,255};
        shape = Rectangle.create(p0.x+x1,p0.y-y1,vs.getWidth(),vs.getHeight(), color, theta);
        price = 0.0;
        this.number = number;
    }

    @JsonCreator
    public Seat(@JsonProperty("column") int column, @JsonProperty("row") int row, @JsonProperty("shape") Shape shape,
                @JsonProperty("price") double price)
    {
        this.column = column;
        this.row = row;
        this.shape = shape;
        this.price = price;
    }

    public Seat(Seat seat) {
        this.column = seat.column;
        this.row = seat.row;
        this.shape = seat.shape.clone();
        this.price = seat.price;
        this.number = seat.number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean isSelected() {
        return shape.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }

    @Override
    public void move(double x, double y, Point offset){
        shape.move(x, y, offset);
    }

    @Override
    public Shape getShape(){
        return shape;
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void rotate(double thetaRadian){}

    public void rotate(double thetaRadian, Point sectionCenter){
        shape.rotate(thetaRadian, sectionCenter);
    }

    @Override
    @JsonIgnore
    public boolean isAuto(){
        return false;
    }

    public boolean isSameSeat(Seat seat) {
        return this.column == seat.column &&
        this.row == seat.row &&
        this.shape.isSameShape(seat.shape) &&
        this.price == seat.price &&
        this.number == seat.number;
    }
}
