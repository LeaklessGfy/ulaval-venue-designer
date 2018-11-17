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

    public Point GravityCenter() {
        float Gx = 0;
        float Gy = 0;
        float area = Area();
        for (Point p : points) {
            if (p == points.lastElement()) {
                Gx += (1 / (6 * area)) * (p.x + points.firstElement().x) * (p.x * points.firstElement().y - points.firstElement().x * p.y);
                Gy += (1 / (6 * area)) * (p.y + points.firstElement().y) * (p.x * points.firstElement().y - points.firstElement().x * p.y);
            } else {
                Gx += (1 / (6 * area)) * (p.x + points.get(points.indexOf(p) + 1).x) * (p.x * points.get(points.indexOf(p) + 1).y - points.get(points.indexOf(p) + 1).x * p.y);
                Gy += (1 / (6 * area)) * (p.y + points.get(points.indexOf(p) + 1).y) * (p.x * points.get(points.indexOf(p) + 1).y - points.get(points.indexOf(p) + 1).x * p.y);
            }
        }
        Point GravityPoint = new Point((int) Gx, (int) Gy);
        return GravityPoint;
    }

    public Vector<Point> Translation(int Xmouse, int Ymouse) {
        int dGx = Xmouse;
        int dGy = Ymouse;
        for (Point p : points) {
            p.set(p.x + dGx, p.y + dGy);
        }
        return points;
    }

    public Vector<Point> Rotation(float degree_angle){
        Point GravityPoint = GravityCenter();
        float theta_radian = degree_angle*0.0174533f; // transform from degrees to radians
        int Gx = GravityPoint.x;
        int Gy = GravityPoint.y;
        for(Point p : points){
            p.set((int)Math.round(((p.x-Gx)*Math.cos(theta_radian))-((p.y-Gy)*Math.sin(theta_radian)) + Gx),(int)Math.round((p.x-Gx)*Math.sin(theta_radian)+(p.y-Gy)*Math.cos(theta_radian)+ Gy));
        }
        return points;
    }
}
