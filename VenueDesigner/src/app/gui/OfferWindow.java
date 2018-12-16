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
    private JLabel typeAmount;
    private JPanel panelEdit;
    private JButton editButton;
    private ArrayList<Offer> LOffer = new ArrayList();


    private boolean testB;

    OfferWindow(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        panelEdit.setVisible(false);
        ButtonGroup Discount = new ButtonGroup();
        Discount.add(radioButtonPercent);
        Discount.add(radioButtonDollar);
        DefaultListModel modelOffers = new DefaultListModel();
        listOffers.setModel(modelOffers);

        okButton.addActionListener(e ->{
            setVisible(false);
            dispose();
        });

        cancelButton.addActionListener(e ->{
            setVisible(false);
            dispose();
        });

        addButton.addActionListener(e ->{
            testB = true; // le bouton add est clique
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

        removeButton.addActionListener( e -> {
            LOffer.remove(listOffers.getSelectedIndex());
            modelOffers.removeElementAt(listOffers.getSelectedIndex());
        });

        okButtonOffer.addActionListener(e->{

            panelEdit.setVisible(false);
            if (!isValidFormName()) {
                return;
            }
            if (!isValidFormDblName()){
                return;
            }
            if (!isValidType()) {
                return;
            }
            if (!isValidFormAmount()) {
                return;
            }
            if (testB == true){ // si je clique sur add
                Offer offer = new Offer(tfOfferName.getText(),typeAmount.getText(),Integer.parseInt(tfAmount.getText()));
                modelOffers.addElement(offer);
                LOffer.add(offer);
                tfOfferName.setText("");
                tfAmount.setText("");
                offer.setLoffer(LOffer);
            }else{ // si je clique sur edit
                Offer offer = new Offer(tfOfferName.getText(),typeAmount.getText(),Integer.parseInt(tfAmount.getText()));
                modelOffers.addElement(offer);
                LOffer.add(offer);
                LOffer.remove(listOffers.getSelectedIndex());
                modelOffers.removeElementAt(listOffers.getSelectedIndex());
                tfOfferName.setText("");
                tfAmount.setText("");
                offer.setLoffer(LOffer);
            }
        });

        editButton.addActionListener(e->{
            testB = false; // le bouton edit est clique
            tfOfferName.setText(LOffer.get(listOffers.getSelectedIndex())+"");// tfOfferName.setText(Loffer.get(listOffers.getSelectedIndex()).getName()+"");
            tfAmount.setText(LOffer.get(listOffers.getSelectedIndex()).getDiscountPrice()+"");
            if(LOffer.get(listOffers.getSelectedIndex()).getDiscountMode().equals("$")){
                radioButtonDollar.setSelected(true);
                radioButtonPercent.setSelected(false);
            }else{
                radioButtonDollar.setSelected(false);
                radioButtonPercent.setSelected(true);
            }
            panelEdit.setVisible(true);

        });

        ////////// listener sur les elements dela liste //////
        listOffers.addListSelectionListener(e->{
            testB = false; // le bouton edit est clique
            tfOfferName.setText(LOffer.get(listOffers.getSelectedIndex())+"");// tfOfferName.setText(Loffer.get(listOffers.getSelectedIndex()).getName()+"");
            tfAmount.setText(LOffer.get(listOffers.getSelectedIndex()).getDiscountPrice()+"");
            if(LOffer.get(listOffers.getSelectedIndex()).getDiscountMode().equals("$")){
                radioButtonDollar.setSelected(true);
                radioButtonPercent.setSelected(false);
            }else{
                radioButtonDollar.setSelected(false);
                radioButtonPercent.setSelected(true);
            }
        });
        ////////////////////////////////////////////////////

        cancelButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
            tfOfferName.setText("");
            tfAmount.setText("");

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
    private boolean isValidFormDblName() {
        boolean test = true;
        for(Offer offer : LOffer){
            if(tfOfferName.equals(offer.getName())){
                test = false;
                break;
            }
        }
        if(test == false){
            JOptionPane.showMessageDialog(null, "Sorry, offer name already exists you have to choose another one ", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return test;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    // faire une validate form pour savoir si il existe un objet dans la liste des offres qui a le meme nom


}
