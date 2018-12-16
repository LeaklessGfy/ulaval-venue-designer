package app.gui;

import app.domain.Controller;
import app.domain.Seat;
import app.domain.section.Section;

import javax.swing.*;
import java.util.Locale;

public final class SeatInfo extends JDialog{
    private JPanel panelMain;
    private JTextField number;
    private JTextField row;
    private JTextField section;
    private JTextField price;
    private JTextField elevation;
    private JTextArea textArea1;

    SeatInfo() {
        setContentPane(panelMain);
        setUndecorated(true);
        setSize(300, 400);
    }

    void update(Controller controller) {
        Seat hoveredSeat = controller.getHoveredSeat();
        Section hoveredSection = controller.getHoveredSection();

        number.setText(hoveredSeat.getNumber()+ "");
        section.setText(hoveredSection.getName());
        row.setText(hoveredSeat.getRow()+1+ "");
        price.setText(String.format(Locale.ROOT,"%.2f", hoveredSeat.getPrice()));
        elevation.setText(String.format(Locale.ROOT,"%.2f", hoveredSection.getElevation()));
    }
}
