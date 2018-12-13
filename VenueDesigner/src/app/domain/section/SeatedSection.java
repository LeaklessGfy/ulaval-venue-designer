package app.domain.section;

import app.domain.Collider;
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
    //TODO:remove this attribute
    public Point stageCenter;
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
        double dx = stageCenter.x-x;
        double dy = stageCenter.y-y;
        double d = Math.sqrt(dx*dx+dy*dy);
        dx /= d;
        dy /= d;
        Point perpPoint = new Point((int)(x-dy*columns*vitalSpace.getWidth()/2),(int)(y+dx*columns*vitalSpace.getWidth()/2));

        double theta=thetaCalc(perpPoint,stageCenter);

        Rectangle rectangle = Rectangle.create(x, y,columns*vitalSpace.getWidth(),rows*vitalSpace.getHeight(), new int[4],theta);


        SeatedSection section = new SeatedSection(null, 0, rectangle, vitalSpace);
        section.theta = theta;
        section.seats = new Seat[rows][columns];
        section.stageCenter=stageCenter;

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

    @Override
    public void rotate(double thetaRadian){
        super.getShape().rotate(thetaRadian);
        theta+=thetaRadian;
        refresh();
    }

    @Override
    public void autoSetSeats(Stage stage, Collider collider){
        Point stageCenter =stage.getShape().computeCentroid();
        Rectangle box = makeBox(stageCenter);
        double alpha = thetaCalc(box.computeCentroid(),stageCenter);
        double boxWidth = Math.sqrt(Math.pow(box.getPoints().elementAt(0).x-box.getPoints().elementAt(1).x,2)+
                Math.pow(box.getPoints().elementAt(0).y-box.getPoints().elementAt(1).y,2));
        double boxHeight = Math.sqrt(Math.pow(box.getPoints().elementAt(2).x-box.getPoints().elementAt(1).x,2)+
                Math.pow(box.getPoints().elementAt(2).y-box.getPoints().elementAt(1).y,2));
        int maxRows = (int)Math.floor(boxHeight/(double)(vitalSpace.getHeight()));
        int maxColumns = (int)Math.floor(boxWidth/(double)(vitalSpace.getWidth()));
        Vector<Vector<Seat>> tempSeats= new Vector<>();
        int a=0;
        for (int i=0; i<maxRows;i++){
            boolean emptyRow = true;
            for (int j=0; j<maxColumns; j++){
                Seat tempSeat = new Seat(i, j, vitalSpace, box.getPoints().elementAt(0),alpha);
                boolean contains = true;
                for (Point p:tempSeat.getShape().getPoints()){
                    if (!collider.hasCollide(p.x,p.y,getShape())){contains=false;}
                }
                if (contains){
                    if(tempSeats.size()<=a){tempSeats.add(new Vector<>()); emptyRow=false;}
                    tempSeats.elementAt(a).add(tempSeat);
                }
            }
            if (!emptyRow){a++;}
        }
        int i=0;
        seats = new Seat[tempSeats.size()][];
        for(Vector<Seat> row: tempSeats){
            int j=0;
            int rowSize = row.size();
            seats[i]=new Seat[rowSize];
            for(Seat seat: row){
                seats[i][j]=seat;
                j++;
            }
            i++;
        }

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

    public Rectangle makeBox(Point stageCenter) {
        Point sectionCenter = getShape().computeCentroid();
        double alpha = thetaCalc(sectionCenter,stageCenter);
        double dx = stageCenter.x-sectionCenter.x;
        double dy = stageCenter.y-sectionCenter.y;
        double d = Math.sqrt(dx*dx+dy*dy);
        dx /= d;
        dy /= d;
        Point perpendicularPoint = new Point((int )(sectionCenter.x-5*dy),(int)(sectionCenter.y+5*dx));
        double leftDist=0;
        double rightDist=0;
        double topDist=0;
        double bottomDist=0;
        for (Point point: getShape().getPoints()){
            double hDist = distancePointLine(point, stageCenter, sectionCenter);
            double vDist = distancePointLine(point, sectionCenter, perpendicularPoint);
            leftDist = Math.max(leftDist,hDist);
            rightDist = Math.min(rightDist, hDist);
            topDist = Math.min(topDist, vDist);
            bottomDist = Math.max(bottomDist,vDist);
        }
        int corrector = 0;
        double p1x = (topDist-corrector)*Math.cos(alpha) - (leftDist+corrector)*Math.sin(alpha) + sectionCenter.x;
        double p1y = (topDist-corrector)*Math.sin(alpha) + (leftDist+corrector)*Math.cos(alpha) + sectionCenter.y;
        double p2x = (topDist-corrector)*Math.cos(alpha) - (rightDist-corrector)*Math.sin(alpha) + sectionCenter.x;
        double p2y = (topDist-corrector)*Math.sin(alpha) + (rightDist-corrector)*Math.cos(alpha) + sectionCenter.y;
        double p3x = (bottomDist+corrector)*Math.cos(alpha) - (rightDist-corrector)*Math.sin(alpha) + sectionCenter.x;
        double p3y = (bottomDist+corrector)*Math.sin(alpha) + (rightDist-corrector)*Math.cos(alpha) + sectionCenter.y;
        double p4x = (bottomDist+corrector)*Math.cos(alpha) - (leftDist+corrector)*Math.sin(alpha) + sectionCenter.x;
        double p4y = (bottomDist+corrector)*Math.sin(alpha) + (leftDist+corrector)*Math.cos(alpha) + sectionCenter.y;
        Vector<Point> points = new Vector<>();
        points.add(new Point((int)p1x,(int)p1y));
        points.add(new Point((int)p2x,(int)p2y));
        points.add(new Point((int)p3x,(int)p3y));
        points.add(new Point((int)p4x,(int)p4y));
        Rectangle box = Rectangle.create(points, new int[4]);
        return box;
    }
    private static double distancePointLine(Point p0, Point p1, Point p2){
        return ((p2.y-p1.y)*p0.x-(p2.x-p1.x)*p0.y+p2.x*p1.y-p2.y*p1.x)/Math.sqrt(Math.pow((p2.y-p1.y),2)+Math.pow((p2.x-p1.x),2));
    }

    private static double thetaCalc(Point p, Point stageCenter){
        double dx = p.x - stageCenter.x;
        double dy = p.y - stageCenter.y;
        double theta;
        if (dx>0 && dy>=0){
            theta = Math.atan(dy/dx);
        } else if (dx>0){
            theta = Math.atan(dy/dx) + 2 * Math.PI;
        } else if (dx < 0) {
            theta = Math.atan(dy/dx) + Math.PI;
        } else if (p.x == 0 && p.y>0){
            theta = Math.PI/2;
        } else {
            theta = 3*Math.PI/2;
        }
        return theta;
    }


}
