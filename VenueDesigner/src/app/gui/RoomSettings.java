package app.gui;

import app.domain.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class RoomSettings extends JFrame {
    private Controller controller;
    private JPanel panelMain;
    private JTextField roomWidthTextField;
    private JTextField roomHeightTextField;
    private JTextField vitalSpaceWidthTextField;
    private JTextField vitalSpaceHeightTextField;
    private JButton oKButton;
    private JButton cancelButton;

    public RoomSettings(Controller controller, ActionEvent event) {
        this.controller = controller;
        setContentPane(panelMain);

        app.domain.Room room = controller.getRoom();

        if (event.getActionCommand().equals("New")) {
            vitalSpaceWidthTextField.setText("1");
            vitalSpaceHeightTextField.setText("1");

        }
        else {
            roomWidthTextField.setText(Integer.toString(room.getWidth()));
            roomHeightTextField.setText(Integer.toString(room.getHeight()));
            vitalSpaceWidthTextField.setText(Integer.toString(room.getVitalSpace().getWidth()));
            vitalSpaceHeightTextField.setText(Integer.toString(room.getVitalSpace().getHeight()));
        }

        oKButton.addActionListener(e -> {
            if(ValidateForm()) {
                if (event.getActionCommand().equals("New")) {
                    int roomWidth = Integer.parseInt(roomWidthTextField.getText());
                    int roomHeight = Integer.parseInt(roomHeightTextField.getText());
                    int vitalSpaceWidth = Integer.parseInt(vitalSpaceWidthTextField.getText());
                    int vitalSpaceHeight = Integer.parseInt(vitalSpaceHeightTextField.getText());
                    controller.create(roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);
                    setVisible(false);
                    dispose();
                    controller.getDrawingPanel().repaint();
                }
                else {
                    room.setWidth(Integer.parseInt(roomWidthTextField.getText()));
                    room.setHeight(Integer.parseInt(roomHeightTextField.getText()));
                    room.getVitalSpace().setWidth(Integer.parseInt(vitalSpaceWidthTextField.getText()));
                    room.getVitalSpace().setHeight(Integer.parseInt(vitalSpaceHeightTextField.getText()));
                    setVisible(false);
                    dispose();
                    controller.getDrawingPanel().repaint();
                }
            }
        });

        cancelButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean IsInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean ValidateForm() {
        boolean val = false;
        if(roomWidthTextField.getText().isEmpty() == true || roomHeightTextField.getText().isEmpty() == true || vitalSpaceWidthTextField.getText().isEmpty() == true || vitalSpaceHeightTextField.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(null, "One or more fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            if(IsInteger(roomWidthTextField.getText()) == false || IsInteger(roomHeightTextField.getText()) == false || IsInteger(vitalSpaceWidthTextField.getText()) == false || IsInteger(vitalSpaceHeightTextField.getText()) == false) {
                JOptionPane.showMessageDialog(null, "One or more fields are not an integer", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                val = true;
            }
        }
        return val;
    }
}
