package kobtsev19203.instruments;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.dialogs.ScalerDialog;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

//    @Native public static final int TYPE_NEAREST_NEIGHBOR = 1;
//
//    /**
//     * Bilinear interpolation type.
//     */
//    @Native public static final int TYPE_BILINEAR = 2;
//
//    /**
//     * Bicubic interpolation type.
//     */
//    @Native public static final int TYPE_BICUBIC = 3;
public class Scaler implements Instrument{
    boolean isFitToScreen = true;
    ScalerDialog dialog;
    private int transformType = AffineTransformOp.TYPE_BILINEAR;
    public Scaler() {
        dialog = new ScalerDialog(this);
    }

    public BufferedImage scale(BufferedImage image, int width, int height) {
        BufferedImage toReturn;
        if(image == null) {
            return null;
        }
        toReturn = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //toReturn.getGraphics().drawImage(image, 0, 0, null);
        if (isFitToScreen) {
            int w = image.getWidth();
            int h = image.getHeight();
            if (w > width || h > height) {
                AffineTransform at = new AffineTransform();
                double scale;
                double scaleW = 1;
                double scaleH = 1;
                if (w > width) {
                    scaleW = (double) width / image.getWidth();
                }
                if (h > height) {
                    scaleH = (double) height / image.getHeight();
                }
                scale = Math.min(scaleW, scaleH);
                at.scale(scale, scale);
                AffineTransformOp scaleOp =
                        new AffineTransformOp(at, transformType);
                toReturn = new BufferedImage((int) (w * scale), (int) (h * scale), BufferedImage.TYPE_INT_ARGB);
                toReturn = scaleOp.filter(image, toReturn);
                return toReturn;
            }
            toReturn = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            toReturn.getGraphics().drawImage(image, 0, 0, null);
        } else {
            toReturn.getGraphics().drawImage(image,0,0,null);
        }
        return toReturn;
    }

    public boolean getFitToScreen() {
        return isFitToScreen;
    }

    public void setFitToScreen(boolean isFitToScreen) {
        this.isFitToScreen = isFitToScreen;
    }

    @Override
    public BufferedImage doWork(BufferedImage image) {
        return null;//XXX:хм, странно получилось
    }

    @Override
    public MyDialog getParameterDialog() {
        return dialog;
    }

    public TransformType getTransformType() {
        switch (transformType) {
            case AffineTransformOp.TYPE_BICUBIC:
                return TransformType.BICUBIC;
            case AffineTransformOp.TYPE_BILINEAR:
                return TransformType.BILINEAR;
            case AffineTransformOp.TYPE_NEAREST_NEIGHBOR:
                return TransformType.NEAREST_NEIGHBOR;
        }
        return null;
    }

    public void setTransformType(TransformType transformType) {
        switch (transformType) {
            case BICUBIC:
                this.transformType = AffineTransformOp.TYPE_BICUBIC;
                break;
            case BILINEAR:
                this.transformType = AffineTransformOp.TYPE_BILINEAR;
                break;
            case NEAREST_NEIGHBOR:
                this.transformType = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
                break;
        }

    }

    public enum TransformType {
        NEAREST_NEIGHBOR,
        BILINEAR,
        BICUBIC
    }
}