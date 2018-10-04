package app.gui;

import java.awt.*;
import java.util.Objects;
import javax.swing.BorderFactory;

import app.domain.Controller;
import app.domain.UIPanel;

public final class DrawingPanel extends javax.swing.JPanel implements UIPanel {
    private final Controller controller;
    private final GUIPainter painter;

    DrawingPanel(Controller p_controller) {
        this.controller = Objects.requireNonNull(p_controller).setDrawingPanel(this);
        this.painter = new GUIPainter(new GUIVisitor(controller));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
    }

    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.draw((Graphics2D) g, controller.getShapes());
    }
}
