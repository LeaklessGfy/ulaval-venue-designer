package app.gui;

import app.domain.Controller;
import app.domain.UIPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class Offer extends JFrame{
    private JPanel panelMain;
    private JButton addButton;
    private JButton removeButton;
    private JButton okButton;
    private JButton cancelButton;
    private JRadioButton radioButtonPercent;
    private JRadioButton radioButtonDollar;
    private JButton okButtonOffer;
    private JButton cancelButtonOffer;

    private JTextField tfOfferName;
    private JTextField tfAmount;
    private JList listOffers;
    private JList listSeat;
    private JLabel typeAmount;
    private JPanel panelEdit;
    private JPanel panelSeat;
    private JButton removeSeatButton;
    private JButton addSeatButton;
    private JLabel labelTotSeats;


    Offer(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        panelEdit.setVisible(false);
        panelSeat.setVisible(false);
        ButtonGroup Discount = new ButtonGroup();
        Discount.add(radioButtonPercent);
        Discount.add(radioButtonDollar);

        DefaultListModel modelOffers = new DefaultListModel();
        DefaultListModel modelSeat = new DefaultListModel();
        listOffers.setModel(modelOffers);
        listSeat.setModel(modelSeat);



        //Room room = controller.getRoom(); // voir apres pour get des trucs genre sections et sieges

        /*okButton.addActionListener(e -> {

            if (event.getActionCommand().equals("Offers")) {
                controller.createOffer();
                list1.setVisible(false);
            } else {
                if (!validateDimensions(room, roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight)) {
                    return;
                }
                room.setDimensions(roomWidth, roomHeight);
                room.getVitalSpace().setWidth(vitalSpaceWidth);
                room.getVitalSpace().setHeight(vitalSpaceHeight);
                for (Section section: room.getSections()){
                    section.accept(new SelectionAdapter() {
                        @Override
                        public void visit(SeatedSection section) {
                            section.refresh();
                        }
                    });
                }
            }

            setVisible(false);
            dispose();
            ui.repaint();
        });*/

        okButton.addActionListener(e ->{});

        addButton.addActionListener(e ->{
            panelEdit.setVisible(true);
        });

        cancelButton.addActionListener(e ->{});

        radioButtonPercent.addActionListener(e->{
            if(radioButtonPercent.isSelected()){
                typeAmount.setText("%");
            }
        });

        radioButtonDollar.addActionListener(e->{
            if(radioButtonDollar.isSelected()){
                typeAmount.setText("$");
            }
        });

        removeButton.addActionListener( e -> {});

        ////////// ajouter un element dans la liste des offres////
        okButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
            //modelOffers.addElement();
        });
        ///////////////////////////////////////////////
        cancelButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
            tfOfferName.setText("");
            tfAmount.setText("");
            radioButtonPercent.setSelected(false);
            radioButtonDollar.setSelected(false);
        });

        addSeatButton.addActionListener(e->{

        });

        removeSeatButton.addActionListener(e->{

        });


        /*removeButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });*/
    }

}
