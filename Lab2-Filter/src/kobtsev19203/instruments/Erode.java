package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;

public class Erode implements Instrument {
    int shift = 1;
    int black = 0;
    int white = 255;
    @Override
    public BufferedImage doWork(BufferedImage image) {//����������� �� ����� ������ ����� ������ ��� ����� ������ � �����.
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int mR = (mainPixel & 0x00FF0000) >> 16; //�������
                int result = mR;
                if(mR == black) {
                    result = checkNeibors(image,x,y);
                }
                int resultPixel = result | (result << 8) | (result << 16) | (255 << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        return toReturn;
    }
    int checkNeibors(BufferedImage image, int x,int y) {
        for (int i = -shift; i <= shift; i++) {
            for (int j = -shift; j <= shift; j++) {
                if (x + i >= 0 & x + i < image.getWidth() & y + j >= 0 & y + j < image.getHeight()) {
                    int widowPixel = image.getRGB(x + i, y + j);
                    int wR = (widowPixel & 0x00FF0000) >> 16; //�������
                    if(wR != black) {
                        return white;
                    }
                }
            }
        }
        return black;
    }
    @Override
    public MyDialog getParameterDialog() {
        return null;
    }
    public void setShift(int shift) {
        this.shift = shift;
    }
    public int getShift() {
        return shift;
    }

    public int getMode() {
        return black/255;
    }
    public void setMode(int mode) {
        if(mode==0) {
            black = 255;
            white = 0;
        } else {
            black = 0;
            white = 255;
        }
    }
}
