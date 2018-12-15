package app.gui;

import app.domain.Seat;
import app.domain.UIPanel;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;

import static app.gui.GUIUtils.isNotNumber;

public final class SeatEdition extends JFrame {
    private JPanel panelMain;
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;
    private JButton colorButton;

    private ColorPicker colorPicker = new ColorPicker();

    SeatEdition(Seat seat, UIPanel panel) {
        setContentPane(panelMain);
        price.setText(String.format(Locale.ROOT,"%.2f",seat.getPrice()));
        colorButton.addActionListener(e -> {
            colorPicker.setSize(300, 400);
            colorPicker.setVisible(true);
            colorPicker.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    colorButton.setBackground(colorPicker.getColor());
                }
            });
        });

        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            seat.setPrice(Double.parseDouble(price.getText()));
            seat.getShape().setColor(colorToArray(colorPicker.getColor()));
            colorPicker.getColor();
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            colorPicker.setVisible(false);
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

    private int[] colorToArray(Color color) {
        return new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
    }
}
