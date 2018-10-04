package app.gui;

import app.domain.Painter;
import app.domain.shape.*;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;

public final class GUIPainter implements Painter<Graphics2D> {
    private final ShapeVisitor<Graphics2D> visitor;

    GUIPainter(ShapeVisitor<Graphics2D> visitor) {
        this.visitor = Objects.requireNonNull(visitor);
    }

    public void draw(Graphics2D g, List<Shape> shapes) {
        for (Shape shape : shapes) {
            shape.accept(g, visitor);
        }
    }
}
