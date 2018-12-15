package app.domain;

import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Objects;
import java.util.Vector;

final class ColliderValidator {
    private final Collider collider;

    ColliderValidator(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
    }

    boolean validShape(Shape shape, Room room, Point offset) {
        if (invalidShapeRoom(shape, room, offset)) {
            return false;
        }
        if (room.getStage().isPresent()) {
            if (collider.hasCollide(shape, room.getStage().get().getShape())) {
                return false;
            }
        }
        for (Section section : room.getSections()) {
            if (collider.hasCollide(shape, section.getShape())) {
                return false;
            }
        }
        return true;
    }

    boolean validPredictShape(Shape shape, Shape predict, Room room, Point offset) {
        if (invalidShapeRoom(predict, room, offset)) {
            return false;
        }
        if (room.getStage().isPresent() && shape != room.getStage().get().getShape()) {
            if (collider.hasCollide(predict, room.getStage().get().getShape())) {
                return false;
            }
        }
        for (Section section : room.getSections()) {
            if (shape != section.getShape() && collider.hasCollide(predict, section.getShape())) {
                return false;
            }
        }
        return true;
    }

    boolean invalidShapeRoom(Shape shape, Room room, Point offset) {
        Vector<Point> points = room.getShape().getPoints();
        double x = points.firstElement().x;
        double y = points.firstElement().y;

        for (Point p : shape.getPoints()) {
            if (p.x - offset.x < x || p.x - offset.x > x + room.getWidth()) {
                return true;
            }
            if (p.y - offset.y < y || p.y - offset.y > y + room.getHeight()) {
                return true;
            }
        }

        return false;
    }
}
