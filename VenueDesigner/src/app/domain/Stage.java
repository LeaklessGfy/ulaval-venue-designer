package app.domain;

import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Vector;

public final class Stage implements Selection {
    private final Shape shape;
    private int elevation;

    @JsonCreator
    public Stage(@JsonProperty("shape") Shape shape) {
        this.shape = Objects.requireNonNull(shape);
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
    public void move(int x, int y) {
        shape.move(x, y);
    }

    @Override
    public void move(int x, int y, Point offset) {
        shape.move(x, y, offset);
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void rotate(double thetaRadian){
        shape.rotate(thetaRadian);
    }

    public Shape getShape() {
        return shape;
    }

    public int getWidth() {
        Vector<Point> points = shape.getPoints();
        return points.get(1).x - points.firstElement().x;
    }

    public int getHeight() {
        Vector<Point> points = shape.getPoints();
        return points.lastElement().y - points.firstElement().y;
    }

    public int getElevation() {
        return elevation;
    }

    public void setWidth(int width) {
        Vector<Point> points = shape.getPoints();
        int x = points.firstElement().x;
        points.get(1).x = x + width;
        points.get(2).x = x + width;
    }

    public void setHeight(int height) {
        Vector<Point> points = shape.getPoints();
        int y = points.firstElement().y;
        points.get(2).y = y + height;
        points.get(3).y = y + height;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

}
