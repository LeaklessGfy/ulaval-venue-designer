package app.gui;

import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Vector;

final class GUIUtils {
    static Coordinates getCoordinates(Shape shape, Point offset) {
        Vector<Point> points = shape.getPoints();

        int size = points.size();
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];
        int i = 0;

        for (Point point : points) {
            xCoords[i] = point.x + offset.x;
            yCoords[i] = point.y + offset.y;
            i++;
        }

        return new Coordinates(points, xCoords, yCoords);
    }
}
