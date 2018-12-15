package app.gui;

import app.domain.UIPanel;
import app.domain.section.StandingSection;

import javax.swing.*;
import java.util.Locale;

import static app.gui.GUIUtils.isNotNumber;
import static app.gui.GUIUtils.isNotInteger;

public class StandingSectionEdition extends JFrame{
    private JButton okButton;
    private JPanel panel1;
    private JTextField elevation;
    private JTextField price;
    private JButton cancelButton;
    private JTextField max;

    StandingSectionEdition(StandingSection section, UIPanel panel) {
        setContentPane(panel1);

        elevation.setText(String.format(Locale.ROOT,"%.2f",section.getElevation()));
        max.setText(section.getMax()+"");
        price.setText(String.format(Locale.ROOT,"%.2f",section.getPrice()));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            section.setElevation(Double.parseDouble(elevation.getText()));
            section.setPrice(Double.parseDouble(price.getText()));
            section.setMax(Integer.parseInt(max.getText()));
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
                isNotNumber(elevation.getText()) ||
                isNotNumber(price.getText())||
                isNotInteger(max.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
