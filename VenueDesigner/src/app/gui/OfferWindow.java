package app.gui;

import app.domain.Controller;
import app.domain.Offer;
import app.domain.UIPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

import static app.gui.GUIUtils.isNotInteger;

public class OfferWindow extends JFrame{
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


    OfferWindow(Controller controller, UIPanel ui, ActionEvent event) {
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
        ArrayList<Offer> Loffer = new ArrayList();



        okButton.addActionListener(e ->{

        });

        cancelButton.addActionListener(e ->{
            setVisible(false);
            dispose();
        });

        addButton.addActionListener(e ->{
            panelEdit.setVisible(true);
        });



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
        /////////// supprimer un element dans la liste des offres ////
        removeButton.addActionListener( e -> {

            modelOffers.removeElementAt(listOffers.getSelectedIndex());
            Loffer.remove(listOffers.getSelectedIndex());

        });


        /////////////////////////////////////////////////////////////

        ////////// ajouter un element dans la liste des offres////
        okButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
            if (!isValidFormName()) {
                return;
            }
            if (!isValidType()) {
                return;
            }
            if (!isValidFormAmount()) {
                return;
            }
            Offer offer = new Offer(tfOfferName.getText(),typeAmount.getText(),Integer.parseInt(tfAmount.getText()));
            modelOffers.addElement(offer);
            Loffer.add(offer);


            tfOfferName.setText("");
            tfAmount.setText("");



        });
        ///////////////////////////////////////////////

        ////////// listener sur les elements dela liste //////
        listOffers.addListSelectionListener(e->{
            tfOfferName.setText(Loffer.get(listOffers.getSelectedIndex()).getName()+"");
            tfAmount.setText(Loffer.get(listOffers.getSelectedIndex()).getDiscountPrice()+"");
            if(Loffer.get(listOffers.getSelectedIndex()).getDiscountMode().equals("$")){
                radioButtonDollar.setSelected(true);
                radioButtonPercent.setSelected(false);
            }else{
                radioButtonDollar.setSelected(false);
                radioButtonPercent.setSelected(true);
            }
            panelSeat.setVisible(true);
            panelEdit.setVisible(true);

        });




        cancelButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
            tfOfferName.setText("");
            tfAmount.setText("");

        });

        addSeatButton.addActionListener(e->{

        });

        removeSeatButton.addActionListener(e->{

        });


    }
    private boolean isValidFormAmount() {
        if (isNotInteger(tfAmount.getText()) || tfAmount.getText().isEmpty() || Integer.parseInt(tfAmount.getText())<0 ) {
            JOptionPane.showMessageDialog(null, "Sorry, amount field is not an integer or is empty or is negative", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    private boolean isValidFormName() {
        if (tfOfferName.getText().isEmpty() || tfOfferName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, offer name field is empty ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    private boolean isValidType() {
        if (typeAmount.getText().isEmpty() || typeAmount.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Sorry, you have to choose the discount type", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // faire une validate form pour savoir si il existe un objet dans la liste des offres qui a le meme nom


}
