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
    private JButton regSeatedSectionButton;
    private JButton standingSectionButton;

    private MainWindow() {
        tablePanel.setBackground(new Color(20, 38, 52));
        tablePanel.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.LIGHT_GRAY));
        rectangleBtn.setBackground(Color.DARK_GRAY);
        rectangleBtn.setFocusPainted(false);
        rectangleBtn.setForeground(Color.LIGHT_GRAY);
        polygoneBtn.setBackground(Color.DARK_GRAY);
        polygoneBtn.setFocusPainted(false);
        polygoneBtn.setForeground(Color.LIGHT_GRAY);
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

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

        // LISTENER ANTHONY
        regSeatedSectionButton.addActionListener(e ->{
            boolean isEnabled = controller.toggleMode(Mode.RegularSeatedSection);

            if(isEnabled){
                regSeatedSectionButton.setBackground(Color.BLUE);
                regSeatedSectionButton.setForeground(Color.WHITE);
            } else {
                regSeatedSectionButton.setBackground(Color.DARK_GRAY);
                regSeatedSectionButton.setForeground(Color.LIGHT_GRAY);
            }
        });
        // FIN DU LISTENER ANTHONY
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        file.add(newItem);
        file.add(openItem);
        file.add(saveItem);

        JMenu edition = new JMenu("Edition");
        JMenuItem dimensions = new JMenuItem("Dimensions");
        JMenuItem vitalSpace = new JMenuItem("Vital Space");
        JMenuItem offers = new JMenuItem("Offers");
        JMenuItem grid = new JMenuItem("Grid");
        edition.add(dimensions);
        edition.add(vitalSpace);
        edition.add(offers);
        edition.add(grid);

        menuBar.add(file);
        menuBar.add(edition);
        frame.setJMenuBar(menuBar);
    }

    private void createUIComponents() {
        String[] columnNames = {"Table de ",
                "test"};
        Object[][] data = {
                {"ceci", "est"},
                {"une", "table"},
                {"temporaire", "."}
        };
        propertyTable = new JTable(data, columnNames);
        controller = new Controller(new GUICollider());
        drawingPanel = new DrawingPanel(new GUIPainter(controller));
        controller.setDrawingPanel(drawingPanel);
    }
}

