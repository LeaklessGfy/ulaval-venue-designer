package app.gui;

import app.domain.Controller;
import app.domain.section.SeatedSection;
import app.domain.VitalSpace;
import app.domain.UIPanel;

import javax.swing.*;

import java.util.Locale;

import static app.gui.GUIUtils.isNotInteger;
import static app.gui.GUIUtils.isNotNumber;

public final class SectionEdition extends JFrame {
    private JTextField columns;
    private JTextField rows;
    private JTextField elevation;
    private JButton okButtton;
    private JButton cancelButton;
    private JPanel panelMain;
    private JTextField vitalSpaceWidth;
    private JTextField vitalSpaceHeight;
    private JTextField price;

    SectionEdition(Controller controller, SeatedSection section, UIPanel panel) {
        setContentPane(panelMain);
        VitalSpace vitalSpace = section.getVitalSpace();
        columns.setText(section.getColumns() + "");
        rows.setText(section.getRows() + "");
        elevation.setText(String.format(Locale.ROOT,"%.2f",section.getElevation()));
        vitalSpaceWidth.setText(String.format(Locale.ROOT,"%.2f",vitalSpace.getWidth()));
        vitalSpaceHeight.setText(String.format(Locale.ROOT,"%.2f",vitalSpace.getHeight()));

        okButtton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            int nbColums = Integer.parseInt(columns.getText());
            int nbRows = Integer.parseInt(rows.getText());
            double spaceWidth = Double.parseDouble(vitalSpaceWidth.getText());
            double spaceHeight = Double.parseDouble(vitalSpaceHeight.getText());
            if (controller.validateSectionDimensions(section, nbColums, nbRows, spaceWidth, spaceHeight)) {
                section.setDimensions(nbColums, nbRows);
                section.setElevation(Double.parseDouble(elevation.getText()));
                if (spaceWidth != vitalSpace.getWidth() || spaceHeight != vitalSpace.getHeight()) {
                    section.setVitalSpace(new VitalSpace(spaceWidth, spaceHeight));
                }
                section.forEachSeats(seat -> {
                    seat.setPrice(Double.parseDouble(price.getText()));
                });
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
                isNotInteger(columns.getText()) ||
                        isNotInteger(rows.getText()) ||
                        isNotNumber(elevation.getText()) ||
                        isNotNumber(vitalSpaceWidth.getText()) ||
                        isNotNumber(vitalSpaceHeight.getText()) ||
                        isNotNumber(price.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
