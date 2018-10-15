package app.domain;

import app.domain.shape.Shape;

public interface Collider {
    boolean hasCollide(int x, int y, Shape shape);
}
