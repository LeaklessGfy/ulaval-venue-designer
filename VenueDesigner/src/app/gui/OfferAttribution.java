package app.gui;

import app.domain.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

final class OfferAttribution extends JFrame{
    private JButton addToOfferButton;
    private JButton removeToOfferButton;
    private JButton okButton;
    private JButton cancelButton;
    private JList<String> listOffer;
    private JPanel panelMain;

    OfferAttribution(Controller controller, UIPanel ui, ActionEvent event) {//Controller controller,SeatSection seatSection, UIPanel ui, ActionEvent event
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        DefaultListModel<String> modelOffer = new DefaultListModel<>();
        listOffer.setModel(modelOffer);

        addToOfferButton.addActionListener(e->{
            /*for (Seat seat : seatSection.getSeats()) {
                Offer offer = LOffer.get(listOffer.getSelectedIndex());
                seat.setDoublePrice(offer.Discount(offer.getDiscountMode(),seat.getPrice(),offer.getDiscountPrice()));
            }*/
        });

        removeToOfferButton.addActionListener(e->{

        });

        okButton.addActionListener(e->{
            setVisible(false);
            dispose();
        });

        cancelButton.addActionListener(e->{
            setVisible(false);
            dispose();
        });

        listOffer.addListSelectionListener(e->{

        });
    }

}
