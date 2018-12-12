package app.domain;

import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Objects;

public final class SeatSection implements Selection {
    private final Seat[] seats;

    SeatSection(Seat[] seats) {
        this.seats = Objects.requireNonNull(seats);
    }

    @Override
    public boolean isSelected() {
        return seats[0].isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        for (Seat seat : seats) {
            seat.setSelected(selected);
        }
    }

    @Override
    public void move(int x, int y) {
    }

    @Override
    public void move(int x, int y, Point offset) {
    }

    @Override
    public void rotate(double theta_radian) {
    }

    @Override
    public Shape getShape() {
        return seats[0].getShape();
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    public Seat[] getSeats() {
        return seats;
    }
}
