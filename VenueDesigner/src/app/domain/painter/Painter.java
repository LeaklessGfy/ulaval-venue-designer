package app.domain.painter;

import app.domain.Controller;
import app.domain.Forme;

import java.awt.*;

public class Painter {
    private final Controller m_controller;

    public Painter(final Controller p_controller) {
        m_controller = p_controller;
    }

    public void paint(Graphics2D g){
        for(Forme forme: m_controller.getForme()) {
            this.paintForme(g, forme);
        }
    }
    public void paintForme(Graphics2D g, Forme p_forme){
        int nPoints=p_forme.getPoints().size();
        int[] xCoords = new int[nPoints];
        int[] yCoords = new int[nPoints];
        int i=0;
        for(Point point:p_forme.getPoints()){
            xCoords[i]=point.x;
            yCoords[i]=point.y;
            i++;
        }
        g.setStroke(new BasicStroke(2));
        if (p_forme.isValid()) {
            Shape shape = new Polygon(xCoords, yCoords, nPoints);
            if (!p_forme.getSelected()) {
                g.setColor(Color.lightGray);
            } else {
                g.setColor(Color.GREEN);
            }
            g.draw(shape);
            g.setColor(Color.darkGray);
            g.fill(shape);
        } else {
            g.setColor(Color.lightGray);
            g.drawPolyline(xCoords,yCoords,nPoints);
            if (m_controller.getCreationMode() == "polygone"){
                g.drawLine(p_forme.getPoints().lastElement().x, p_forme.getPoints().lastElement().y,
                        m_controller.getXCursor(), m_controller.getYCursor());
            }
            else if (m_controller.getCreationMode() == "rectangle"){
                g.drawLine(p_forme.getPoints().lastElement().x, p_forme.getPoints().lastElement().y,
                        m_controller.getXCursor(), p_forme.getPoints().lastElement().y);
                g.drawLine(p_forme.getPoints().lastElement().x, p_forme.getPoints().lastElement().y,
                        p_forme.getPoints().lastElement().x, m_controller.getYCursor());
                g.drawLine(m_controller.getXCursor(), p_forme.getPoints().lastElement().y,
                        m_controller.getXCursor(), m_controller.getYCursor());
                g.drawLine(p_forme.getPoints().lastElement().x, m_controller.getYCursor(),
                        m_controller.getXCursor(), m_controller.getYCursor());
            }

        }
    }
}
