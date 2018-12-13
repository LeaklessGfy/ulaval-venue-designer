package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;
import java.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "Rectangle")
})
abstract class AbstractShape implements Shape {
    private final Vector<Point> points;
    private final int[] color;
    private boolean selected;

    AbstractShape(Vector<Point> points, int[] color) {
        this.points = Objects.requireNonNull(points);
        this.color = Objects.requireNonNull(color);
        this.selected = false;
    }

    AbstractShape(AbstractShape shape) {
        this.points = new Vector<>();
        for (Point p : shape.points) {
            points.add(new Point(p));
        }
        this.color = shape.color;
        this.selected = shape.selected;
    }

    @Override
    public void setSelected(boolean bool) {
        selected = bool;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public Vector<Point> getPoints() {
        return points;
    }

    @Override
    public int[] getColor() {
        return color;
    }

    public float Area(){ // Polygon Area Calculation
        float area = 0;
        for(Point p: points){
            if(p == points.lastElement()){
                area += 0.5*(p.x)*(points.firstElement().y)-(points.firstElement().x)*(p.y);
            } else{
                area += 0.5*(p.x) * (points.get(points.indexOf(p)+1).y) - (points.get(points.indexOf(p)+1).y)*(p.y);
            }
        }
        return area;
    }

    @Override
    public Point computeCentroid() {
        double cx =0.0;
        double cy =0.0;
        double signedArea = 0.0;
        double x0; // Current vertex X
        double y0; // Current vertex Y
        double x1; // Next vertex X
        double y1; // Next vertex Y
        double a;  // Partial signed area

        // For all vertices
        for (int i=0; i<points.size(); ++i)
        {
            x0 = points.elementAt(i).x;
            y0 = points.elementAt(i).y;
            x1 = points.elementAt((i+1)%points.size()).x;
            y1 = points.elementAt((i+1)%points.size()).y;
            a = x0*y1 - x1*y0;
            signedArea += a;
            cx += (x0 + x1)*a;
            cy += (y0 + y1)*a;
        }

        signedArea *= 0.5;
        cx /= (6.0*signedArea);
        cy /= (6.0*signedArea);

        return new Point((int)Math.round(cx),(int)Math.round(cy));
    }

    @Override
    public void move(int x, int y) {
        move(x, y, new Point());
    }

    @Override
    public void move(int x, int y, Point offset) {
        Point centroid = this.computeCentroid();
        for (Point p : points) {
            int dx = p.x - centroid.x;
            int dy = p.y - centroid.y;
            p.set((x+dx) - offset.x, (y+dy) - offset.y);
        }
    }

    @Override
    public void rotate(double thetaRadian){
        Point gravityPoint = computeCentroid();
        int Gx = gravityPoint.x;
        int Gy = gravityPoint.y;
        for(Point p : points){
            p.set((int)(((p.x-Gx)*Math.cos(thetaRadian))-((p.y-Gy)*Math.sin(thetaRadian)) + Gx),(int)((p.x-Gx)*Math.sin(thetaRadian)+(p.y-Gy)*Math.cos(thetaRadian)+ Gy));
        }
    }

    @Override
    public Shape clone() {
        return null;
    }

}
