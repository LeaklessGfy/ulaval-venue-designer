package app.gui;

import app.domain.*;
import app.domain.shape.Shape;
import app.domain.section.Section;
import app.domain.shape.Point;

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
            if (validateDimensions(controller, stage, stageWidth, stageHeight)) {
                stage.setWidth(stageWidth);
                stage.setHeight(stageHeight);
                stage.setElevation(Integer.parseInt(elevation.getText()));
                setVisible(false);
                dispose();
                panel.repaint();
            }
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean validateDimensions(Controller controller, Stage stage, int width, int height) {
        Room room = controller.getRoom();
        Shape shape = stage.getShape().clone();
        Stage predict = new Stage(shape);
        predict.setWidth(width);
        predict.setHeight(height);
        if (!room.validShape(predict.getShape(), new Point())) {
            JOptionPane.showMessageDialog(null, "Inconsistent dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (Section s : room.getSections()) {
            if (controller.getCollider().hasCollide(s.getShape(), predict.getShape())) {
                JOptionPane.showMessageDialog(null, "Collision with other sections.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
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
