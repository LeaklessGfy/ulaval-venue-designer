package app.gui;

import java.awt.*;
import java.util.Objects;
import javax.swing.BorderFactory;

import app.domain.UIPanel;

public final class DrawingPanel extends javax.swing.JPanel implements UIPanel {
    private final GUIPainter painter;

    DrawingPanel(GUIPainter painter) {
        this.painter = Objects.requireNonNull(painter);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
    }

    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, 600, 500);
        painter.draw((Graphics2D) g);
    }
}
