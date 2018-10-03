package app.gui;

import app.domain.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends Panel {
    private JPanel panelMain;
    private JPanel buttonTopPanel;
    private JScrollPane mainScrollPane;
    private DrawingPanel drawingPanel;
    private JButton rectangleBtn;
    private JButton polygoneBtn;
    private JTable propertyTable;
    private JPanel tablePanel;
    private Controller controller;

    public MainWindow() {
        tablePanel.setBackground(new Color(20, 38, 52));
        rectangleBtn.setBackground(Color.DARK_GRAY);
        rectangleBtn.setFocusPainted(false);
        rectangleBtn.setForeground(Color.LIGHT_GRAY);
        polygoneBtn.setBackground(Color.DARK_GRAY);
        polygoneBtn.setFocusPainted(false);
        polygoneBtn.setForeground(Color.LIGHT_GRAY);
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        drawingPanel.setBackground(new Color(20, 38, 52));

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.mouseClicked(e);
            }
        });
        rectangleBtn.addActionListener(e -> {
            boolean isEnabled = controller.toggleMode("rectangle");
            if (isEnabled) {
                rectangleBtn.setBackground(Color.BLUE);
                rectangleBtn.setForeground(Color.WHITE);
                polygoneBtn.setBackground(Color.DARK_GRAY);
                polygoneBtn.setForeground(Color.LIGHT_GRAY);
            } else {
                rectangleBtn.setBackground(Color.DARK_GRAY);
                rectangleBtn.setForeground(Color.LIGHT_GRAY);
            }
        });
        polygoneBtn.addActionListener(e -> {
            boolean isEnabled = controller.toggleMode("polygone");
            if (isEnabled) {
                polygoneBtn.setBackground(Color.BLUE);
                polygoneBtn.setForeground(Color.WHITE);
                rectangleBtn.setBackground(Color.DARK_GRAY);
                rectangleBtn.setForeground(Color.LIGHT_GRAY);
            } else {
                polygoneBtn.setBackground(Color.DARK_GRAY);
                polygoneBtn.setForeground(Color.LIGHT_GRAY);
            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                controller.mouseMoved(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        controller = new Controller();
        drawingPanel = new DrawingPanel(controller);
    }

}

