package app.domain.section;

import app.domain.shape.Shape;
import app.domain.Seat;
import java.util.Vector;

public interface Section {
    String getName();
    int getElevation();
    Shape getShape();
    Vector<Seat> getSeats();
    void move(int x, int y);
}
