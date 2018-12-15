package app.gui;

import app.domain.Seat;
import app.domain.SeatSection;
import app.domain.UIPanel;

import javax.swing.*;

import static app.gui.GUIUtils.isNotNumber;

public final class SeatSectionEdition extends JFrame {
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel panelMain;

    SeatSectionEdition(SeatSection seatSection, UIPanel panel) {
        setContentPane(panelMain);
        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            for (Seat seat : seatSection.getSeats()) {
                seat.setPrice(Double.parseDouble(price.getText()));
            }
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
        if (isNotNumber(price.getText())) {
            JOptionPane.showMessageDialog(null, "One or more fields are not a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
