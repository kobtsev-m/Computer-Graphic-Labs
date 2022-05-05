package kobtsev19203.instruments;

import kobtsev19203.dialogs.FloydDitherDialog;
import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class FloydDither implements Instrument {
    FloydDitherDialog dialog;
    int[] rgbCount;

    public FloydDither() {
        rgbCount = new int[3];
        rgbCount[0] =  10;
        rgbCount[1] =  10;
        rgbCount[2] =  10;
        dialog = new FloydDitherDialog(this);
    }

    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        toReturn.getGraphics().drawImage(image, 0, 0, null);
        int[] err = new int[3];
        int[] newRGB = new int[3];
        for (int y = 0; y < toReturn.getHeight(); y++) {
            for (int x = 0; x < toReturn.getWidth(); x++) {
                int pixel = toReturn.getRGB(x, y);
                setNearest(toReturn, x, y, pixel, err, newRGB);
            }
        }
        return toReturn;
    }

    private void setNearest(BufferedImage image, int x, int y, int rgba, int[] err, int[] newRGB) {
        int[] origrgb = new int[4];
        getrgba(rgba, origrgb);
        int[] newrgb = new int[4];
        for (int i = 0; i < 3; i++) {
            newrgb[i] = getNearest(origrgb[i], rgbCount[i]);
            err[i] = origrgb[i] - newrgb[i];
            spreadError(image, x, y, err);
        }
        newrgb[3] = 0;
        image.setRGB(x, y, arrToIntRGBA(newrgb));
    }

    private void getrgba(int pixel, int[] rgba) {
        rgba[0] = (pixel & 0x00FF0000) >> 16;
        rgba[1] = ((pixel & 0x0000FF00) >> 8);
        rgba[2] = (pixel & 0x000000FF);
        rgba[3] = (pixel & 0xFF000000) >> 24;
    }

    int getNearest(int color, int quantumCount) {
        return (int) Math.round((Math.round((quantumCount - 1) * color / 255.0) * (255.0 / (quantumCount - 1))));
    }

    private void spreadError(BufferedImage image, int x, int y, int[] err) {
        int origrgb;
        int[] orig = new int[4];
        if (x < image.getWidth() - 1) {
            origrgb = image.getRGB(x + 1, y);
            getrgba(origrgb, orig);
            image.setRGB(x + 1, y, addErr(orig, err, 7));
        }
        if (y < image.getHeight() - 1) {
            if (x > 0) {
                origrgb = image.getRGB(x - 1, y + 1);
                getrgba(origrgb, orig);
                image.setRGB(x - 1, y + 1, addErr(orig, err, 3));
            }
            origrgb = image.getRGB(x, y + 1);
            getrgba(origrgb, orig);
            image.setRGB(x, y + 1, addErr(orig, err, 5));
            if (x < image.getWidth() - 1) {
                origrgb = image.getRGB(x + 1, y + 1);
                getrgba(origrgb, orig);
                image.setRGB(x + 1, y + 1, addErr(orig, err, 1));
            }
        }
    }

    private int addErr(int[] orig, int[] err, int coef) {
        for (int i = 0; i < err.length; i++) {
            orig[i] = Math.max(Math.min(orig[i] + err[i] * coef / 16, 255), 0);
        }
        return arrToIntRGBA(orig);
    }

    private int arrToIntRGBA(int[] RGBA) {
        return RGBA[2] | (RGBA[1] << 8) | (RGBA[0] << 16) | (RGBA[3] << 24);
    }


    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }

    public int getBlue() {
        return rgbCount[0];
    }

    public int getGreen() {
        return rgbCount[1];
    }

    public int getRed() {
        return rgbCount[2];
    }

    public void setBlue(int blueCount) {
        rgbCount[0] = blueCount;

    }

    public void setGreen(int greenCount) {
        rgbCount[1] = greenCount;
    }

    public void setRed(int redCount) {
        rgbCount[2] = redCount;
    }
}
