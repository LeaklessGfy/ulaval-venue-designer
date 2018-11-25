package app.gui;

import app.domain.UIPanel;
import app.domain.VitalSpace;
import app.domain.section.SeatedSection;

import javax.swing.*;

public class SectionEdition extends JFrame {
    private JTextField columns;
    private JTextField rows;
    private JTextField elevation;
    private JButton okButtton;
    private JButton cancelButton;
    private JPanel panelMain;
    private JTextField vitalSpaceWidth;
    private JTextField vitalSpaceHeight;

    SectionEdition(SeatedSection section, UIPanel panel) {
        setContentPane(panelMain);
        VitalSpace vitalSpace = section.getVitalSpace();
        columns.setText(section.getColumns() + "");
        rows.setText(section.getRows() + "");
        elevation.setText(section.getElevation() + "");
        vitalSpaceWidth.setText(vitalSpace.getWidth() + "");
        vitalSpaceHeight.setText(vitalSpace.getHeight() + "");

        okButtton.addActionListener(e -> {
            section.setDimensions(Integer.parseInt(columns.getText()), Integer.parseInt(rows.getText()));
            section.setElevation(Integer.parseInt(elevation.getText()));
            int spaceWidth = Integer.parseInt(vitalSpaceWidth.getText());
            int spaceHeight = Integer.parseInt(vitalSpaceHeight.getText());
            if (spaceWidth != vitalSpace.getWidth() || spaceHeight != vitalSpace.getHeight()) {
                section.setVitalSpace(new VitalSpace(spaceWidth, spaceHeight));
            }
            setVisible(false);
            dispose();
            panel.repaint();
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
}
