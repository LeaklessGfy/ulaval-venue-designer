package app.gui;

import app.domain.*;
import app.domain.seat.Seat;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

final class OfferAttribution extends JFrame{
    private JButton addOfferButton;
    private JButton removeOfferButton;
    private JButton okButton;
    private JList<Offer> seatOffers;
    private JPanel panelMain;
    private JPanel panelAdd;
    private JButton addOffersToSeat;
    private JList<Offer> listOffers;

    OfferAttribution(Controller controller, Seat seat) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        setSize(300, 400);
        setVisible(true);
        panelAdd.setVisible(false);

        DefaultListModel<Offer> modelSeatOffer = new DefaultListModel<>();
        for (Offer offer : seat.getOffers()) {
            modelSeatOffer.addElement(offer);
        }
        seatOffers.setModel(modelSeatOffer);

        DefaultListModel<Offer> modelOffer = new DefaultListModel<>();
        for (Offer offer : controller.getRoom().getOffers()) {
            modelOffer.addElement(offer);
        }
        listOffers.setModel(modelOffer);

        addOfferButton.addActionListener(e -> {
            panelAdd.setVisible(true);
        });

        removeOfferButton.addActionListener(e -> {
            Offer offer = seatOffers.getSelectedValue();
            if (offer == null) {
                return;
            }
            seat.getOffers().remove(offer);
            modelSeatOffer.removeElement(offer);
        });

        addOffersToSeat.addActionListener(e -> {
            List<Offer> offerList = listOffers.getSelectedValuesList();
            List<Offer> seatOffer = seat.getOffers();
            for (Offer offer : offerList) {
                if (seatOffer.contains(offer)) {
                    continue;
                }
                seatOffer.add(offer);
                modelSeatOffer.addElement(offer);
            }
        });

        okButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
}
