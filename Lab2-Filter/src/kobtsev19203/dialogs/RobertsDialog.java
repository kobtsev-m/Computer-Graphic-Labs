package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.Roberts;

import javax.swing.*;
import java.awt.*;

public class RobertsDialog extends JPanel implements MyDialog {
    final int MIN_VALUE = 10;
    final int MAX_VALUE = 255;
    final int MAX_VALUE_LENGTH = 3;
    private boolean isOk = false;
    Roberts roberts;
    public JFormattedTextField valueText;

    public RobertsDialog(Roberts roberts) {
        super();
        setLayout(new GridLayout(2, 1));
        JPanel paramsPanel = new JPanel(new GridLayout(1, 3));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.roberts = roberts;
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, roberts.getBinParameter());
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
            roberts.setBinParameter(Integer.parseInt(valueText.getText()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog)p.getRootPane().getParent();
            valueText.setText(String.valueOf(roberts.getBinParameter()));
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
