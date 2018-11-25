package app.gui;

import app.domain.shape.Point;

import java.util.Vector;

final class GUIUtils {
    static Coordinates getCoordinates(Vector<Point> points, Point offset) {
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
