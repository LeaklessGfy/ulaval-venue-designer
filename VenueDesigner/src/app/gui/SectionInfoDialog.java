package app.gui;

import javax.swing.*;

 public final class SectionInfoDialog {

        private SectionInfoDialog() {
        }

        public static int[] show(){
            JPanel myPanel = new JPanel();
            JTextField xField = new JTextField(5);
            JTextField yField = new JTextField(5);
            myPanel.add(new JLabel("Columns:"));
            myPanel.add(xField);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Rows:"));
            myPanel.add(yField);
            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Please enter the number of rows and columns", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String x = xField.getText();
                String y = yField.getText();
                try
                {
                    int xInt = Integer.parseInt(x);
                    int yInt = Integer.parseInt(y);
                    int[] array = {xInt, yInt};
                    for (int i: array){
                        if (i<1){
                            JOptionPane.showMessageDialog(null, "Input values should be non-zero positive integers");
                            array[0]=-1;
                        }
                    }
                    return array;
                }
                catch (NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(null, "Input values should be non-zero positive integers");
                }
            }
            int[] array = {-1};
            return array;
        }
}