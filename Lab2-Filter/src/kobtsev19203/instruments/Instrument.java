package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public interface Instrument {
    BufferedImage doWork(BufferedImage image);

    MyDialog getParameterDialog();
}
