package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.Aquarelle;

import javax.swing.*;
import java.awt.*;

public class AquarelleDialog  extends JPanel implements MyDialog {
    final int MIN_VALUE = 1;
    final int MAX_VALUE = 100;
    final int MAX_VALUE_LENGTH = 3;
    public JFormattedTextField valueText;
    private boolean isOk = false;
    Aquarelle aquarelle;
    public AquarelleDialog(Aquarelle aquarelle) {
        super();
        setLayout(new GridLayout(2, 1));
        JPanel paramsPanel = new JPanel(new GridLayout(1, 3));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.aquarelle = aquarelle;
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, aquarelle.getValue());
        paramsPanel.add(new Label("parameter:"));
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
            aquarelle.setValue(Integer.parseInt(valueText.getText()));
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog)p.getRootPane().getParent();
            valueText.setText(String.valueOf(aquarelle.getValue()));
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
