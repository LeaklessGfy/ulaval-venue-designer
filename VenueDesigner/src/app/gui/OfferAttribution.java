package app.gui;

import app.domain.Controller;
import app.domain.UIPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class OfferAttribution extends JFrame{
    private JButton addToOfferButton;
    private JButton removeToOfferButton;
    private JButton okButton;
    private JButton cancelButton;
    private JList listOffer;
    private JPanel panelMain;


    OfferAttribution(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
    }

}
