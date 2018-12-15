package app.gui;

import app.domain.Controller;
import app.domain.UIPanel;
import app.domain.VitalSpace;
import app.domain.section.SeatedSection;

import javax.swing.*;
import java.util.Locale;

import static app.gui.GUIUtils.isNotNumber;

public class IrregularSectionEdition  extends JFrame {
    private JTextField name;
    private JTextField elevationText;
    private JPanel panel1;
    private JTextField priceText;
    private JTextField vsHeightText;
    private JTextField vsWidthText;
    private JButton okButton;
    private JButton cancelButton;

    IrregularSectionEdition(Controller controller, SeatedSection section, UIPanel panel) {
        setContentPane(panel1);
        VitalSpace vitalSpace = section.getVitalSpace();
        name.setText(section.getName());
        elevationText.setText(String.format(Locale.ROOT,"%.2f", section.getElevation()));
        vsWidthText.setText(String.format(Locale.ROOT,"%.2f", vitalSpace.getWidth()));
        vsHeightText.setText(String.format(Locale.ROOT,"%.2f", vitalSpace.getHeight()));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }

            double spaceWidth = Double.parseDouble(vsWidthText.getText());
            double spaceHeight = Double.parseDouble(vsHeightText.getText());

            section.setName(name.getText());
            section.setElevation(Double.parseDouble(elevationText.getText()));
            section.setVitalSpace(new VitalSpace(spaceWidth, spaceHeight));
            boolean check = false;
            if(!section.autoSetSeat){section.autoSetSeat=true;
            check=true;}
            section.forEachSeats(seat -> {
                seat.setPrice(Double.parseDouble(priceText.getText()));
            });
            setVisible(false);
            dispose();
            controller.autoSetSeat();
            if (check){
                section.autoSetSeat=false;
            }
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private boolean isValidForm() {
        if (

                        isNotNumber(elevationText.getText()) ||
                        isNotNumber(vsWidthText.getText()) ||
                        isNotNumber(vsHeightText.getText()) ||
                        isNotNumber(priceText.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}

