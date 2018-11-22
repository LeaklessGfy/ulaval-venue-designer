package app.domain.section;

import app.domain.shape.Shape;

public interface Section {
    String getName();
    int getElevation();
    Shape getShape();
    void move(int x, int y);
}
