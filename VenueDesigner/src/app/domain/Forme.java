package app.domain;

import java.awt.*;
import java.util.Vector;

public class Forme {
    protected Vector<Point> m_points = new Vector<>();
    private  boolean m_valid = false;
    private boolean m_selected =false;

    private double calculDistance(Point p_p1, Point p_p2) {
        int x1 = p_p1.x;
        int x2 = p_p2.x;
        int y1 = p_p1.y;
        int y2 = p_p2.y;

        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public void addPoint(Point p_point){
        if (m_points.size() >= 3 && calculDistance(m_points.firstElement(), p_point)<=30)
        {
            m_valid = true;
        }
        else {
            m_points.add(p_point);
        }
    }
    public void setSelected(boolean bool){
        m_selected = bool;
    }
    public Boolean getSelected() {return  m_selected;}

    public void setValid(boolean bool) {
        m_valid=bool;
    }

    public Vector<Point> getPoints() {
        return m_points;
    }

    public boolean isValid(){
        return m_valid;
    }
}
