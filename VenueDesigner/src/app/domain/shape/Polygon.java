package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Vector;

public final class Polygon extends AbstractShape {
    public static class Builder implements ShapeBuilder {
        private final Vector<Point> points = new Vector<>();
        private final int[] color = new int[4];

        Builder() {}

        Builder(int r, int g, int b, int a) {
            color[0] = r;
            color[1] = g;
            color[2] = b;
            color[3] = a;
        }

        @Override
        public void addPoint(Point point) {
            points.add(point);
        }

        @Override
        public boolean isComplete() {
            return points.size() >= 3 && dist(points.firstElement(), points.lastElement()) <= 10;
        }

        @Override
        public void correctLastPoint() {
            points.removeElementAt(points.size() - 1);
        }

        @Override
        public Shape build() {
            return new Polygon(points, color);
        }

        @Override
        public Vector<Point> getPoints() {
            return points;
        }

        @Override
        public <T> void accept(T g, Painter<T> painter) {
            painter.draw(g, this);
        }

        private double dist(Point p_p1, Point p_p2) {
            double x1 = p_p1.x;
            double x2 = p_p2.x;
            double y1 = p_p1.y;
            double y2 = p_p2.y;

            return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
        }
    }

    @JsonCreator
    public Polygon(@JsonProperty("points") Vector<Point> points, @JsonProperty("color") int[] color) { super(points, color); }

    public Polygon(Polygon polygon) { super(polygon); }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g,this);
    }

    @Override
    public Shape clone() {
        return new Polygon(this);
    }
}
