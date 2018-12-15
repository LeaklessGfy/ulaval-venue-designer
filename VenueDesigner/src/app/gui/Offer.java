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
    private JList list1;
    private JLabel typeAmount;
    private JPanel panelEdit;



    Offer(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);
        panelEdit.setVisible(false);
        ButtonGroup Discount = new ButtonGroup();
        Discount.add(radioButtonPercent);
        Discount.add(radioButtonDollar);

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

        okButtonOffer.addActionListener(e->{});

        cancelButtonOffer.addActionListener(e->{
            panelEdit.setVisible(false);
        });

        /*removeButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });*/
    }

}
