package kobtsev19203.dialogs;

import kobtsev19203.instruments.Scaler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ScalerDialog extends JPanel implements MyDialog {
    Scaler scaler;
    public ScalerDialog(Scaler scaler) {
        this.scaler = scaler;
        String[] items = {
                "NEAREST NEIGHBOR",
                "BILINEAR",
                "BICUBIC"
        };
        JComboBox comboBox = new JComboBox(items);
        comboBox.setSelectedIndex(2);
        //XXX:тут какая-то подозрительная копипаста
        ActionListener actionListener = e -> {
            JComboBox box = (JComboBox)e.getSource();
            String item = (String)box.getSelectedItem();
            switch (Objects.requireNonNull(item)) {
                case "BICUBIC":
                    scaler.setTransformType(Scaler.TransformType.BICUBIC);
                    break;
                case "BILINEAR":
                    scaler.setTransformType(Scaler.TransformType.BILINEAR);
                    break;
                case "NEAREST NEIGHBOR":
                    scaler.setTransformType(Scaler.TransformType.NEAREST_NEIGHBOR);
                    break;
            }
        };
        comboBox.addActionListener(actionListener);
        add(comboBox);
    }

    @Override
    public boolean isDialogResult() {
        return true;
    }
}
