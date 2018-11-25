package app.domain.section;

import app.domain.Seat;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Vector;

public interface Section {
    String getName();
    int getElevation();
    boolean isSelected();
    void setSelected(boolean selected);
    Vector<Seat> getSeats();
    Shape getShape();
    void move(int x, int y, Point offset);
    <T> void accept(T g, Painter<T> painter);
}
