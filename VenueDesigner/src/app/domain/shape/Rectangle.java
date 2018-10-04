package app.domain.shape;

public final class Rectangle extends AbstractShape {
    @Override
    public void addPoint(Point p_point){
        if (points.isEmpty()) {
            points.add(p_point);
        } else {
            points.add(new Point(p_point.x, points.firstElement().y));
            points.add(p_point);
            points.add(new Point(points.firstElement().x, p_point.y));
            setValid(true);
        }
    }

    @Override
    public <T> void accept(T canvas, ShapeVisitor<T> visitor) {
        if (isValid()) {
            visitor.visit(canvas, this);
        } else {
            visitor.visitTemporary(canvas, this);
        }
    }
}
