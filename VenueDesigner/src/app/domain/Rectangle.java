package app.domain;

import java.awt.*;

public class Rectangle extends Forme{
    public void addPoint(Point p_point){
        if (super.getPoints().isEmpty()){
            m_points.add(p_point);
        } else {
            m_points.add(new Point(p_point.x, super.getPoints().firstElement().y));
            m_points.add(p_point);
            m_points.add(new Point(super.getPoints().firstElement().x, p_point.y));
            super.setValid(true);
        }
    }

}
