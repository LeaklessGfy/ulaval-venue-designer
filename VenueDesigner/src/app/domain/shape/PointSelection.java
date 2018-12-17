package app.domain.shape;

import app.domain.Stage;
import app.domain.collider.Collider;
import app.domain.section.SeatedSection;
import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;

import java.util.Objects;

public final class PointSelection implements Selection {
    private final Shape shape;
    private final Point point;
    private SeatedSection seatedSection;

    public PointSelection(Shape shape, Point point) {
        this.shape = Objects.requireNonNull(shape);
        this.point = Objects.requireNonNull(point);
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    @Override
    public void setSelected(boolean selected) {}

    @Override
    public void move(double x, double y, Point offset) {
        Point centroid = shape.computeCentroid();
        double dx = point.x - centroid.x;
        double dy = point.y - centroid.y;
        point.set((x+dx) - offset.x, (y+dy) - offset.y);
    }

    @Override
    public void rotate(double thetaRadian) {}

    @Override
    public Shape getShape() {
        return null;
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isAuto() {
        return false;
    }

    public void setSeatedSection(SeatedSection seatedSection) {
        this.seatedSection = seatedSection;
    }

    public void recalculate(Stage stage, Collider collider) {
        if (seatedSection != null) {
            seatedSection.autoSetSeats(stage, collider);
        }
    }
}
