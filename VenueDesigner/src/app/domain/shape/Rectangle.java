package app.domain.shape;

import java.util.Vector;

public final class Rectangle extends AbstractShape {
    public static class Builder implements ShapeBuilder {
        private final Vector<Point> points = new Vector<>();
        private final int[] color = new int[3];

        Builder() {}

        Builder(int r, int g, int b) {
            color[0] = r;
            color[1] = g;
            color[2] = b;
        }

        @Override
        public void addPoint(Point point) {
            if (points.isEmpty()) {
                points.add(point);
            } else {
                points.add(new Point(point.x, points.firstElement().y));
                points.add(point);
                points.add(new Point(points.firstElement().x, point.y));
            }
        }

        @Override
        public boolean isComplete() {
            return points.size() == 4;
        }

        @Override
        public void correctLastPoint() {}

        @Override
        public Rectangle build() {
            return new Rectangle(points, color);
        }

        @Override
        public void setSelected(boolean selected) {}

        @Override
        public void move(int x, int y) {}

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public Vector<Point> getPoints() {
            return points;
        }

        @Override
        public int[] getColor() {
            return color;
        }

        @Override
        public <T> void accept(T g, Painter<T> painter) {
            painter.draw(g, this);
        }

        @Override
        public Point computeCentroid(){
            return new Point(-1,-1);
        }
    }

    public Rectangle(Vector<Point> points, int[] color) {
        super(points, color);
    }

    public static Rectangle create(int x, int y, int width, int height, int[] color) {
        Vector<Point> points = new Vector<>();
        points.add(new Point(x, y));
        points.add(new Point(x + width, y));
        points.add(new Point(x + width, y + height));
        points.add(new Point(x, y + height));

        return new Rectangle(points, color);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }
}
