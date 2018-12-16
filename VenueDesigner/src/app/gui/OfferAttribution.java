package app.gui;

import app.domain.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

public class OfferAttribution extends JFrame{
    private JButton addToOfferButton;
    private JButton removeToOfferButton;
    private JButton okButton;
    private JButton cancelButton;
    private JList listOffer;
    private JPanel panelMain;
    private ArrayList<Seat> ListSeat = new ArrayList();
    private ArrayList<Offer> LOffer = Offer.Loffer;


    OfferAttribution(Controller controller, UIPanel ui, ActionEvent event) {//Controller controller,SeatSection seatSection, UIPanel ui, ActionEvent event
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        DefaultListModel modelOffer = new DefaultListModel();
        listOffer.setModel(modelOffer);
        for(Offer offer: LOffer){
            modelOffer.addElement(offer);
        }

        addToOfferButton.addActionListener(e->{
            /*for (Seat seat : seatSection.getSeats()) {
                Offer offer = LOffer.get(listOffer.getSelectedIndex());
                seat.setDoublePrice(offer.Discount(offer.getDiscountMode(),seat.getPrice(),offer.getDiscountPrice()));
            }*/
            

        });

        removeToOfferButton.addActionListener(e->{

        });

        okButton.addActionListener(e->{

        });

        cancelButton.addActionListener(e->{});

        listOffer.addListSelectionListener(e->{

        });
    }

}
