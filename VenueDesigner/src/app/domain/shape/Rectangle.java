package app.domain.shape;

import java.util.Vector;

public final class Rectangle extends AbstractShape {
    public static class Builder implements ShapeBuilder {
        private final Vector<Point> points = new Vector<>();

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
        public void correctLastPoint() { return; }

        @Override
        public Rectangle build() {
            return new Rectangle(points);
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
        public <T> void accept(T g, Painter<T> painter) {
            painter.draw(g, this);
        }

        @Override
        public Point computeCentroid(){
            return new Point(-1,-1);
        }
    }

    public Rectangle(Vector<Point> points) {
        super(points);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }
}
