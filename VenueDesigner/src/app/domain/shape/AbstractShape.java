package app.domain.shape;

import java.util.Objects;
import java.util.Vector;

abstract class AbstractShape implements Shape {
    private final Vector<Point> points;
    private boolean selected;

    public AbstractShape(Vector<Point> points) {
        this.points = Objects.requireNonNull(points);
        this.selected = false;
    }

    public void setSelected(boolean bool) {
        selected = bool;
    }

    public boolean isSelected() {
        return selected;
    }

    public Vector<Point> getPoints() {
        return points;
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

    public Point computeCentroid() {
        Point centroid = new Point(0,0);
        double signedArea = 0.0;
        double x0 = 0.0; // Current vertex X
        double y0 = 0.0; // Current vertex Y
        double x1 = 0.0; // Next vertex X
        double y1 = 0.0; // Next vertex Y
        double a = 0.0;  // Partial signed area

        // For all vertices except last
        int i=0;
        for (i=0; i<points.size()-2; ++i)
        {
            x0 = points.elementAt(i).x;
            y0 = points.elementAt(i).y;
            x1 = points.elementAt(i).x;
            y1 = points.elementAt(i).y;
            a = x0*y1 - x1*y0;
            signedArea += a;
            centroid.x += (x0 + x1)*a;
            centroid.y += (y0 + y1)*a;
        }

        // Do last vertex separately to avoid performing an expensive
        // modulus operation in each iteration.
        x0 = points.elementAt(i).x;
        y0 = points.elementAt(i).y;
        x1 = points.elementAt(i).x;
        y1 = points.elementAt(i).y;
        a = x0*y1 - x1*y0;
        signedArea += a;
        centroid.x += (x0 + x1)*a;
        centroid.y += (y0 + y1)*a;

        signedArea *= 0.5;
        centroid.x /= (6.0*signedArea);
        centroid.y /= (6.0*signedArea);
        return centroid;
    }

    public void move(int x, int y) {
        Point gp = this.computeCentroid();
        for (Point p : points) {
            System.out.print(x);
            System.out.print("-");
            System.out.print(y);
            System.out.print(" --- ");
            int dx = gp.x - p.x;
            int dy = gp.y - p.y;
            System.out.print(x+dx);
            System.out.print("-");
            System.out.print(y+dy);
            System.out.print("\n");
            p.set(x+dx, y+dy);
        }
    }

    public Vector<Point> Rotation(float degree_angle){
        Point gravityPoint = computeCentroid();
        float theta_radian = degree_angle*0.0174533f; // transform from degrees to radians
        int Gx = gravityPoint.x;
        int Gy = gravityPoint.y;
        for(Point p : points){
            p.set((int)Math.round(((p.x-Gx)*Math.cos(theta_radian))-((p.y-Gy)*Math.sin(theta_radian)) + Gx),(int)Math.round((p.x-Gx)*Math.sin(theta_radian)+(p.y-Gy)*Math.cos(theta_radian)+ Gy));
        }
        return points;
    }
    
}
