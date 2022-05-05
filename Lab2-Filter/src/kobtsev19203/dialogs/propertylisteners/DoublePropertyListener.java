package kobtsev19203.dialogs.propertylisteners;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DoublePropertyListener implements PropertyChangeListener {
    private double minvalue;
    private double maxvalue;
    private JFormattedTextField text;
    private JSlider slider;
    public DoublePropertyListener(double minvalue, double maxvalue, JFormattedTextField text, JSlider slider) {
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
        double inputValue = Double.parseDouble(text.getText());
        if (minvalue > inputValue) {
            text.setText(String.valueOf(minvalue));
            return;
        }
        if (maxvalue < inputValue) {
            text.setText(String.valueOf(maxvalue));
            return;
        }
        double value = Double.parseDouble(text.getText());
        //setter.accept(value);
        slider.setValue((int)(Double.parseDouble(text.getText())*10));
    }
}
