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
    private JButton regSeatedSection;
    private JButton standingSection;
    private JButton regSeatedSection2;
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
        tablePanel.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.LIGHT_GRAY));
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getMode() == Mode.RegularSeatedSection2) {
                    JFrame sectionSettings = new SectionSettings(controller, drawingPanel, e.getX(), e.getY());
                    sectionSettings.setSize(300,400);
                    sectionSettings.setVisible(true);
                } else {
                    controller.mouseClicked(e.getX(), e.getY());
                }
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
            toggleButton(stage, Mode.Stage);
        });

        regSeatedSection.addActionListener(e -> {
            toggleButton(regSeatedSection, Mode.RegularSeatedSection);
        });

        regSeatedSection2.addActionListener(e -> {
            toggleButton(regSeatedSection2, Mode.RegularSeatedSection2);
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
            JFrame roomSettings = new RoomSettings(controller, drawingPanel, e);
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
            if (controller.getRoom().isPresent()) {
                JFrame roomSettings = new RoomSettings(controller, drawingPanel, e);
                roomSettings.setSize(300, 400);
                roomSettings.setVisible(true);
            } else {
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
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
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
        drawingPanel = new DrawingPanel(new GUIPainter(controller, drawingPanel));
        controller.setDrawingPanel(drawingPanel);
    }

    private void toggleButton(JButton btn, Mode mode) {
        boolean isEnabled = controller.toggleMode(mode);

        stage.setBackground(UIManager.getColor("Button.background"));
        stage.setForeground(UIManager.getColor("Button.foreground"));
        regSeatedSection.setBackground(UIManager.getColor("Button.background"));
        regSeatedSection.setForeground(UIManager.getColor("Button.foreground"));
        regSeatedSection2.setBackground(UIManager.getColor("Button.background"));
        regSeatedSection2.setForeground(UIManager.getColor("Button.foreground"));

        if (isEnabled) {
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.setForeground(UIManager.getColor("Button.foreground"));
        }
    }
}

