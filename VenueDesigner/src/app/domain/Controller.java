package app.domain;

import app.gui.DrawingPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class Controller {
    private Vector<Forme> m_formes = new Vector<>();
    private String m_creationMode= new String();
    private DrawingPanel m_drawingPanel;
    private int[] m_cursor = {-1,-1};

    public String getCreationMode(){
        return m_creationMode;
    }
    public void setDrawingPanel(DrawingPanel p_DrawingPanel){
        m_drawingPanel = p_DrawingPanel;
    }
    public int getXCursor () {
        return  m_cursor[0];
    }
    public int getYCursor () {
        return  m_cursor[1];
    }
    public void mouseClicked(MouseEvent e) {
        if(m_formes.isEmpty()){
            if (m_creationMode == "rectangle"){
                m_formes.add(new Rectangle());
                m_formes.lastElement().addPoint(new Point(e.getX(), e.getY()));
            }
            else if (m_creationMode == "polygone"){
                m_formes.add(new Forme());
                m_formes.lastElement().addPoint(new Point(e.getX(), e.getY()));
            }
        }
        else if (m_formes.lastElement().isValid()){
            if (m_creationMode == "rectangle"){
                for (Forme forme : m_formes) {
                    forme.setSelected(false);
                }
                m_formes.add(new Rectangle());
                m_formes.lastElement().addPoint(new Point(e.getX(), e.getY()));
            }
            else if(m_creationMode == "polygone"){
                for (Forme forme : m_formes) {
                    forme.setSelected(false);
                }
                m_formes.add(new Forme());
                m_formes.lastElement().addPoint(new Point(e.getX(), e.getY()));
            }
            else {
                for (Forme forme : m_formes) {
                    int nPoints=forme.getPoints().size();
                    int[] xCoords = new int[nPoints];
                    int[] yCoords = new int[nPoints];
                    int i=0;
                    for(Point point:forme.getPoints()){
                        xCoords[i]=point.x;
                        yCoords[i]=point.y;
                        i++;
                    }
                    Shape shape = new Polygon(xCoords, yCoords, nPoints);
                    if (shape.contains(e.getPoint())) {
                        forme.setSelected(true);
                    }
                    else {
                        forme.setSelected(false);
                    }
                }
                m_drawingPanel.repaint();
            }
        }
        else if (!m_formes.lastElement().isValid()) {
            m_formes.lastElement().addPoint(new Point(e.getX(), e.getY()));
            m_drawingPanel.repaint();
        }
    }

    public void mouseMoved(MouseEvent e){
        if (!m_formes.isEmpty()){
            if (!m_formes.lastElement().isValid()){
                m_cursor[0] = e.getX();
                m_cursor[1] = e.getY();
                m_drawingPanel.repaint();
            }
        }
    }
    public boolean toggleMode(String p_mode) {
        if(m_creationMode == p_mode){
            m_creationMode = "";
            return false;
        }
        else {
            m_creationMode=p_mode;
            return true;
        }
    }

    public Vector<Forme> getForme(){
        return m_formes;
    }
}
