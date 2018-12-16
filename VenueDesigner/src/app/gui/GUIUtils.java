package app.gui;

import app.domain.shape.Point;

import java.awt.Color;
import java.util.Vector;

final class GUIUtils {
    static Coordinates getCoordinates(Vector<Point> points, Point offset) {
        int size = points.size();
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];
        int i = 0;

        for (Point point : points) {
            xCoords[i] = (int)Math.round(point.x + offset.x);
            yCoords[i] = (int)Math.round(point.y + offset.y);
            i++;
        }

        return new Coordinates(points, xCoords, yCoords);
    }

    static boolean isNotInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
    static boolean isNotNumber(String text){
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    static int[] colorToArray(Color color) {
        return new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
    }
}
