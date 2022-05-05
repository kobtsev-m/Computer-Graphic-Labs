package kobtsev19203.instruments;

import kobtsev19203.dialogs.BlurDialog;
import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class Blur implements Instrument {
    private int[][] matrix;
    private int k;
    private int matrixSize = 5;
    private BlurDialog blurDialog;

    public Blur() {
        this.blurDialog = new BlurDialog(this);
        setMatrix();
    }
    //TODO: здесь и в подобных алгоритмах возможна оптимизация. Сумму 6 левых значений можно использовать на следующем шаге.
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
                resultR = Math.min(resultR / k, 255);
                resultG = Math.min(resultG / k, 255);
                resultB = Math.min(resultB / k, 255);
                int resultPixel = resultB | (resultG << 8) | (resultR << 16) | (resultA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }

    @Override
    public MyDialog getParameterDialog() {
        return blurDialog;
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public void setMatrixSize(int matrixSize) {
        this.matrixSize = matrixSize;
        setMatrix();
    }

    private void setMatrix() {
        if (matrixSize == 3) {
            matrix = new int[][]{
                    {1, 1, 1},
                    {1, 2, 1},
                    {1, 1, 1},
            };
            k = 10;
        } else if (matrixSize == 5) {
            matrix = new int[][]{
                    {1, 2, 3, 2, 1},
                    {2, 4, 5, 4, 2},
                    {3, 5, 6, 5, 3},
                    {2, 4, 5, 4, 2},
                    {1, 2, 3, 2, 1},
            };
            k = 74;
        } else {
            matrix = new int[matrixSize][matrixSize];
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    matrix[i][j] = 1;
                }
            }
            k = matrixSize*matrixSize;
        }
    }
}
