package kobtsev19203.dialogs.propertylisteners;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OddPropertyListener implements PropertyChangeListener {
    private int minvalue;
    private int maxvalue;
    private JFormattedTextField text;
    private JSlider slider;
    public OddPropertyListener(int minvalue, int maxvalue, JFormattedTextField text, JSlider slider) {
        super();
        this.maxvalue = maxvalue;
        this.minvalue = minvalue;
        this.text = text;
        this.slider = slider;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("".equals(text.getText())) {
            text.setText(String.valueOf(1));
            return;
        }
        int inputValue = Integer.parseInt(text.getText());
        if (minvalue > inputValue) {
            text.setText(String.valueOf(minvalue));
            JOptionPane.showMessageDialog(null,"Enter a numerical value from " + minvalue + " to " + maxvalue);
            return;
        }
        if (maxvalue < inputValue) {
            text.setText(String.valueOf(maxvalue));
            JOptionPane.showMessageDialog(null,"Enter a numerical value from " + minvalue + " to " + maxvalue);
            return;
        }
        if (inputValue%2 == 0) {
            inputValue+=1;
            JOptionPane.showMessageDialog(null,"Entered value must be odd");
            text.setText(String.valueOf(inputValue));
            return;
        }
        slider.setValue(Integer.parseInt(text.getText()));
    }
}
