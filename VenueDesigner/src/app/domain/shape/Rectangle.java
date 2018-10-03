package app.domain.shape;

public class Rectangle extends Shape {
    public void addPoint(Point p_point){
        if (m_points.isEmpty()){
            m_points.add(p_point);
        } else {
            m_points.add(new Point(p_point.x, super.getPoints().firstElement().y));
            m_points.add(p_point);
            m_points.add(new Point(super.getPoints().firstElement().x, p_point.y));
            super.setValid(true);
        }
    }
}
