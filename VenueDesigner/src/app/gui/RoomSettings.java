package app.gui;

import app.domain.*;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.selection.SelectionAdapter;
import app.domain.shape.Point;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Optional;

import static app.gui.GUIUtils.isNotInteger;

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
        Room room = controller.getRoom();

        roomWidthTextField.setText(Integer.toString(room.getWidth()));
        roomHeightTextField.setText(Integer.toString(room.getHeight()));
        vitalSpaceWidthTextField.setText(Integer.toString(room.getVitalSpace().getWidth()));
        vitalSpaceHeightTextField.setText(Integer.toString(room.getVitalSpace().getHeight()));

        oKButton.addActionListener(e -> {
            if (!validateForm()) {
                return;
            }
            int roomWidth = Integer.parseInt(roomWidthTextField.getText());
            int roomHeight = Integer.parseInt(roomHeightTextField.getText());
            int vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
            int vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
            if (event.getActionCommand().equals("New")) {
                controller.createRoom(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
            } else {
                if (validateDimensions(room, roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight)) {
                    return;
                }
                room.setDimensions(roomWidth, roomHeight);
                room.getVitalSpace().setWidth(vitalSpaceWidth);
                room.getVitalSpace().setHeight(vitalSpaceHeight);
                for (Section section: room.getSections()){
                    section.accept(new SelectionAdapter() {
                        @Override
                        public void visit(SeatedSection section) {
                            section.refresh();
                        }
                    });
                }
            }

            setVisible(false);
            dispose();
            ui.repaint();
        });

        cancelButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean validateDimensions(Room room, int roomWidth, int roomHeight, int vitalSpaceWidth, int vitalSpaceHeight) {
        VitalSpace vs = new VitalSpace(vitalSpaceWidth, vitalSpaceHeight);
        Room predict = new Room(roomWidth, roomHeight, vs);
        Optional<Stage> opt = room.getStage();
        if (opt.isPresent()) {
            if (!predict.validShape(opt.get().getShape(), new Point())) {
                JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            for (Section section : room.getSections()) {
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
