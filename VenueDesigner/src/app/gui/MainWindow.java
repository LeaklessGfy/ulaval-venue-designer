package app.gui;

import app.domain.Controller;
import app.domain.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class MainWindow extends Frame {
    private Controller controller;

    private JPanel panelMain;
    private JPanel buttonTopPanel;
    private JScrollPane mainScrollPane;
    private DrawingPanel drawingPanel;
    private JButton rectangleBtn;
    private JButton polygoneBtn;
    private JTable propertyTable;
    private JPanel tablePanel;
    private JButton stage;
    private JButton seatedSectionButton;
    private JButton standingSectionButton;
    private JMenu file;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenu edition;
    private JMenuItem room;
    private JMenuItem offers;
    private JMenuItem grid;

    private MainWindow(JFrame frame) {
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
                controller.mouseClicked(e.getX(), e.getY());
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                controller.mouseMoved(e.getX(), e.getY());
            }
        });

        rectangleBtn.addActionListener(e -> {
            boolean isEnabled = controller.toggleMode(Mode.Rectangle);

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
            boolean isEnabled = controller.toggleMode(Mode.Polygon);

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

        stage.addActionListener(e -> {
            controller.toggleMode(Mode.Stage);
        });

        JMenuBar menuBar = new JMenuBar();

        file = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        file.add(newItem);
        file.add(openItem);
        file.add(saveItem);

        newItem.addActionListener( e -> {
            JFrame roomSettings = new RoomSettings(this.controller, e);
            roomSettings.setSize(300,400);
            roomSettings.setVisible(true);
        });

        edition = new JMenu("Edition");
        room = new JMenuItem("Room");
        offers = new JMenuItem("Offers");
        grid = new JMenuItem("Grid");
        edition.add(room);
        edition.add(offers);
        edition.add(grid);

        room.addActionListener( e -> {
            if (this.controller.getRoom() != null) {
                JFrame roomSettings = new RoomSettings(this.controller, e);
                roomSettings.setSize(300, 400);
                roomSettings.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(null, "No room defined", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        menuBar.add(file);
        menuBar.add(edition);
        frame.setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow(frame).panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        controller = new Controller(new GUICollider());
        drawingPanel = new DrawingPanel(new GUIPainter(controller));
        controller.setDrawingPanel(drawingPanel);
    }
}

