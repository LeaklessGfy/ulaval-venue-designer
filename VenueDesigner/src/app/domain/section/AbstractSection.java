package app.domain.section;

import app.domain.shape.Point;
import app.domain.shape.Shape;

abstract class AbstractSection implements Section {
    private String name;
    private int elevation;
    private Shape shape;

    AbstractSection(String name, int elevation, Shape shape) {
        this.name = name;
        this.elevation = elevation;
        this.shape = shape;
    }

    AbstractSection(Shape shape) {
        this(null, 0, shape);
    }

    @Override
    public String getName() {
        return name == null ? "" : name;
    }

    @Override
    public int getElevation() {
        return elevation;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public  void move(int x, int y) {
        this.shape.move(x,y);
    }
}
