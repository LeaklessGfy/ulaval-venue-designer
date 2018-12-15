package app.gui;

import app.domain.Controller;
import app.domain.Mode;
import app.domain.Seat;
import app.domain.SeatSection;
import app.domain.selection.SelectionAdapter;
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
    private JButton zoomIn;
    private JButton zoomOut;
    private JButton editButton;
    private JButton removeButton;
    private JButton autoScaling;
    private JMenu file;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenu edition;
    private JMenuItem room;
    private JMenuItem offers;
    private JMenuItem grid;


    private MainWindow(JFrame frame) {
        buttonTopPanel.setBackground(Color.LIGHT_GRAY);
        tablePanel.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.LIGHT_GRAY));
        tablePanel.setVisible(false);
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getMode() == Mode.RegularSeatedSection2) {
                    JFrame sectionSettings = new SectionSettings(
                            controller,
                            drawingPanel,
                            (int)(e.getX() / controller.getScale()),
                            (int)(e.getY() / controller.getScale()),
                            () -> reset()
                    );
                    sectionSettings.setSize(300,400);
                    sectionSettings.setVisible(true);
                } else {
                    controller.mouseClicked(e.getX(), e.getY());
                    reset();
                    tablePanel.setVisible(controller.getMode() == Mode.Selection);
                    regSeatedSection.setVisible(controller.getRoom().isStageSet());
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

        drawingPanel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                controller.mouseWheelMoved(e.getPreciseWheelRotation());
            }
        });

        stage.addActionListener(e -> {
            toggleButton(stage, Mode.Stage);
            tablePanel.setVisible(false);
        });

        regSeatedSection.setVisible(controller.getRoom().isStageSet());
        regSeatedSection.addActionListener(e -> {
            toggleButton(regSeatedSection, Mode.RegularSeatedSection2);
            tablePanel.setVisible(false);
        });

        zoomIn.addActionListener( e -> {
            controller.zoom(0.1);
        });

        zoomOut.addActionListener( e -> {
            controller.zoom(-0.1);
        });

        autoScaling.addActionListener(e -> {
            controller.autoScaling(drawingPanel.getWidth(), drawingPanel.getHeight());
        });

        editButton.addActionListener(e -> {
            controller.editSelected(new SelectionAdapter() {
                @Override
                public void visit(Stage stage) {
                    JFrame stageEdition = new StageEdition(controller, stage, drawingPanel);
                    stageEdition.setSize(300,400);
                    stageEdition.setVisible(true);
                }

                @Override
                public void visit(SeatedSection section) {
                    JFrame sectionEdition = new SectionEdition(controller, section, drawingPanel);
                    sectionEdition.setSize(300, 400);
                    sectionEdition.setVisible(true);
                }

                @Override
                public void visit(Seat seat) {
                    JFrame seatEdition = new SeatEdition(seat, drawingPanel);
                    seatEdition.setSize(300, 400);
                    seatEdition.setVisible(true);
                }

                @Override
                public void visit(SeatSection seatSection) {
                    JFrame seatSectionEdition = new SeatSectionEdition(seatSection, drawingPanel);
                    seatSectionEdition.setSize(300, 400);
                    seatSectionEdition.setVisible(true);
                }
            });
        });

        removeButton.addActionListener(e -> {
            controller.removeSelected();
            tablePanel.setVisible(controller.getMode()==Mode.Selection);
            regSeatedSection.setVisible(controller.getRoom().isStageSet());
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
            regSeatedSection.setVisible(controller.getRoom().isStageSet());
        });

        saveItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("room.json"));
            FileFilter filter = new FileNameExtensionFilter("JSON files", "json");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().toString();
                if (!filename.endsWith(".json")) {
                    filename += ".json";
                }
                controller.save(filename);
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
        /////////////////////////////
        // debut apparition offers
        offers.addActionListener(e->{
            if(controller.getRoom().isStageSet()){// pour commencer la scène est presente
                JFrame Offer = new OfferWindow(controller, drawingPanel, e);
                Offer.setSize(700, 500);// plus tard ce sera 500
                Offer.setVisible(true);
            }else{
                // boite de dialogue pour dire attention la scene existe pas encore
            }

        });

        // fin apparition offers
        /////////////////////////////
        room.addActionListener( e -> {
            JFrame roomSettings = new RoomSettings(controller, drawingPanel, e);
            roomSettings.setSize(300, 400);
            roomSettings.setVisible(true);
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

