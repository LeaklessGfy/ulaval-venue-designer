package app.gui;

import app.domain.*;
import app.domain.section.SeatedSection;
import app.domain.section.Section;

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
                if (event.getActionCommand().equals("New") || !room.isPresent()) {
                    int roomWidth = Integer.parseInt(roomWidthTextField.getText());
                    int roomHeight = Integer.parseInt(roomHeightTextField.getText());
                    int vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
                    int vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
                    controller.createRoom(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
                    setVisible(false);
                    dispose();
                    ui.repaint();
                } else {
                    Room r = room.get();
                    r.setWidth(Integer.parseInt(roomWidthTextField.getText()));
                    r.setHeight(Integer.parseInt(roomHeightTextField.getText()));
                    r.getVitalSpace().setWidth(Integer.parseInt(vitalSpaceWidthTextField.getText()));
                    r.getVitalSpace().setHeight(Integer.parseInt(vitalSpaceHeightTextField.getText()));
                    setVisible(false);
                    for (Section section: r.getSections()){
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
                    dispose();
                    ui.repaint();
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
