package app.gui;

import app.domain.*;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.selection.SelectionAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

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
            if (event.getActionCommand().equals("New")) {
                int roomWidth = Integer.parseInt(roomWidthTextField.getText());
                int roomHeight = Integer.parseInt(roomHeightTextField.getText());
                int vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
                int vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
                controller.createRoom(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
            } else {
                room.setWidth(Integer.parseInt(roomWidthTextField.getText()));
                room.setHeight(Integer.parseInt(roomHeightTextField.getText()));
                room.getVitalSpace().setWidth(Integer.parseInt(vitalSpaceWidthTextField.getText()));
                room.getVitalSpace().setHeight(Integer.parseInt(vitalSpaceHeightTextField.getText()));
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

    private boolean validateForm() {
        if(
                roomWidthTextField.getText().isEmpty() ||
                roomHeightTextField.getText().isEmpty() ||
                vitalSpaceWidthTextField.getText().isEmpty() ||
                vitalSpaceHeightTextField.getText().isEmpty()
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (
                isNotInteger(roomWidthTextField.getText()) ||
                        isNotInteger(roomHeightTextField.getText()) ||
                        isNotInteger(vitalSpaceWidthTextField.getText()) ||
                        isNotInteger(vitalSpaceHeightTextField.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
