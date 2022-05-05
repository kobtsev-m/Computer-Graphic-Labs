package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.dialogs.RobertsDialog;

import java.awt.image.BufferedImage;

public class Roberts implements Instrument{
    private int[][] matrix;
    private int k;
    int binParameter = 10;
    RobertsDialog dialog;
    public Roberts() {
        dialog = new RobertsDialog(this);
        matrix = new int[][]{
                {1, 0},
                {0, -1},
        };
        k = 1;
    }

    @Override
    public BufferedImage doWork(BufferedImage image) {
        return roberts(image);
    }
    private BufferedImage roberts(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int mR = (mainPixel & 0x00FF0000) >> 16; //красный
                int mG = ((mainPixel & 0x0000FF00) >> 8); // зеленый
                int mB = (mainPixel & 0x000000FF); // синий
                int mA = (mainPixel & 0xFF000000) >> 24;
                int mBright = (int)Math.round(0.299*mR + 0.587*mG + 0.114*mB);
                int bA = 0;
                int bB = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (x + i < image.getWidth() & y + j < image.getHeight()) {
                            int widowPixel = image.getRGB(x + i, y + j);
                            int wR = (widowPixel & 0x00FF0000) >> 16; //красный
                            int wG = ((widowPixel & 0x0000FF00) >> 8); // зеленый
                            int wB = (widowPixel & 0x000000FF); // синий
                            int wBright =(int)Math.round(0.299*wR + 0.587*wG + 0.114*wB);
                            bA += wBright * matrix[i][j];
                            bB += wBright * matrix[j][i];
                        } else {
                            bA += mBright * matrix[i][j];
                            bB += mBright * matrix[j][i];
                        }
                    }
                }
                int resultGrey = Math.min((Math.abs(bA) + Math.abs(bB)) / (k * 2), 255);
                if(resultGrey > binParameter) {//TODO:нужно ли выносить в отдельный класс?, ведь это тоже инструмент
                    resultGrey = 255;
                } else {
                    resultGrey = 0;
                }
                int resultPixel = resultGrey | (resultGrey << 8) | (resultGrey << 16) | (mA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }
    public int getBinParameter() {
        return binParameter;
    }
    public void setBinParameter(int binParameter) {
        this.binParameter = binParameter;
    }
    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }
}
