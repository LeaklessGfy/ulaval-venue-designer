package app.gui;

import app.domain.Seat;
import app.domain.UIPanel;

import javax.swing.*;

public class SeatEdition extends JFrame {
    private JPanel panelMain;
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;

    SeatEdition(Seat seat, UIPanel panel) {
        setContentPane(panelMain);
        price.setText(seat.getPrice() + "");

        okButton.addActionListener(e -> {
            seat.setPrice(Integer.parseInt(price.getText()));
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
}
