package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class None implements Instrument {
    @Override
    public BufferedImage doWork(BufferedImage image) {
        return image;
    }

    @Override
    public MyDialog getParameterDialog() {
        return null;
    }
}
