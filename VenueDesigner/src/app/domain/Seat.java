package app.domain;

import app.domain.section.Zone;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Seat(int column, int row, VitalSpace vs, Point p0, Zone zone) {
        this.column = column;
        this.row = row;
        Vector<Point> points = new Vector<>();
        int x=0;
        int y=0;
        switch (zone){
            case Bas:{
                x= p0.x+(column)*vs.getWidth();
                y=p0.y+(row)*vs.getHeight();
                break;
            }
            case Gauche:{
                x= p0.x-(row)*vs.getHeight();
                y=p0.y+(column)*vs.getWidth();
                break;
            }
            case Haut:{
                x= p0.x-(column)*vs.getWidth();
                y=p0.y-(row)*vs.getHeight();
                break;
            }
            case Droit:{
                x= p0.x+(row)*vs.getHeight();
                y=p0.y-(column)*vs.getWidth();
                break;
            }
        }
        shape = Rectangle.create(x,y,vs.getWidth(),vs.getHeight(), new int[4], zone);
    }

    @JsonCreator
    public Seat(@JsonProperty("column") int column, @JsonProperty("row") int row, @JsonProperty("shape") Shape shape) {
        this.column = column;
        this.row = row;
        this.shape = shape;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }
}
