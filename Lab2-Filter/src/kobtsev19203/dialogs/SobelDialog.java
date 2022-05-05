package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.Sobel;

import javax.swing.*;
import java.awt.*;

public class SobelDialog extends JPanel implements MyDialog {
    final int MIN_VALUE = 0;
    final int MAX_VALUE = 255;
    final int MAX_VALUE_LENGTH = 3;
    public JFormattedTextField valueText;
    private boolean isOk = false;
    Sobel sobel;

    public SobelDialog(Sobel sobel) {
        super();
        setLayout(new GridLayout(2, 1));
        JPanel paramsPanel = new JPanel(new GridLayout(1, 3));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.sobel = sobel;
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, sobel.getBinParameter());
        paramsPanel.add(new Label("Binarization parameter:"));
        valueText = ts.text;
        paramsPanel.add(ts.text);
        paramsPanel.add(ts.slider);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel p = this;
        okButton.addActionListener(e -> {
            JDialog frame = (JDialog)p.getRootPane().getParent();
            sobel.setBinParameter(Integer.parseInt(valueText.getText()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog)p.getRootPane().getParent();
            valueText.setText(String.valueOf(sobel.getBinParameter()));
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
