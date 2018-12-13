package app.domain.section;

import app.domain.Collider;
import app.domain.Seat;
import app.domain.selection.SelectionVisitor;
import app.domain.Stage;
import app.domain.VitalSpace;
import app.domain.shape.*;
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

    public static SeatedSection create(double x, double y, int columns, int rows, VitalSpace vitalSpace, Stage stage) {
        Objects.requireNonNull(vitalSpace);
        Point stageCenter = stage.getShape().computeCentroid();
        double dx = stageCenter.x-x;
        double dy = stageCenter.y-y;
        double d = Math.sqrt(dx*dx+dy*dy);
        dx /= d;
        dy /= d;
        Point perpPoint = new Point(x-dy*columns*vitalSpace.getWidth()/2,y+dx*columns*vitalSpace.getWidth()/2);

        double theta=thetaCalc(perpPoint,stageCenter);
        int[] color = {63,63,76,255};
        Rectangle rectangle = Rectangle.create(x, y,columns*vitalSpace.getWidth(),rows*vitalSpace.getHeight(), color ,theta);


        SeatedSection section = new SeatedSection(null, 0, rectangle, vitalSpace);
        section.theta = theta;
        section.seats = new Seat[rows][columns];
        section.setElevation(0.0);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                section.seats[i][j] = new Seat(i, j, vitalSpace, new Point(x,y),theta);
            }
        }

        return section;
    }

    @Override
    public void move(double x, double y) {
        move(x, y, new Point());
    }

    @Override
    public  void move(double x, double y, Point offset) {
        Point ref = getShape().computeCentroid();
        forEachSeats(seat -> {
            Point seatCenter = seat.getShape().computeCentroid();
            double dx = ref.x-seatCenter.x;
            double dy = ref.y-seatCenter.y;
            seat.move(x - dx,y-dy, offset);});

        getShape().move(x, y, offset);
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
        super.getShape().rotate(thetaRadian, getShape().computeCentroid());
        theta+=thetaRadian;
        forEachSeats( seat -> seat.rotate(thetaRadian,getShape().computeCentroid()));
    }

    @Override
    public void autoSetSeats(Stage stage, Collider collider){
        Point stageCenter =stage.getShape().computeCentroid();
        Shape tolerantShape = createTolerantShape(getShape(),1.01);
        Rectangle box = makeBox(tolerantShape,stageCenter);
        double alpha = thetaCalc(box.computeCentroid(),stageCenter);
        double boxWidth = Math.sqrt(Math.pow(box.getPoints().elementAt(0).x-box.getPoints().elementAt(1).x,2)+
                Math.pow(box.getPoints().elementAt(0).y-box.getPoints().elementAt(1).y,2));
        double boxHeight = Math.sqrt(Math.pow(box.getPoints().elementAt(2).x-box.getPoints().elementAt(1).x,2)+
                Math.pow(box.getPoints().elementAt(2).y-box.getPoints().elementAt(1).y,2));
        Vector<Vector<Seat>> tempSeats= new Vector<>();

        int i=0;
        Point p0 = box.getPoints().elementAt(0);
        Point iterV = new Point(p0);
        double dv=0;
        while (dv<=boxHeight-vitalSpace.getHeight()){
            boolean emptyRow = true;
            Point iterH = new Point(iterV);
            double dh= 0;
            while (dh<=boxWidth-vitalSpace.getWidth()){
                Seat tempSeat = new Seat(0, 0, vitalSpace, iterH,alpha);
                boolean contains = true;
                for (Point p:tempSeat.getShape().getPoints()){
                    if (!collider.hasCollide( p.x,p.y,tolerantShape)){contains=false;}
                }
                if (contains){
                    if(tempSeats.size()<=i){tempSeats.add(new Vector<>()); emptyRow=false;}
                    tempSeats.elementAt(i).add(tempSeat);
                    iterH.x+=vitalSpace.getWidth()*Math.sin(alpha);
                    iterH.y-=vitalSpace.getWidth()*Math.cos(alpha);

                } else {
                    iterH.x+=Math.sin(alpha);
                    iterH.y-=Math.cos(alpha);
                }
                dh = Math.sqrt(Math.pow(iterV.x-iterH.x,2)+Math.pow(iterV.y-iterH.y,2));
            }
            if (!emptyRow){
                i++;
                iterV.x+=vitalSpace.getHeight()*Math.cos(alpha);
                iterV.y+=vitalSpace.getHeight()*Math.sin(alpha);

            } else {
                iterV.x+=Math.cos(alpha);
                iterV.y+=Math.sin(alpha);
            }
            dv=Math.sqrt(Math.pow(iterV.x-p0.x,2)+Math.pow(iterV.y-p0.y,2));
        }

        int a=0;
        seats = new Seat[tempSeats.size()][];
        for(Vector<Seat> row: tempSeats){
            int b=0;
            int rowSize = row.size();
            seats[a]=new Seat[rowSize];
            for(Seat seat: row){
                seats[a][b]=seat;
                b++;
            }
            a++;
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
        double x = points.firstElement().x;
        double y = points.firstElement().y;
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

    private static Rectangle makeBox(Shape shape, Point stageCenter) {
        Point sectionCenter = shape.computeCentroid();
        double alpha = thetaCalc(sectionCenter,stageCenter);
        double dx = stageCenter.x-sectionCenter.x;
        double dy = stageCenter.y-sectionCenter.y;

        Point perpendicularPoint = new Point(sectionCenter.x-dy,sectionCenter.y+dx);
        double leftDist=0;
        double rightDist=0;
        double topDist=0;
        double bottomDist=0;
        for (Point point: shape.getPoints()){
            double hDist = distancePointLine(point, stageCenter, sectionCenter);
            double vDist = distancePointLine(point, sectionCenter, perpendicularPoint);
            leftDist = Math.max(leftDist,hDist);
            rightDist = Math.min(rightDist, hDist);
            topDist = Math.min(topDist, vDist);
            bottomDist = Math.max(bottomDist,vDist);
        }
        double p1x = topDist*Math.cos(alpha) - leftDist*Math.sin(alpha) + sectionCenter.x;
        double p1y = topDist*Math.sin(alpha) + leftDist*Math.cos(alpha) + sectionCenter.y;
        double p2x = topDist*Math.cos(alpha) - rightDist*Math.sin(alpha) + sectionCenter.x;
        double p2y = topDist*Math.sin(alpha) + rightDist*Math.cos(alpha) + sectionCenter.y;
        double p3x = bottomDist*Math.cos(alpha) - rightDist*Math.sin(alpha) + sectionCenter.x;
        double p3y = bottomDist*Math.sin(alpha) + rightDist*Math.cos(alpha) + sectionCenter.y;
        double p4x = bottomDist*Math.cos(alpha) - leftDist*Math.sin(alpha) + sectionCenter.x;
        double p4y = bottomDist*Math.sin(alpha) + leftDist*Math.cos(alpha) + sectionCenter.y;
        Vector<Point> points = new Vector<>();
        points.add(new Point(p1x,p1y));
        points.add(new Point(p2x,p2y));
        points.add(new Point(p3x,p3y));
        points.add(new Point(p4x,p4y));
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

    private static Shape createTolerantShape(Shape shape, double scaleFactor){
        Vector<Point> points = new Vector<>();
        Point gravityCenter = shape.computeCentroid();
        for (Point p: shape.getPoints()){
            double d = Math.sqrt(Math.pow(p.x-gravityCenter.x,2)+Math.pow(p.y-gravityCenter.y,2));
            double alpha = thetaCalc(p,gravityCenter);
            d *= scaleFactor;
            double px= gravityCenter.x + d*Math.cos(alpha);
            double py= gravityCenter.y + d*Math.sin(alpha);
            points.add(new Point(px,py));
        }
        Shape tolerantShape = new Polygon(points, new int[4]);
        return tolerantShape;
    }


}
