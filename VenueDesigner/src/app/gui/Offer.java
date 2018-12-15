package app.gui;

import app.domain.Controller;
import app.domain.Room;
import app.domain.UIPanel;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.selection.SelectionAdapter;

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


    private JTextField TfOfferName;
    private JTextField TfAmount;
    private JList list1;
    private JLabel typeAmount;
    private JPanel Panel2;
    private JButton okButtonOffer;
    private JButton cancelButtonOffer;


    Offer(Controller controller, UIPanel ui, ActionEvent event) {
        Objects.requireNonNull(controller);
        setContentPane(panelMain);


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

        addButton.addActionListener(e ->{});

        cancelButton.addActionListener(e ->{});

        radioButtonPercent.addActionListener(e->{});

        radioButtonDollar.addActionListener(e->{});

        removeButton.addActionListener( e -> {});

        /*removeButton.addActionListener( e -> {
            setVisible(false);
            dispose();
        });*/
    }

}