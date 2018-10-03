package app.gui;

import java.awt.*;
import java.util.Objects;
import javax.swing.BorderFactory;

import app.domain.Controller;
import app.domain.UIPanel;
import app.domain.painter.Painter;

public class DrawingPanel extends javax.swing.JPanel implements UIPanel {
    private final Painter m_painter;

    public DrawingPanel(Controller p_controller) {
        Objects.requireNonNull(p_controller).setDrawingPanel(this);
        m_painter = new Painter(p_controller);

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
    }

    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        m_painter.paint((Graphics2D) g);
    }
}
