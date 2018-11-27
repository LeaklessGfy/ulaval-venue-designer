package app.gui;

import app.domain.Stage;
import app.domain.UIPanel;

import javax.swing.*;

import static app.gui.GUIUtils.isNotInteger;

public final class StageEdition extends JFrame {
    private JTextField width;
    private JTextField height;
    private JTextField elevation;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel panelMain;

    StageEdition(Stage stage, UIPanel panel) {
        setContentPane(panelMain);
        width.setText(stage.getWidth() + "");
        height.setText(stage.getHeight() + "");
        elevation.setText(stage.getElevation() + "");

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            stage.setWidth(Integer.parseInt(width.getText()));
            stage.setHeight(Integer.parseInt(height.getText()));
            stage.setElevation(Integer.parseInt(elevation.getText()));
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if (
                isNotInteger(width.getText()) ||
                        isNotInteger(height.getText()) ||
                        isNotInteger(elevation.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
