package app.gui;

import app.domain.*;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.shape.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Optional;

public final class RoomSettings extends JFrame {
    private JPanel panelMain;
    private JTextField roomWidthTextField;
    private JTextField roomHeightTextField;
    private JTextField vitalSpaceWidthTextField;
    private JTextField vitalSpaceHeightTextField;
    private JButton oKButton;
    private JButton cancelButton;

    RoomSettings(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        Optional<Room> room = controller.getRoom();

        if (event.getActionCommand().equals("New") || !room.isPresent()) {
            vitalSpaceWidthTextField.setText("10");
            vitalSpaceHeightTextField.setText("10");
        } else {
            Room r = room.get();
            roomWidthTextField.setText(Integer.toString(r.getWidth()));
            roomHeightTextField.setText(Integer.toString(r.getHeight()));
            vitalSpaceWidthTextField.setText(Integer.toString(r.getVitalSpace().getWidth()));
            vitalSpaceHeightTextField.setText(Integer.toString(r.getVitalSpace().getHeight()));
        }

        oKButton.addActionListener(e -> {
            if (validateForm()) {
                int roomWidth = Integer.parseInt(roomWidthTextField.getText());
                int roomHeight = Integer.parseInt(roomHeightTextField.getText());
                int vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
                int vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
                if (event.getActionCommand().equals("New") || !room.isPresent()) {
                    roomWidth = Integer.parseInt(roomWidthTextField.getText());
                    roomHeight = Integer.parseInt(roomHeightTextField.getText());
                    vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
                    vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
                    controller.createRoom(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
                    setVisible(false);
                    dispose();
                    ui.repaint();
                } else {
                    Room r = room.get();
                    if (validateDimensions(controller, roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight)) {
                        r.setDimensions(roomWidth, roomHeight);
                        r.getVitalSpace().setWidth(vitalSpaceWidth);
                        r.getVitalSpace().setHeight(vitalSpaceHeight);
                        for (Section section : r.getSections()) {
                            section.accept(new SelectionVisitor() {
                                @Override
                                public void visit(Stage stage) {
                                }

                                @Override
                                public void visit(SeatedSection section) {
                                    section.refresh();
                                }

                                @Override
                                public void visit(Seat seat) {
                                }
                            });
                        }
                        setVisible(false);
                        dispose();
                        ui.repaint();
                    }
                }
            }
        });

        cancelButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isNotInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private boolean validateDimensions(Controller controller, int roomWidth, int roomHeight, int vitalSpaceWidth, int vitalSpaceHeight) {
        Room r = controller.getRoom().get();
        VitalSpace vs = new VitalSpace(vitalSpaceWidth, vitalSpaceHeight);
        Room predict = new Room(roomWidth, roomHeight, vs);
        if (r.isStageSet()) {
            if (!predict.validShape(r.getStage().get().getShape(), new Point())) {
                JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            for (Section section : r.getSections()) {
                if (!predict.validShape(section.getShape(), new Point())) {
                    JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateForm() {
        if(
                roomWidthTextField.getText().isEmpty() ||
                roomHeightTextField.getText().isEmpty() ||
                vitalSpaceWidthTextField.getText().isEmpty() ||
                vitalSpaceHeightTextField.getText().isEmpty()
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (
                isNotInteger(roomWidthTextField.getText()) ||
                        isNotInteger(roomHeightTextField.getText()) ||
                        isNotInteger(vitalSpaceWidthTextField.getText()) ||
                        isNotInteger(vitalSpaceHeightTextField.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
