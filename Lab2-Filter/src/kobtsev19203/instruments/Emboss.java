package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class Emboss implements Instrument{
    private int[][] matrix;
    private int k;

    public Emboss() {
        matrix = new int[][]{
                {0, 1, 0},
                {1, 0, -1},
                {0, -1, 0},
        };
        k = 1;
    }

    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int mR = (mainPixel & 0x00FF0000) >> 16; //красный
                int mG = ((mainPixel & 0x0000FF00) >> 8); // зеленый
                int mB = (mainPixel & 0x000000FF); // синий
                int resultR = 0;
                int resultG = 0;
                int resultB = 0;
                int resultA = (mainPixel & 0xFF000000) >> 24;
                for (int i = -(matrix.length / 2); i <= matrix.length / 2; i++) {
                    for (int j = -(matrix.length / 2); j <= matrix.length / 2; j++) {
                        if (x + i >= 0 & x + i < image.getWidth() & y + j >= 0 & y + j < image.getHeight()) {
                            int widowPixel = image.getRGB(x + i, y + j);
                            int wR = (widowPixel & 0x00FF0000) >> 16; //красный
                            int wG = ((widowPixel & 0x0000FF00) >> 8); // зеленый
                            int wB = (widowPixel & 0x000000FF); // синий
                            resultR += wR * matrix[i + matrix.length / 2][j + matrix.length / 2];
                            resultG += wG * matrix[i + matrix.length / 2][j + matrix.length / 2];
                            resultB += wB * matrix[i + matrix.length / 2][j + matrix.length / 2];
                        } else {
                            resultR += mR * matrix[i + matrix.length / 2][j + matrix.length / 2];
                            resultG += mG * matrix[i + matrix.length / 2][j + matrix.length / 2];
                            resultB += mB * matrix[i + matrix.length / 2][j + matrix.length / 2];
                        }
                    }
                }
                resultB+=128;
                resultG+=128;
                resultR+=128;
                resultR = Math.min(resultR / k , 255);
                resultG = Math.min(resultG / k, 255);
                resultB = Math.min(resultB / k, 255);
                //System.out.println("bR :" + resultR + "G :" + resultG +  "B :" + resultB);
                resultR = Math.max(resultR, 0);
                resultG = Math.max(resultG, 0);
                resultB = Math.max(resultB, 0);
                //System.out.println("pR :" + resultR + "G :" + resultG +  "B :" + resultB);
                //int result = (resultR + resultB + resultG)/3;
                int resultPixel = resultB | (resultG << 8) | (resultR << 16) | (resultA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }

    @Override
    public MyDialog getParameterDialog() {
        return null;
    }
}