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
    private JTable propertyTable;
    private JPanel tablePanel;
    private JButton stage;
    private JButton regSeatedSectionButton;
    private JButton standingSectionButton;
    private JButton regSeat2;

    private MainWindow() {
        tablePanel.setBackground(new Color(20, 38, 52));
        tablePanel.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.LIGHT_GRAY));
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
            @Override
            public void mouseDragged(MouseEvent e) {
                controller.mouseDragged(e.getX(), e.getY());
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
        regSeat2.addActionListener(e ->{
            boolean isEnabled = controller.toggleMode(Mode.RegularSeatedSection2);

            if(isEnabled){
                regSeat2.setBackground(Color.BLUE);
                regSeat2.setForeground(Color.WHITE);
            } else {
                regSeat2.setBackground(Color.DARK_GRAY);
                regSeat2.setForeground(Color.LIGHT_GRAY);
            }
        });
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

