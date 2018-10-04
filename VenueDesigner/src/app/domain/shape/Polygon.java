package app.domain.shape;

public final class Polygon extends AbstractShape {
    @Override
    public void addPoint(Point p_point) {
        if (points.size() >= 3 && dist(points.firstElement(), p_point) <= 30) {
            setValid(true);
        } else {
            points.add(p_point);
        }
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g,this);
        /*
        if (isValid()) {
            visitor.visit(canvas, this);
        } else {
            visitor.visitTemporary(canvas, this);
        }
        */
    }

    private double dist(Point p_p1, Point p_p2) {
        int x1 = p_p1.x;
        int x2 = p_p2.x;
        int y1 = p_p1.y;
        int y2 = p_p2.y;

        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }
}
