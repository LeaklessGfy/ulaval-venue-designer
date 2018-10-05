package app.gui;

import app.domain.Collider;
import app.domain.shape.Shape;

import java.awt.Polygon;

public final class GUICollider implements Collider {
    @Override
    public boolean hasCollide(int x, int y, Shape shape) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape);
        Polygon polygon = new Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        return polygon.contains(x, y);
    }
}
