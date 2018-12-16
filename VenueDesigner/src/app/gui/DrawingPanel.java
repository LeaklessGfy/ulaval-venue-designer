package app.gui;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

import app.domain.UIPanel;

final class DrawingPanel extends JPanel implements UIPanel {
    private final GUIPainter painter;

    DrawingPanel(GUIPainter painter) {
        this.painter = Objects.requireNonNull(painter);
    }

    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0,0,getWidth(),getHeight());
        painter.draw((Graphics2D) g);
    }
}
