package app.gui;

import app.domain.Controller;
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

    StageEdition(Controller controller, Stage stage, UIPanel panel) {
        setContentPane(panelMain);
        width.setText(stage.getWidth() + "");
        height.setText(stage.getHeight() + "");
        elevation.setText(stage.getElevation() + "");

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            int stageWidth = Integer.parseInt(width.getText());
            int stageHeight = Integer.parseInt(height.getText());
            if (controller.validateStageDimensions(stage, stageWidth, stageHeight)) {
                stage.setWidth(stageWidth);
                stage.setHeight(stageHeight);
                stage.setElevation(Integer.parseInt(elevation.getText()));
                setVisible(false);
                dispose();
                panel.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
