package app.gui;

import app.domain.UIPanel;

import app.domain.section.StandingSection;

import javax.swing.*;

import static app.gui.GUIUtils.isNotInteger;

public class StandingSectionEdition extends JFrame{
    private JTextField name;
    private JTextField maxPeople;
    private JTextField elevation;
    private JTextField price;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel panelMain;

    StandingSectionEdition(StandingSection section, UIPanel panel){
        setContentPane(panelMain);
        name.setText(section.getName());
        maxPeople.setText(section.getMax()+"");
        elevation.setText(section.getElevation()+"");


        okButton.addActionListener(e -> {
            if (!isValidForm()) {
                return;
            }
            section.setElevation(Integer.parseInt(elevation.getText()));
            section.setName(name.getText());
            section.setPrice(Integer.parseInt(price.getText()));
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
    private boolean isValidForm(){
        if (isNotInteger(maxPeople.getText()) ||
                isNotInteger(elevation.getText()) ||
                isNotInteger(price.getText())
        ) {
            JOptionPane.showMessageDialog(null, "One or more fields are not an integer", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
