package app.domain.section;

import app.domain.Seat;
import app.domain.selection.SelectionVisitor;
import app.domain.Stage;
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
import java.util.function.Consumer;

public final class SeatedSection extends AbstractSection {
    @JsonProperty
    private VitalSpace vitalSpace;
    @JsonProperty
    private double theta;
    @JsonProperty
    private Seat[][] seats = new Seat[0][0];

    SeatedSection(String name, int elevation,Shape shape, VitalSpace vitalSpace) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
    }

    @JsonCreator
    SeatedSection(@JsonProperty("name") String name, @JsonProperty("elevation") int elevation, @JsonProperty("shape") Shape shape, @JsonProperty("vitalSpace") VitalSpace vitalSpace, @JsonProperty("seats") Seat[][] seats, @JsonProperty("theta") double theta) {
        super(name, elevation, shape);
        this.vitalSpace = vitalSpace;
        this.seats = seats;
        this.theta = theta;
    }

    public static SeatedSection create(int x, int y, int columns, int rows, VitalSpace vitalSpace, Stage stage) {
        Objects.requireNonNull(vitalSpace);
        Point stageCenter = stage.getShape().computeCentroid();
        double dx = x - stageCenter.x;
        double dy = y - stageCenter.y;
        double alpha = Math.atan((double)stage.getHeight()/(double)stage.getWidth());
        double theta;
        if (dx>0 && dy>=0){
            theta = Math.atan(dy/dx);
        } else if (dx>0){
            theta = Math.atan(dy/dx) + 2 * Math.PI;
        } else if (dx < 0) {
            theta = Math.atan(dy/dx) + Math.PI;
        } else if (x == 0 && y>0){
            theta = Math.PI/2;
        } else {
            theta = 3*Math.PI/2;
        }

        Rectangle rectangle = Rectangle.create(x, y,columns*vitalSpace.getWidth(),rows*vitalSpace.getHeight(), new int[4],theta);


        SeatedSection section = new SeatedSection(null, 0, rectangle, vitalSpace);
        section.theta = theta;
        section.seats = new Seat[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                section.seats[i][j] = new Seat(i, j, vitalSpace, new Point(x,y),theta);
            }
        }

        return section;
    }

    @Override
    public void move(int x, int y) {
        move(x, y, new Point());
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
    public void forEachSeats(Consumer<Seat> consumer) {
        for (Seat[] seatRow : seats) {
            for (Seat seat : seatRow) {
                consumer.accept(seat);
            }
        }
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    public int getRows() {
        return seats.length;
    }

    @JsonIgnore
    public int getColumns() {
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
        this.setShape(Rectangle.create(x, y,getColumns()*vitalSpace.getWidth(),getRows()*vitalSpace.getHeight(), new int[4],theta));
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                seats[i][j] = new Seat(i, j, vitalSpace, getShape().getPoints().get(0),theta);
            }
        }
    }

    public void setDimensions(int columns, int rows) {
        seats = new Seat[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new Seat(i, j, vitalSpace, getShape().getPoints().get(0),theta);
            }
        }
        refresh();
    }

    public double getTheta(){
        return theta;
    }
}
