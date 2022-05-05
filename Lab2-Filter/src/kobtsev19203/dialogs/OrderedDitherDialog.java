package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.OrderedDither;

import javax.swing.*;
import java.awt.*;

public class OrderedDitherDialog extends JPanel implements MyDialog {
    final int MIN_VALUE = 2;
    final int MAX_VALUE = 128;
    final int MAX_VALUE_LENGTH = 3;
    private boolean isOk = false;
    OrderedDither orderedDither;
    JTextField blueText;
    JTextField greenText;
    JTextField redText;
    public OrderedDitherDialog(OrderedDither orderedDither) {
        super();
        setLayout(new GridLayout(2, 1));
        JPanel paramsPanel = new JPanel(new GridLayout(3, 3));
        JPanel buttonPanel = new JPanel();
        this.orderedDither = orderedDither;
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, orderedDither.getRed());
        paramsPanel.add(new Label("Red:"));
        redText = ts.text;
        paramsPanel.add(redText);
        paramsPanel.add(ts.slider);

        ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, orderedDither.getGreen());
        paramsPanel.add(new Label("Green:"));
        greenText = ts.text;
        paramsPanel.add(greenText);
        paramsPanel.add(ts.slider);

        ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, orderedDither.getBlue());
        paramsPanel.add(new Label("Blue:"));
        blueText = ts.text;
        paramsPanel.add(blueText);
        paramsPanel.add(ts.slider);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel p = this;
        okButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            orderedDither.setBlue(Integer.parseInt(blueText.getText()));
            orderedDither.setGreen(Integer.parseInt(greenText.getText()));
            orderedDither.setRed(Integer.parseInt(redText.getText()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            blueText.setText(String.valueOf(orderedDither.getBlue()));
            greenText.setText(String.valueOf(orderedDither.getGreen()));
            redText.setText(String.valueOf(orderedDither.getRed()));
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