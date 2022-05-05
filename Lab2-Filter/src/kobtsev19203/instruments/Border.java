package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class Border implements Instrument {
    private int[][] SobelMatrix;
    private int[][] RobertsMatrix;
    private int k;
    private BorderType type;

    public Border(BorderType type) {
        this.type = type;
        SobelMatrix = new int[][]{
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1},
        };
        k = 4;
        RobertsMatrix = new int[][]{
                {1, 0},
                {0, -1},
        };
        k = 1;
    }

    @Override
    public BufferedImage doWork(BufferedImage image) {
        switch (type) {
            case SOBEL:
                return sobel(image);
            case ROBERTS:
                return roberts(image);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public MyDialog getParameterDialog() {
        return null;
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
                int mBright = mR + mG + mB;
                int bA = 0;
                int bB = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (x + i < image.getWidth() & y + j < image.getHeight()) {
                            int widowPixel = image.getRGB(x + i, y + j);
                            int wR = (widowPixel & 0x00FF0000) >> 16; //красный
                            int wG = ((widowPixel & 0x0000FF00) >> 8); // зеленый
                            int wB = (widowPixel & 0x000000FF); // синий
                            int wBright = wR + wG + wB;
                            bA += wBright * RobertsMatrix[i][j];
                            bB += wBright * RobertsMatrix[j][i];
                        } else {
                            bA += mBright * RobertsMatrix[i][j];
                            bB += mBright * RobertsMatrix[j][i];
                        }
                    }
                }
                int resultGrey = Math.min((Math.abs(bA) + Math.abs(bB)) / (k * 2), 255);
                int resultPixel = resultGrey | (resultGrey << 8) | (resultGrey << 16) | (mA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }

    private BufferedImage sobel(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        toReturn.getGraphics().drawImage(image, 0, 0, null);
        final int shift = SobelMatrix.length / 2;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int mR = (mainPixel & 0x00FF0000) >> 16; //красный
                int mG = ((mainPixel & 0x0000FF00) >> 8); // зеленый
                int mB = (mainPixel & 0x000000FF); // синий
                int mA = (mainPixel & 0xFF000000) >> 24;
                int mBright = mR + mG + mB;
                int bA = 0;
                int bB = 0;
                for (int i = -shift; i <= shift; i++) {
                    for (int j = -shift; j <= shift; j++) {
                        if (x + i >= 0 & x + i < image.getWidth() & y + j >= 0 & y + j < image.getHeight()) {
                            int widowPixel = image.getRGB(x + i, y + j);
                            int wR = (widowPixel & 0x00FF0000) >> 16; //красный
                            int wG = ((widowPixel & 0x0000FF00) >> 8); // зеленый
                            int wB = (widowPixel & 0x000000FF); // синий
                            int wBright = wR + wG + wB;
                            bA += wBright * SobelMatrix[i + shift][j + shift];
                            bB += wBright * SobelMatrix[j + shift][i + shift];
                        } else {
                            bA += mBright * SobelMatrix[i + shift][j + shift];
                            bB += mBright * SobelMatrix[j + shift][i + shift];
                        }
                    }
                }
                int resultGrey = Math.min((Math.abs(bA) + Math.abs(bB)) / (k * 2), 255);
                int resultPixel = resultGrey | (resultGrey << 8) | (resultGrey << 16) | (mA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }

    public enum BorderType {SOBEL, ROBERTS}
}
