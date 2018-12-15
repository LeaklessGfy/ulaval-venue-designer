package app.gui;

import app.domain.*;
import app.domain.section.StandingSection;
import app.domain.selection.SelectionAdapter;
import app.domain.section.SeatedSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public final class MainWindow extends Frame implements Observer {
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
    private JButton leftRotateButton;
    private JButton rightRotateButton;
    private JButton irregularSeatedSectionButton;
    private JButton standingSectionButton;
    private JButton autoScalingButton;
    private JCheckBox autoSeatCheckBox;
    private JMenu file;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenu edition;
    private JMenuItem room;
    private JMenuItem offers;
    private JMenuItem grid;
    private SeatInfo seatInfo;
    private int cursorX;
    private int cursorY;


    private MainWindow(JFrame frame) {
        buttonTopPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.LIGHT_GRAY));
        tablePanel.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.LIGHT_GRAY));
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
                    autoSeatCheckBox.setSelected(controller.isAutoSelected());
                    regSeatedSection.setVisible(controller.getRoom().isStageSet());
                    standingSectionButton.setVisible(controller.getRoom().isStageSet());
                    irregularSeatedSectionButton.setVisible(controller.getRoom().isStageSet());
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                cursorX=e.getX();
                cursorY=e.getY();
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

        irregularSeatedSectionButton.setVisible(controller.getRoom().isStageSet());
        irregularSeatedSectionButton.addActionListener(e -> {
            toggleButton(irregularSeatedSectionButton, Mode.IrregularSeatedSection);
            tablePanel.setVisible(false);
        });

        standingSectionButton.setVisible(controller.getRoom().isStageSet());
        standingSectionButton.addActionListener(e -> {
            toggleButton(standingSectionButton, Mode.IrregularStandingSection);
            tablePanel.setVisible(false);
        });

        zoomIn.addActionListener( e -> {
            controller.zoom(0.1);
        });

        zoomOut.addActionListener( e -> {
            controller.zoom(-0.1);
        });

        autoScalingButton.addActionListener(e -> {
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
                    JFrame sectionEdition;
                    if (section.isRegular){
                        sectionEdition = new SectionEdition(controller, section, drawingPanel);
                    }else {
                        if (!controller.getRoom().getStage().isPresent()){
                            JOptionPane.showMessageDialog(null, "A stage is needed to use this feature.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        sectionEdition = new IrregularSectionEdition(controller,section,drawingPanel);
                    }
                    sectionEdition.setSize(300, 400);
                    sectionEdition.setVisible(true);
                }

                @Override
                public void visit(StandingSection section) {
                    JFrame sectionEdition = new StandingSectionEdition(section, drawingPanel);

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
            autoSeatCheckBox.setSelected(controller.isAutoSelected());
            regSeatedSection.setVisible(controller.getRoom().isStageSet());
            standingSectionButton.setVisible(controller.getRoom().isStageSet());
            irregularSeatedSectionButton.setVisible(controller.getRoom().isStageSet());
        });

        leftRotateButton.addActionListener(e -> {
            controller.rotateSelected(false);
        });

        rightRotateButton.addActionListener(e -> {
            controller.rotateSelected(true);
        });

        autoSeatCheckBox.addActionListener(e -> {
            controller.autoSetSeatSelected();
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
        controller.setObserver(this);
        painter = new GUIPainter(controller);
        drawingPanel = new DrawingPanel(painter);
        controller.setDrawingPanel(drawingPanel);
        seatInfo = new SeatInfo();
        seatInfo.setSize(200,200);
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
        irregularSeatedSectionButton.setBackground(UIManager.getColor("Button.background"));
        irregularSeatedSectionButton.setForeground(UIManager.getColor("Button.foreground"));
        standingSectionButton.setBackground(UIManager.getColor("Button.background"));
        standingSectionButton.setForeground(UIManager.getColor("Button.foreground"));

        if (isEnabled) {
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.setForeground(UIManager.getColor("Button.foreground"));
        }
    }

    @Override
    public void displaySeatInfo() {
        int x= drawingPanel.getLocation().x + (int )controller.getXCursor();
        int y= drawingPanel.getLocation().y + (int ) controller.getYCursor();
        seatInfo.update(controller);
        seatInfo.setLocation(x,y);
        seatInfo.setVisible(true);
    }

    @Override
    public  void hideSeatInfo(){
        seatInfo.setVisible(false);
    }
}

