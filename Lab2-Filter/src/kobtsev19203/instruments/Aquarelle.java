package kobtsev19203.instruments;

import kobtsev19203.dialogs.AquarelleDialog;
import kobtsev19203.dialogs.MyDialog;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aquarelle implements Instrument {
    private int size = 3;
    AquarelleDialog dialog;
    public Aquarelle() {
        dialog = new AquarelleDialog(this);
    }
    @Override
    public BufferedImage doWork(BufferedImage image) {
        BufferedImage toReturn = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int mainPixel = image.getRGB(x, y);
                int resultA = (mainPixel & 0xFF000000) >> 24;
                List<Integer> RpixelList = new ArrayList<>();
                List<Integer> GpixelList = new ArrayList<>();
                List<Integer> BpixelList = new ArrayList<>();
                for (int i = -(size / 2); i <= size / 2; i++) {
                    for (int j = -(size / 2); j <= size / 2; j++) {
                        if (x + i >= 0 & x + i < image.getWidth() & y + j >= 0 & y + j < image.getHeight()) {
                            int widowPixel = image.getRGB(x + i, y + j);
                            int wR = (widowPixel & 0x00FF0000) >> 16; //красный
                            int wG = ((widowPixel & 0x0000FF00) >> 8); // зеленый
                            int wB = (widowPixel & 0x000000FF); // синий
                            RpixelList.add(wR);
                            GpixelList.add(wG);
                            BpixelList.add(wB);
                        }
                    }
                }

                int resultR = getMedian(RpixelList);
                int resultG = getMedian(GpixelList);
                int resultB = getMedian(BpixelList);
                int resultPixel = resultB | (resultG << 8) | (resultR << 16) | (resultA << 24);
                toReturn.setRGB(x, y, resultPixel);
            }
        }
        Sharp s = new Sharp();
        return s.doWork(toReturn);
    }

    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }

    private int getMedian(List<Integer> list) {
        /*int size = list.size();
        int max = list.get(0);
        for (int i = 0; i < size / 2; i++) {
            int indexMax = 0;
            int index = 0;
            max = list.get(0);
            for (int elem : list) {
                if (elem > max) {
                    max = elem;
                    indexMax = index;
                }
                index += 1;
            }
            list.remove(indexMax);
        }
        return max;
        */
        Collections.sort(list);
        return list.get(list.size()/2);
    }

    public void setValue(int size) {
        this.size = size;
    }

    public int getValue() {
        return size;
    }
}
