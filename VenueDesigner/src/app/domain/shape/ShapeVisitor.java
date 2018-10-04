package app.domain.shape;

public interface ShapeVisitor<T> {
    void visit(T canvas, Rectangle rectangle);
    void visitTemporary(T canvas, Rectangle rectangle);

    void visit(T canvas, Polygon polygon);
    void visitTemporary(T canvas, Polygon polygon);
}
