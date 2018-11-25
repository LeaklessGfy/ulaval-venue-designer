package app.domain.section;

import app.domain.Seat;
import app.domain.VitalSpace;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Vector;

public final class SeatedSection extends AbstractSection {
    @JsonProperty
    private final VitalSpace vitalSpace;
    @JsonProperty
    private final Vector<Seat> seats;

    SeatedSection(String name, int elevation,Shape shape, VitalSpace vitalSpace) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
        this.seats = new Vector<>();
    }

    @JsonCreator
    SeatedSection(@JsonProperty("name") String name, @JsonProperty("elevation") int elevation, @JsonProperty("shape") Shape shape, @JsonProperty("vitalSpace") VitalSpace vitalSpace, @JsonProperty("seats") Vector<Seat> seats) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
        this.seats = seats;
    }

    public static SeatedSection create(int x, int y, int column, int row, VitalSpace vitalSpace) {
        Objects.requireNonNull(vitalSpace);

        Vector<Point> points = new Vector<>();
        points.add(new Point(x, y));
        points.add(new Point(x + (column * vitalSpace.getWidth()), y));
        points.add(new Point(x + (column * vitalSpace.getWidth()), y + (row * vitalSpace.getHeight())));
        points.add(new Point(x, y + (row * vitalSpace.getHeight())));
        Rectangle rectangle = new Rectangle(points, new int[3]);

        SeatedSection section = new SeatedSection(null, 0, rectangle, vitalSpace);

        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                section.seats.add(new Seat(i, j, vitalSpace, points.elementAt(0)));
            }
        }

        return section;
    }

    @Override
    public  void move(int x, int y, Point offset) {
        Shape shape = getShape();
        for (Seat seat : seats){
            Point sectionCenter = shape.computeCentroid();
            Point seatCenter = seat.getShape().computeCentroid();
            int dx = sectionCenter.x - seatCenter.x;
            int dy = sectionCenter.y - seatCenter.y;
            seat.move(x + dx,y+dy, offset);
        }
        shape.move(x, y, offset);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        getShape().accept(g, painter);
        for (Seat seat : seats) {
            seat.getShape().accept(g, painter);
        }
    }
}
