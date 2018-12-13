package app.gui;

import app.domain.Seat;
import app.domain.UIPanel;

import javax.swing.*;

import java.util.Locale;

import static app.gui.GUIUtils.isNotNumber;

public final class SeatEdition extends JFrame {
    private JPanel panelMain;
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;

    SeatEdition(Seat seat, UIPanel panel) {
        setContentPane(panelMain);
        price.setText(String.format(Locale.ROOT,"%.2f",seat.getPrice()));

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            seat.setPrice(Double.parseDouble(price.getText()));
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
