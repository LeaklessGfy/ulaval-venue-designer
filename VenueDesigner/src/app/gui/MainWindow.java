package app.gui;

import app.domain.Controller;
import app.domain.Mode;
import app.domain.SelectionVisitor;
import app.domain.Stage;
import app.domain.section.SeatedSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public final class MainWindow extends Frame {
    private Controller controller;
    private GUIPainter painter;

    private JPanel panelMain;
    private JPanel buttonTopPanel;
    private JScrollPane mainScrollPane;
    private DrawingPanel drawingPanel;
    private JPanel tablePanel;
    private JButton stage;
    private JButton regSeatedSection;
    private JButton editButton;
    private JButton removeButton;
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
        tablePanel.setVisible(false);
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getMode() == Mode.RegularSeatedSection2) {
                    JFrame sectionSettings = new SectionSettings(controller, drawingPanel, e.getX(), e.getY(), () -> reset());
                    sectionSettings.setSize(300,400);
                    sectionSettings.setVisible(true);
                } else {
                    controller.mouseClicked(e.getX(), e.getY());
                    reset();
                    tablePanel.setVisible(controller.getMode() == Mode.Selection);
                    regSeatedSection.setVisible(controller.getRoom().get().isStageSet());
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

        regSeatedSection.setVisible(controller.getRoom().get().isStageSet());
        regSeatedSection.addActionListener(e -> {
            toggleButton(regSeatedSection, Mode.RegularSeatedSection2);
        });

        editButton.addActionListener(e -> {
            controller.editSelected(new SelectionVisitor() {
                @Override
                public void visit(Stage stage) {
                    JFrame stageEdition = new StageEdition(stage, drawingPanel);
                    stageEdition.setSize(300,400);
                    stageEdition.setVisible(true);
                }

                @Override
                public void visit(SeatedSection section) {
                    JFrame sectionEdition = new SectionEdition(section, drawingPanel);
                    sectionEdition.setSize(300, 400);
                    sectionEdition.setVisible(true);
                }
            });
        });

        removeButton.addActionListener(e -> {
            controller.removeSelected();
            tablePanel.setVisible(controller.getMode()==Mode.Selection);
            regSeatedSection.setVisible(controller.getRoom().get().isStageSet());
        });
        
        JMenuBar menuBar = new JMenuBar();
        file = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");

        openItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("JSON files", "json");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().toString();
                this.controller.load(filename);
            }
        });

        saveItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("room.json"));
            FileFilter filter = new FileNameExtensionFilter("JSON files", "json");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                if (this.controller.getRoom().isPresent()) {
                    String filename = fileChooser.getSelectedFile().toString();
                    if (!filename.endsWith(".json")) {
                        filename += ".json";
                    }
                    this.controller.save(filename);
                }
            }
        });

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
        controller = new Controller(new GUICollider());
        painter = new GUIPainter(controller);
        drawingPanel = new DrawingPanel(painter);
        controller.setDrawingPanel(drawingPanel);
    }

    private void reset() {
        if (controller.getMode() == Mode.None) {
            stage.setBackground(UIManager.getColor("Button.background"));
            stage.setForeground(UIManager.getColor("Button.foreground"));
            regSeatedSection.setBackground(UIManager.getColor("Button.background"));
            regSeatedSection.setForeground(UIManager.getColor("Button.foreground"));
        }
    }

    private void toggleButton(JButton btn, Mode mode) {
        boolean isEnabled = controller.toggleMode(mode);

        stage.setBackground(UIManager.getColor("Button.background"));
        stage.setForeground(UIManager.getColor("Button.foreground"));
        regSeatedSection.setBackground(UIManager.getColor("Button.background"));
        regSeatedSection.setForeground(UIManager.getColor("Button.foreground"));

        if (isEnabled) {
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.setForeground(UIManager.getColor("Button.foreground"));
        }
    }
}

