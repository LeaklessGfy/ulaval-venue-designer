package app.gui;

import app.domain.shape.Point;

import java.util.Vector;

class Coordinates {
    final Vector<Point> points;
    final int size;
    final int[] xCoords;
    final int[] yCoords;

    Coordinates(Vector<Point> points, int size, int[] xCoords, int[] yCoords) {
        this.points = points;
        this.size = size;
        this.xCoords = xCoords;
        this.yCoords = yCoords;
    }
}
