package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.Rotate;

import javax.swing.*;
import java.awt.*;

public class RotateDialog extends JPanel implements MyDialog {
    final int MIN_VALUE = -180;
    final int MAX_VALUE = 180;
    final int MAX_VALUE_LENGTH = 4;
    private boolean isOk = false;
    JTextField valueText;
    Rotate rotate;

    public RotateDialog(Rotate rotate) {
        super();
        setLayout(new GridLayout(2, 1));
        JPanel paramsPanel = new JPanel(new GridLayout(1, 3));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.rotate = rotate;
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, rotate.getAngle());
        paramsPanel.add(new Label("Angle:"));
        valueText = ts.text;
        paramsPanel.add(valueText);
        paramsPanel.add(ts.slider);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel p = this;
        okButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            rotate.setAngle(Integer.parseInt(valueText.getText()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            valueText.setText(String.valueOf(rotate.getAngle()));
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