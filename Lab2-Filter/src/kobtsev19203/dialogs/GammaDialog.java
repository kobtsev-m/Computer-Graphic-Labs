package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.DoudleTextAndSLiderCreator;
import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.propertylisteners.DoublePropertyListener;
import kobtsev19203.instruments.Gamma;

import javax.swing.*;
import java.awt.*;

public class GammaDialog extends JPanel implements MyDialog {
    final double MIN_VALUE = 0.1;
    final double MAX_VALUE = 10;
    final int MAX_VALUE_LENGTH = 4;
    private boolean isOk = false;
    JTextField valueText;
    Gamma gamma;

    public GammaDialog(Gamma gamma) {
        super();
        setLayout(new GridLayout(2, 1));
        this.gamma = gamma;
        JPanel paramsPanel = new JPanel(new GridLayout(1, 3));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        TextAndSlider ts = DoudleTextAndSLiderCreator.create(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, gamma.getGamme());
        ts.text.addPropertyChangeListener(new DoublePropertyListener(MIN_VALUE, MAX_VALUE, ts.text, ts.slider));
        paramsPanel.add(new Label("p:"));
        valueText = ts.text;
        paramsPanel.add(ts.text);
        paramsPanel.add(ts.slider);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel p = this;
        okButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            gamma.setGamma((double)Math.round(Double.parseDouble(valueText.getText())*10)/10);
            valueText.setText(String.valueOf(gamma.getGamme()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            valueText.setText(String.valueOf(gamma.getGamme()));
            isOk = false;
            frame.dispose();
        });
        add(paramsPanel);
        add(buttonPanel);
    }

    @Override
    public boolean isDialogResult() {
        return isOk;
    }
}
