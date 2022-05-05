package kobtsev19203.instruments;

import kobtsev19203.dialogs.GammaDialog;
import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class Gamma implements Instrument {
    private double p = 1.2;
    GammaDialog dialog;
    public Gamma() {
        dialog = new GammaDialog(this);
    }
    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int R = (pixel & 0x00FF0000) >> 16; //красный
                int G = ((pixel & 0x0000FF00) >> 8); // зеленый
                int B = (pixel & 0x000000FF); // синий
                int A = (pixel & 0xFF000000) >> 24;
                double dR = (double)R/255;
                double dB = (double)B/255;
                double dG = (double)G/255;
                int resultR = (int) Math.max(255*Math.min(Math.pow(dR, p), 1), 0);//FIXME: возможно нпроверки на 0< <1 не нужны
                int resultG = (int) Math.max(255*Math.min(Math.pow(dG, p), 1), 0);
                int resultB = (int) Math.max(255*Math.min(Math.pow(dB, p), 1), 0);
                pixel = resultB | (resultG << 8) | (resultR << 16) | (A << 24);
                toReturn.setRGB(x, y, pixel);
                //System.out.print("result");
            }
        }
        return toReturn;
    }

    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }

    public double getGamme() {
        return p;
    }
    public void setGamma(double p) {
        this.p = p;
    }
}
