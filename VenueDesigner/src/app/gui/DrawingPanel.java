package app.gui;

import java.awt.*;
import javax.swing.BorderFactory;

import app.domain.Controller;
import app.domain.painter.Painter;

public class DrawingPanel extends javax.swing.JPanel
{
    private Painter m_painter;
    private final Controller m_controller;

    public DrawingPanel(final Controller p_controller){
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
        m_controller = p_controller;
        m_controller.setDrawingPanel(this);
        m_painter = new Painter(m_controller);
    }
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_painter != null){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            m_painter.paint(g2);
        }
    }

}
