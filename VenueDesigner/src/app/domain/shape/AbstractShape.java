package app.domain.shape;

import java.util.Objects;
import java.util.Vector;

abstract class AbstractShape implements Shape {
    private final Vector<Point> points;
    private final int[] color;
    private boolean selected;

    AbstractShape(Vector<Point> points, int[] color) {
        this.points = Objects.requireNonNull(points);
        this.color = Objects.requireNonNull(color);
        this.selected = false;
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

        return new Point((int) cx,(int)cy);
    }

    @Override
    public void move(int x, int y) {
        Point centroid = this.computeCentroid();
        for (Point p : points) {
            int dx = centroid.x - p.x;
            int dy = centroid.y - p.y;
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
