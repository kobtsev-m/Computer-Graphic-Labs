package kobtsev19203.dialogs;

import kobtsev19203.dialogs.TextAndSlider.TextAndSlider;
import kobtsev19203.dialogs.TextAndSlider.TextAndSliderCreator;
import kobtsev19203.instruments.Otsu;

import javax.swing.*;
import java.awt.*;

public class OtsuDialog extends JPanel implements MyDialog {
    final int MIN_VALUE = 1;
    final int MAX_VALUE = 9;
    final int MAX_VALUE_LENGTH = 1;
    private boolean isOk = false;
    Otsu otsu;
    JTextField erodeText;
    JTextField dilateText;

    /*JRadioButton blackEroseButton;
    JRadioButton whiteEroseButton;
    JRadioButton blackDilateButton;
    JRadioButton whiteDilateButton;*/
    public OtsuDialog(Otsu otsu) {
        super();
        this.otsu = otsu;
        setLayout(new BorderLayout());
        JPanel paramsPanel = new JPanel(new GridLayout(3, 4));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //GroupLayout layout = new GroupLayout(this);
        //setLayout(layout);
        TextAndSlider ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, otsu.dilate.getShift());
        paramsPanel.add(new Label("Operation parameter"));
        paramsPanel.add(new Label("Binarization"));
        paramsPanel.add(new Label(""));
        paramsPanel.add(new Label("Mode:"));

        paramsPanel.add(new Label("Dilate"));
        dilateText = ts.text;
        paramsPanel.add(dilateText);
        paramsPanel.add(ts.slider);
        ButtonGroup dilateMode = new ButtonGroup();
        JRadioButton blackDilateButton= new JRadioButton("black", false);
        JRadioButton whiteDilateButton= new JRadioButton("white", false);
        dilateMode.add(blackDilateButton);
        dilateMode.add(whiteDilateButton);
        JPanel dialoteModePanel = new JPanel();
        dialoteModePanel.add(blackDilateButton);
        dialoteModePanel.add(whiteDilateButton);
        paramsPanel.add(dialoteModePanel);

        paramsPanel.add(new Label("Erode:"));
        ts = TextAndSliderCreator.createDefault(MIN_VALUE, MAX_VALUE, MAX_VALUE_LENGTH, otsu.erode.getShift());
        erodeText = ts.text;
        paramsPanel.add(erodeText);
        paramsPanel.add(ts.slider);
        ButtonGroup eroseMode = new ButtonGroup();
        JRadioButton blackEroseButton= new JRadioButton("black", false);
        JRadioButton whiteEroseButton= new JRadioButton("white", false);
        eroseMode.add(blackEroseButton);
        eroseMode.add(whiteEroseButton);
        JPanel eroseModePanel = new JPanel();
        eroseModePanel.add(blackEroseButton);
        eroseModePanel.add(whiteEroseButton);
        paramsPanel.add(eroseModePanel);
        if(otsu.erode.getMode() == 0) {
            blackEroseButton.setSelected(true);
        } else {
            whiteEroseButton.setSelected(true);
        }
        if(otsu.dilate.getMode() == 0) {
            blackDilateButton.setSelected(true);
        } else {
            whiteDilateButton.setSelected(true);
        }
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel p = this;
        okButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            otsu.dilate.setShift(Integer.parseInt(dilateText.getText()));
            otsu.erode.setShift(Integer.parseInt(erodeText.getText()));
            if(blackDilateButton.isSelected()) {
                otsu.dilate.setMode(0);
            } else {
                otsu.dilate.setMode(1);
            }
            if(blackEroseButton.isSelected()) {
                otsu.erode.setMode(0);
            } else {
                otsu.erode.setMode(1);
            }
            isOk = true;
            frame.dispose();
        });
        cancelButton.addActionListener(e -> {
            JDialog frame = (JDialog) p.getRootPane().getParent();
            erodeText.setText(String.valueOf(otsu.erode.getShift()));
            dilateText.setText(String.valueOf(otsu.dilate.getShift()));
            if(otsu.erode.getMode() == 0) {
                blackEroseButton.setSelected(true);
            } else {
                whiteEroseButton.setSelected(true);
            }
            if(otsu.dilate.getMode() == 0) {
                blackDilateButton.setSelected(true);
            } else {
                whiteDilateButton.setSelected(true);
            }
            isOk = false;
            frame.dispose();
        });
        add(paramsPanel,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
    }

    @Override
    public boolean isDialogResult() {
        return isOk;
    }
}
