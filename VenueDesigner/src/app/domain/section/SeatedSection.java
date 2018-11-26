package app.domain.section;

import app.domain.Seat;
import app.domain.SelectionVisitor;
import app.domain.VitalSpace;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Vector;

public final class SeatedSection extends AbstractSection {
    @JsonProperty
    private VitalSpace vitalSpace;
    @JsonProperty
    private Seat[][] seats = new Seat[0][0];

    SeatedSection(String name, int elevation,Shape shape, VitalSpace vitalSpace) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
    }

    @JsonCreator
    SeatedSection(@JsonProperty("name") String name, @JsonProperty("elevation") int elevation, @JsonProperty("shape") Shape shape, @JsonProperty("vitalSpace") VitalSpace vitalSpace, @JsonProperty("seats") Seat[][] seats) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
        this.seats = seats;
    }

    public static SeatedSection create(int x, int y, int columns, int rows, VitalSpace vitalSpace) {
        Objects.requireNonNull(vitalSpace);

        Vector<Point> points = new Vector<>();
        points.add(new Point(x, y));
        points.add(new Point(x + (columns * vitalSpace.getWidth()), y));
        points.add(new Point(x + (columns * vitalSpace.getWidth()), y + (rows * vitalSpace.getHeight())));
        points.add(new Point(x, y + (rows * vitalSpace.getHeight())));
        Rectangle rectangle = new Rectangle(points, new int[4]);

        SeatedSection section = new SeatedSection(null, 0, rectangle, vitalSpace);
        section.seats = new Seat[columns][rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                section.seats[i][j] = new Seat(i, j, vitalSpace, points.get(0));
            }
        }

        return section;
    }

    @Override
    public  void move(int x, int y, Point offset) {
        Shape shape = getShape();
        for (Seat[] seatRow : seats) {
            for (Seat seat : seatRow) {
                Point sectionCenter = shape.computeCentroid();
                Point seatCenter = seat.getShape().computeCentroid();
                int dx = seatCenter.x - sectionCenter.x;
                int dy = seatCenter.y - sectionCenter.y;
                seat.move(x + dx,y+dy, offset);
            }
        }
        shape.move(x, y, offset);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }

    @Override
    public Seat[][] getSeats() {
        return seats;
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    public int getColumns() {
        return seats.length;
    }

    @JsonIgnore
    public int getRows() {
        if (seats.length < 1) {
            return 0;
        }
        return seats[0].length;
    }

    public VitalSpace getVitalSpace() {
        return vitalSpace;
    }

    public void setVitalSpace(VitalSpace vitalSpace) {
        this.vitalSpace = vitalSpace;
        refresh();
    }

    public void refresh() {
        Vector<Point> points = getShape().getPoints();
        int x = points.firstElement().x;
        int y = points.firstElement().y;
        points.get(1).x = x + (getColumns() * vitalSpace.getWidth());
        points.get(2).x = x + (getColumns() * vitalSpace.getWidth());
        points.get(2).y = y + (getRows() * vitalSpace.getHeight());
        points.get(3).y = y + (getRows() * vitalSpace.getHeight());
        for (int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                seats[i][j] = new Seat(i, j, vitalSpace, getShape().getPoints().get(0));
            }
        }
    }

    public void setDimensions(int columns, int rows) {
        seats = new Seat[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                seats[i][j] = new Seat(i, j, vitalSpace, getShape().getPoints().get(0));
            }
        }
        refresh();
    }
}
