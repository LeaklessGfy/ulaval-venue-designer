package app.gui;

import app.domain.Controller;
import app.domain.Seat;

import javax.swing.*;
import java.util.Locale;

public class SeatInfo extends JDialog{
    private JPanel panel1;
    private JTextField number;
    private JTextField row;
    private JTextField section;
    private JTextField price;
    private JTextField elevation;
    private JTextArea textArea1;

    SeatInfo(){
        setContentPane(panel1);
        setUndecorated(true);
    }

    public void update(Controller controller){
        number.setText(controller.getHoveredSeat().getNumber()+ "");
        section.setText(controller.getHoveredSection().getName());
        row.setText(controller.getHoveredSeat().getRow()+1+ "");
        price.setText(String.format(Locale.ROOT,"%.2f",controller.getHoveredSeat().getPrice()));
        elevation.setText(String.format(Locale.ROOT,"%.2f",controller.getHoveredSection().getElevation()));
    }
}
