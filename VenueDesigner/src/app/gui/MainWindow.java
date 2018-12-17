package app.gui;

import app.domain.*;
import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.section.StandingSection;
import app.domain.selection.SelectionAdapter;
import app.domain.section.SeatedSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import javax.imageio.*;
import java.awt.image.*;

public final class MainWindow extends Frame implements Observer {
    private Controller controller;

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
    private JButton undo;
    private JButton redo;
    private JMenu file;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exportImage;
    private JMenu edition;
    private JMenuItem room;
    private JMenuItem offers;
    private JMenuItem grid;
    private JMenuItem prices;
    private SeatInfo seatInfo;

    private MainWindow(JFrame frame) {
        buttonTopPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.LIGHT_GRAY));
        tablePanel.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.LIGHT_GRAY));
        tablePanel.setVisible(false);
        buttonTopPanel.setBackground(new Color(20, 38, 52));
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getMode() == Mode.RegularSeatedSection) {
                    new SectionSettings(
                            controller,
                            drawingPanel,
                            (int)(e.getX() / controller.getScale()),
                            (int)(e.getY() / controller.getScale()),
                            () -> reset()
                    );
                } else {
                    controller.mouseClicked(e.getX(), e.getY());
                    if (controller.getMode() == Mode.None) {
                        reset();
                    }
                    tablePanel.setVisible(controller.getMode() == Mode.Selection);
                    autoSeatCheckBox.setSelected(controller.isAutoSelected());
                    regSeatedSection.setVisible(controller.getRoom().isStageSet());
                    standingSectionButton.setVisible(controller.getRoom().isStageSet());
                    irregularSeatedSectionButton.setVisible(controller.getRoom().isStageSet());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                controller.mouseReleased();
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
            toggleButton(regSeatedSection, Mode.RegularSeatedSection);
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
                    new StageEdition(controller, stage, drawingPanel);
                }

                @Override
                public void visit(SeatedSection section) {
                    if (section.isRegular){
                        new SectionEdition(controller, section, drawingPanel);
                    } else {
                        if (!controller.getRoom().getStage().isPresent()){
                            JOptionPane.showMessageDialog(null, "A stage is needed to use this feature.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        new IrregularSectionEdition(controller, section);
                    }
                }

                @Override
                public void visit(StandingSection section) {
                    new StandingSectionEdition(controller, section, drawingPanel);
                }

                @Override
                public void visit(Seat seat) {
                    new SeatEdition(controller, seat, drawingPanel);
                }

                @Override
                public void visit(SeatSection seatSection) {
                    new SeatSectionEdition(controller, seatSection, drawingPanel);
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
            controller.autoSetSeat();
        });

        undo.addActionListener(e -> controller.undo());
        undo.setEnabled(false);
        redo.addActionListener(e -> controller.redo());
        redo.setEnabled(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(frame, "Do you want to save before quit ?");
                if (answer == JOptionPane.YES_OPTION) {
                    save();
                    e.getWindow().dispose();
                    System.exit(0);
                } else if (answer == JOptionPane.NO_OPTION) {
                    e.getWindow().dispose();
                    System.exit(0);
                }
            }
        });

        initMenu(frame);
        controller.saveRoom();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow(frame).panelMain);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
    }

    private void createUIComponents() {
        controller = new Controller(new GUICollider());
        controller.setObserver(this);
        GUIPainter painter = new GUIPainter(controller);
        drawingPanel = new DrawingPanel(painter);
        controller.setDrawingPanel(drawingPanel);
        seatInfo = new SeatInfo();
    }

    private void initMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        file = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exportImage = new JMenuItem(("Export as image"));
        file.add(newItem);
        file.add(openItem);
        file.add(saveItem);
        file.add(exportImage);

        newItem.addActionListener(e -> new RoomSettings(controller, drawingPanel, e));
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
        saveItem.addActionListener(e -> save());
        exportImage.addActionListener(e -> saveImage());

        edition = new JMenu("Edition");
        room = new JMenuItem("Room");
        offers = new JMenuItem("Offers");
        grid = new JMenuItem("Grid");
        prices = new JMenuItem("Prices");
        edition.add(room);
        edition.add(offers);
        edition.add(grid);
        edition.add(prices);

        room.addActionListener(e -> new RoomSettings(controller, drawingPanel, e));
        offers.addActionListener(e-> {
            new OfferWindow(controller);
        });
        prices.addActionListener(e -> {
            if(controller.getRoom().getStage().isPresent()){
            new AutoPrices(controller, controller.getRoom().getSections(),controller.getRoom().getStage().get());}
            else {JOptionPane.showMessageDialog(null, "A stage is needed to use this feature.", "Error", JOptionPane.ERROR_MESSAGE);}
        });

        menuBar.add(file);
        menuBar.add(edition);
        frame.setJMenuBar(menuBar);
    }

    private void reset() {
        stage.setBackground(UIManager.getColor("Button.background"));
        stage.setForeground(UIManager.getColor("Button.foreground"));
        regSeatedSection.setBackground(UIManager.getColor("Button.background"));
        regSeatedSection.setForeground(UIManager.getColor("Button.foreground"));
        irregularSeatedSectionButton.setBackground(UIManager.getColor("Button.background"));
        irregularSeatedSectionButton.setForeground(UIManager.getColor("Button.foreground"));
        standingSectionButton.setBackground(UIManager.getColor("Button.background"));
        standingSectionButton.setForeground(UIManager.getColor("Button.foreground"));
    }

    private void toggleButton(JButton btn, Mode mode) {
        boolean isEnabled = controller.toggleMode(mode);
        reset();
        if (isEnabled) {
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(UIManager.getColor("Button.background"));
            btn.setForeground(UIManager.getColor("Button.foreground"));
        }
    }

    private void save() {
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
    }

    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("room"));
        fileChooser.setFileFilter(new FileNameExtensionFilter(".png", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".jpg", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".jpeg", "jpeg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".bmp", "bmp"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().toString();
            String extension = fileChooser.getFileFilter().getDescription();
            if (!filename.endsWith(extension)) {
                filename += extension;
            }
            app.domain.shape.Point controllerOffset = new app.domain.shape.Point(controller.getOffset());
            double controllerScale = controller.getScale();
            double tempScale = controller.prepareSave(drawingPanel.getWidth(), drawingPanel.getHeight());
            BufferedImage image = new BufferedImage((int)(controller.getRoom().getWidth() * tempScale), (int)(controller.getRoom().getHeight() * tempScale), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            drawingPanel.paint(g);
            g.dispose();
            controller.offsetScale(controllerOffset, controllerScale);

            try {
                ImageIO.write(image, "png", new File(filename));
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }
    }

    @Override
    public void onHover() {
        int x = drawingPanel.getLocation().x + (int)(controller.getXCursor()*controller.getScale())+7;
        int y = drawingPanel.getLocation().y + (int)(controller.getYCursor()*controller.getScale());
        seatInfo.update(controller.getHoveredSeat(), controller.getHoveredSection());
        seatInfo.setLocation(x, y);
        seatInfo.setVisible(true);
    }

    @Override
    public void onLeave(){
        seatInfo.setVisible(false);
    }

    @Override
    public void onUndoRedo() {
        undo.setEnabled(controller.canUndo());
        redo.setEnabled(controller.canRedo());
        regSeatedSection.setVisible(controller.getRoom().isStageSet());
        standingSectionButton.setVisible(controller.getRoom().isStageSet());
        irregularSeatedSectionButton.setVisible(controller.getRoom().isStageSet());
    }
}

