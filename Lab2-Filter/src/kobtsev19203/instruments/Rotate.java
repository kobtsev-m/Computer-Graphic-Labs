package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.dialogs.RotateDialog;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rotate implements Instrument {
    private int angle = 35;
    RotateDialog dialog;
    public Rotate(){
        dialog = new RotateDialog(this);
    }
    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        double radians = angle * Math.PI / 180.;
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        final int WHITE = Color.WHITE.getRGB();
        for(int y = 0; y < image.getHeight(); ++y) {
            for(int x = 0; x < image.getWidth(); ++x) {
                int srcX = centerX + (int) ((x - centerX) * cos + (y - centerY) * sin);
                int srcY = centerY+ (int) (-(x - centerX) * sin + (y - centerY) * cos);

                if (srcX < 0 || srcX >= image.getWidth() || srcY < 0 || srcY >= image.getHeight()) {
                    toReturn.setRGB(x, y, WHITE);
                    continue;
                }
                toReturn.setRGB(x, y, image.getRGB(srcX, srcY));
            }
        }
        return toReturn;
    }

    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }
    public int getAngle() {
        return angle;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
}
