package kobtsev19203;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.instruments.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Component which draws two diagonal lines and reacts on mouse clicks
 *
 * @author Alexander Borzov
 */
public class InitView extends JPanel {
    private int shift = 4;
    private BufferedImage imageToPaint;
    private BufferedImage imageWithEffect;
    private BufferedImage originalImage;
    private Instrument instrument;
    static private Gamma gamma = new Gamma();
    static private Grey grey = new Grey();
    static private Scaler scaler = new Scaler();
    static private Inversion inversion = new Inversion();
    static private Blur blur = new Blur();
    static private Sharp sharp = new Sharp();
    static private Emboss emboss = new Emboss();
    static private OrderedDither orderedDither = new OrderedDither();
    static private Aquarelle aquarelle = new Aquarelle();
    static private FloydDither floydDither = new FloydDither();
    static private Sobel sobel = new Sobel();
    static private Roberts roberts = new Roberts();
    static private Rotate rotate = new Rotate();
    static private Otsu otsu = new Otsu();
    static private None none = new None();
    static private EInstrument einstrument;
    private int width = 800;
    private int height = 600;
    private boolean isInEffect = false;
    private JLabel label;
    /**
     * Constructs object
     */
    public InitView() {
        instrument = null;
        einstrument = EInstrument.NONE;
        imageWithEffect = null;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onMouseClick();
            }
        });
    }

    /**
     * Performs actual component drawing
     *
     * @param g Graphics object to draw component to
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        float[] dash1 = {2f, 0f, 2f};
        BasicStroke bs1 = new BasicStroke(1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND,
                1.0f,
                dash1,
                2f);
        g2d.setStroke(bs1);
        if(isInEffect) {
            imageToPaint = scaler.scale(imageWithEffect, width, height);
        } else {
            imageToPaint =  scaler.scale(originalImage, width, height);
        }
        if (imageToPaint != null) {
            g2d.drawRect(shift, shift, imageToPaint.getWidth() + 1, imageToPaint.getHeight() + 1);
            //g.drawImage(imageToPaint, shift + 1, shift + 1, null);
			/*
			double scale = 1;
            double scaleW = 1;
            double scaleH = 1;
            if(w > width) {
                scaleW = (double)width/ imageWithEffect.getWidth();
            }
            if(h > height){
                scaleH = (double)height/ imageWithEffect.getHeight();
            }
            scale = Math.min(scaleW,scaleH);
            width = width*scale;
            height = height*scale;
            g.drawImage(imageToPaint,shift + 1,shift + 1,width - 2 * shift, height - 2 * shift,null);
			}*///другой вариант изменения размера
            g.drawImage(imageToPaint, shift + 1, shift + 1, null);
        } else {
            g2d.drawRect(shift, shift, getWidth() - 2 * shift, getHeight() - 2 * shift);
        }
    }

    private void openError() {
        JOptionPane.showMessageDialog(this, "file open error",
                "ERROR",
                JOptionPane.ERROR_MESSAGE);
    }

    public void open(File file) {
        BufferedImage newImage;
        if (file == null) {
            return;
        }
        try {
            newImage = ImageIO.read(file);
        } catch (IOException e) {
            openError();
            return;
        }
        if (newImage == null) {
            openError();
            return;
        }
        isInEffect = false;
        originalImage = newImage;
        setImage();
    }
    public void setImage(){
        width = getWidth() - 2 * shift - 2;
        height = getHeight() - 2 * shift - 2;
        paintImage();
        if(!scaler.getFitToScreen() && originalImage!=null) {
            setPreferredSize(new Dimension(originalImage.getWidth() + 2*shift + 1, originalImage.getHeight() + 2*shift + 1));
        }
    }
    void paintImage() {
        if (isInEffect) {
            imageToPaint = scaler.scale(imageWithEffect, width, height);
        } else {
            if (originalImage == null) {
                return;
            }
            imageToPaint = scaler.scale(originalImage, width, height);
        }
        repaint();
    }

    private void onMouseClick() {
        if (!isInEffect) {
            if (imageWithEffect == null) {
                if (instrument != null && originalImage != null) {
                    imageWithEffect = instrument.doWork(originalImage);
                }
            }
        }
        isInEffect = !isInEffect;
        paintImage();
    }

    public void useInstrument() {
        if (instrument != null && originalImage != null) {
            imageWithEffect = instrument.doWork(originalImage);
            isInEffect = true;
        }
        paintImage();
    }

    public void setInstrument(EInstrument einstrument) {
        isInEffect = false;
        imageWithEffect = null;
        InitView.einstrument = einstrument;
        instrument = einstrument.getInstrument();
    }

    public EInstrument getInstrument() {
        return einstrument;
    }

    public MyDialog getParametersPanel() {
        return instrument.getParameterDialog();
    }
    public void setImageSize() {
        if(scaler.getFitToScreen() && originalImage != null) {
            this.width = getWidth() - 2*shift - 2;
            this.height = getHeight() - 2*shift - 2;
            paintImage();
        }
    }

    public void setFitToScreen(boolean isFitToScreenMode) {
        scaler.setFitToScreen(isFitToScreenMode);
        setImage();
    }

    public void showFitDialog() {
        JOptionPane.showConfirmDialog(null, scaler.getParameterDialog(), "Set parameters", JOptionPane.DEFAULT_OPTION);
    }

    public void applyChanges() {
        if(isInEffect & imageWithEffect != originalImage) {
            originalImage = imageWithEffect;
        }
        isInEffect = false;
    }

    public void save(File image) throws IOException {
        if(isInEffect) {
            ImageIO.write(imageWithEffect, "png" , image);
        } else if(originalImage != null) {
            ImageIO.write(originalImage, "png" , image);
        } else {
            JOptionPane.showMessageDialog(this, "no image",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    enum EInstrument{
        AQUARELLE(aquarelle),
        BLUR(blur),
        EMBOSS(emboss),
        FLOYD(floydDither),
        GAMMA(gamma),
        GREY(grey),
        INVERSION(inversion),
        ORDERED(orderedDither),
        OTSU(otsu),
        ROBERTS(roberts),
        ROTATE(rotate),
        SHARP(sharp),
        SOBEL(sobel),
        NONE(none);
        Instrument instrument;
        EInstrument(Instrument instrument) {
            this.instrument = instrument;
        }
        public Instrument getInstrument() {
            return instrument;
        }
    }
}
