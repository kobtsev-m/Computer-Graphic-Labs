package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.dialogs.OtsuDialog;

import java.awt.image.BufferedImage;

public class Otsu implements Instrument {
    private final int cSize = 256;
    public Dilate dilate = new Dilate();
    public Erode erode = new Erode();
    OtsuDialog dialog;
    public Otsu() {
        dialog = new OtsuDialog(this);
    }
    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int pixelCount = image.getHeight() * image.getWidth();
        int intensitySum = calculateIntensitySum(image);
        int[] hist = calculateHist(image);
        int bestT = 0;
        double bestSigma = 0.0;

        int w1 = 0;
        int u1 = 0;

        for (int thresh = 0; thresh < cSize - 1; ++thresh) {
            w1 += hist[thresh];
            u1 += thresh * hist[thresh];

            double first_class_prob = w1 / (double) pixelCount;
            double second_class_prob = 1.0 - first_class_prob;

            double first_class_mean = u1 / (double) w1;
            double second_class_mean = (intensitySum - u1)
                    / (double) (pixelCount - w1);

            double mean_delta = first_class_mean - second_class_mean;

            double sigma = first_class_prob * second_class_prob * mean_delta * mean_delta;

            if (sigma > bestSigma) {
                bestSigma = sigma;
                bestT = thresh;
            }
        }


        int binParameter = bestT;
        System.out.println(binParameter);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int mR = (mainPixel & 0x00FF0000) >> 16; //�������
                int mG = ((mainPixel & 0x0000FF00) >> 8); // �������
                int mB = (mainPixel & 0x000000FF); // �����
                int mA = (mainPixel & 0xFF000000) >> 24;
                int mBright = (int) Math.round(0.299 * mR + 0.587 * mG + 0.114 * mB);
                if (mBright > binParameter) {//TODO:����� �� �������� � ��������� �����?, ���� ��� ���� ����������
                    mBright = 255;
                } else {
                    mBright = 0;
                }
                int resultPixel = mBright | (mBright << 8) | (mBright << 16) | (mA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        //for(int i=0;i<count)
        return dilate.doWork(erode.doWork(toReturn));
        //return toReturn;
    }

    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }

    int calculateIntensitySum(BufferedImage image) {
        int sum = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int R = (pixel & 0x00FF0000) >> 16; //�������
                int G = ((pixel & 0x0000FF00) >> 8); // �������
                int B = (pixel & 0x000000FF); // �����
                int A = (pixel & 0xFF000000) >> 24;
                int grey = (int) Math.round(0.299 * R + 0.587 * G + 0.114 * B);
                sum += grey;
            }
        }
        return sum;
    }

    int[] calculateHist(BufferedImage image) {
        int[] hist = new int[cSize];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int R = (pixel & 0x00FF0000) >> 16; //�������
                int G = ((pixel & 0x0000FF00) >> 8); // �������
                int B = (pixel & 0x000000FF); // �����
                int A = (pixel & 0xFF000000) >> 24;
                int grey = (int) Math.round(0.299 * R + 0.587 * G + 0.114 * B);
                hist[grey] += 1;
            }
        }
        return hist;
    }
}