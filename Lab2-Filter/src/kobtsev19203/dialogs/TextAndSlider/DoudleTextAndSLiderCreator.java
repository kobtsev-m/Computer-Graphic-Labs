package kobtsev19203.dialogs.TextAndSlider;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class DoudleTextAndSLiderCreator {
    private static int d = 10;
    public static TextAndSlider create(double minvalue, double maxvalue, int maxlength, double defaultvalue) {
        return createTextAndSLider(minvalue,maxvalue,maxlength,defaultvalue);
    }
    public static TextAndSlider createDefault(double minvalue, double maxvalue, int maxlength, double defaultvalue) {
        TextAndSlider ts = createTextAndSLider(minvalue,maxvalue,maxlength,defaultvalue);
        ts.text.addPropertyChangeListener(evt -> {
            if("".equals(ts.text.getText())){
                ts.text.setText(String.valueOf(1));
                return;
            }
            double inputValue=Double.parseDouble(ts.text.getText());
            if(minvalue>inputValue){
                ts.text.setText(String.valueOf(minvalue));
                JOptionPane.showMessageDialog(null,"Enter a numerical value from " + minvalue + " to " + maxvalue);
                return;
            }
            if(maxvalue<inputValue) {
                ts.text.setText(String.valueOf(maxvalue));
                JOptionPane.showMessageDialog(null,"Enter a numerical value from " + minvalue + " to " + maxvalue);
                return;
            }
            double value = Double.parseDouble(ts.text.getText());
            ts.slider.setValue((int)(value*d));
        });
        return ts;
    }
    private static TextAndSlider createTextAndSLider(double minvalue, double maxvalue, int maxlength, double defaultvalue) {
        JFormattedTextField text =new JFormattedTextField();
        text.setPreferredSize(new Dimension(35,20));
        JSlider jSlider = new JSlider((int)(minvalue*d), (int)(maxvalue*d), (int)(defaultvalue*d));
        jSlider.setPaintTicks(true);
        jSlider.setMajorTickSpacing(10);
        text.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a)throws BadLocationException {
                if(str.length()+getLength()>maxlength){
                    return;
                }
                for(int i=0;i<str.length();++i){
                    if(!Character.isDigit(str.charAt(i)) && str.charAt(i) != '-' && str.charAt(i) != '.'){
                        return;
                    }
                }
                super.insertString(offs,str,a);
            }
        });
        text.setText(String.valueOf(defaultvalue));
        text.setPreferredSize(new Dimension(35,20));
        text.setVisible(true);
        jSlider.addChangeListener(changeEvent -> {
            int value = (jSlider.getValue());
            text.setText(String.valueOf((double)value/d));
        });
        return new TextAndSlider(text,jSlider);
    }

}
