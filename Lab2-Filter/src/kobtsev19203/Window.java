package kobtsev19203;

import kobtsev19203.dialogs.MyDialog;
import kobtsev19203.InitView.EInstrument;
import kobtsev19203.utils.FileUtils;
import kobtsev19203.utils.WindowBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Window extends WindowBase {
    InitView view;
    private JScrollPane scrollPane;
    private boolean isFitToScreenMode = false;
    private final JPanel panelWithScroll;
    MouseAdapter ma;
    String aboutText;
    private JRadioButtonMenuItem fitToScreenMenuItem;
    private JToggleButton fitToScreenButton;
    private JToggleButton blur;
    private JToggleButton roberts;
    private JToggleButton inversion;
    private JToggleButton sobel;
    private JToggleButton emboss;
    private JToggleButton gamma;
    private JToggleButton ordered;
    private JToggleButton aqua;
    private JToggleButton floyd;
    private JToggleButton grey;
    private JToggleButton otsu;
    private JToggleButton sharp;
    private JToggleButton rotate;
    private ButtonGroup effectButtons;
    private ButtonGroup effectMenu;
    private JRadioButtonMenuItem mblur;
    private JRadioButtonMenuItem mroberts;
    private JRadioButtonMenuItem minversion;
    private JRadioButtonMenuItem msobel;
    private JRadioButtonMenuItem memboss;
    private JRadioButtonMenuItem mgamma;
    private JRadioButtonMenuItem mordered;
    private JRadioButtonMenuItem maqua;
    private JRadioButtonMenuItem mfloyd;
    private JRadioButtonMenuItem mgrey;
    private JRadioButtonMenuItem motsu;
    private JRadioButtonMenuItem msharp;
    private JRadioButtonMenuItem mrotate;

    public static void main(String[] args) {
        new Window();
    }

    public Window() {
        super(800, 600, "ICGFilter");
        setMinimumSize(new Dimension(640, 480));
        try {
            effectButtons = new ButtonGroup();
            effectMenu = new ButtonGroup();
            addMenu("File");
            addMenuItem("File/Open", "open.png", "onOpen");
            addMenuItem("File/Save", "save.png", "onSave");
            addMenuItem("File/Apply", "apply.png", "onApply");
            addMenuItem("File/Exit", "exit.png", "onExit");
            addMenu("Filter");
            minversion = addMenuItem("Filter/Inversion", "invert.png", "onInverison");
            mblur = addMenuItem("Filter/Blur", "blur.png", "onBlur");
            mroberts = addMenuItem("Filter/Roberts", "roberts.png", "onRoberts");
            msobel = addMenuItem("Filter/Sobel", "sobel.png", "onSobel");
            memboss = addMenuItem("Filter/Emboss", "emboss.png", "onEmboss");
            mgamma = addMenuItem("Filter/Gamma", "gamma.png", "onGamma");
            mordered = addMenuItem("Filter/OrderedDither", "ordered.png", "onOrderedDither");
            maqua = addMenuItem("Filter/Aquarelle", "aquarelle.png", "onAquarelle");
            mfloyd = addMenuItem("Filter/FloydDither", "floyd.png", "onFloydDither");
            mgrey = addMenuItem("Filter/Grey", "grey.png", "onGrey");
            motsu = addMenuItem("Filter/Otsu", "otsu.png", "onOtsu");
            msharp = addMenuItem("Filter/Sharp", "sharp.png", "onSharp");
            addMenu("Parameters");
            addMenuItem("Parameters/Parameters", "settings.png", "onParametersButton");
            mrotate = addMenuItem("Parameters/Rotate", "rotate.png", "onRotate");
            fitToScreenMenuItem = addMenuItem("Parameters/FitToScreen", "resize.png", "onFitToScreen");
            addMenuItem("Parameters/FitSettings", "FitSettings.png", "onFitSettings");
            addMenu("Help");
            addMenuItem("Help/About", "about.png", "onAbout");

            addToolBarButton("File/Open");
            addToolBarButton("File/Save");
            addToolBarButton("File/Apply");
            addToolBarSeparator();
            sharp = addToolBarButton("Filter/Sharp");
            blur = addToolBarButton("Filter/Blur");
            emboss = addToolBarButton("Filter/Emboss");
            aqua = addToolBarButton("Filter/Aquarelle");
            addToolBarSeparator();
            inversion = addToolBarButton("Filter/Inversion");
            grey = addToolBarButton("Filter/Grey");
            gamma = addToolBarButton("Filter/Gamma");
            addToolBarSeparator();
            roberts = addToolBarButton("Filter/Roberts");
            sobel = addToolBarButton("Filter/Sobel");
            otsu = addToolBarButton("Filter/Otsu");
            addToolBarSeparator();
            ordered = addToolBarButton("Filter/OrderedDither");
            floyd = addToolBarButton("Filter/FloydDither");
            addToolBarSeparator();
            rotate = addToolBarButton("Parameters/Rotate");
            addToolBarButton("Parameters/Parameters");
            fitToScreenButton = addToolBarButton("Parameters/FitToScreen");
            fitToScreenButton.setSelected(isFitToScreenMode);
            addToolBarButton("Parameters/FitSettings");
            addToolBarSeparator();
            addToolBarButton("Help/About");

            effectMenu.add(minversion);
            effectMenu.add(mblur);
            effectMenu.add(mroberts);
            effectMenu.add(msobel);
            effectMenu.add(memboss);
            effectMenu.add(mgamma);
            effectMenu.add(mordered);
            effectMenu.add(maqua);
            effectMenu.add(mfloyd);
            effectMenu.add(mgrey);
            effectMenu.add(motsu);
            effectMenu.add(msharp);
            effectButtons.add(sharp);
            effectButtons.add(blur);
            effectButtons.add(emboss);
            effectButtons.add(aqua);
            effectButtons.add(inversion);
            effectButtons.add(grey);
            effectButtons.add(gamma);
            effectButtons.add(roberts);
            effectButtons.add(sobel);
            effectButtons.add(otsu);
            effectButtons.add(ordered);
            effectButtons.add(floyd);
            effectButtons.add(rotate);
            effectMenu.add(mrotate);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        view = new InitView();
        panelWithScroll = new JPanel();
        panelWithScroll.setLayout(new BorderLayout());
        panelWithScroll.add(view);
        add(panelWithScroll);

        setFitToScreen(isFitToScreenMode);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                view.setImageSize();
            }
        });

        pack();
        setVisible(true);
    }

    public void onAbout() {
        JOptionPane.showMessageDialog(this, aboutText, "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onExit() {
        System.exit(0);
    }

    public void onSave() {
        File file = FileUtils.getSaveImageFileName(this);
        try {
            if(file != null) {
                view.save(file);
            } else {
                System.out.println("save error");
            }
        } catch (IOException e) {
            System.out.println("save error");
        }
    }
    public void onApply() {
        view.applyChanges();
        view.setInstrument(EInstrument.NONE);
        setButton(EInstrument.NONE);
    }
    public void onGamma() {
        selectInstrument(gamma,mgamma, EInstrument.GAMMA);
    }

    public void onInverison() {
        selectInstrument(inversion,minversion, EInstrument.INVERSION);
    }
    private void selectInstrument(JToggleButton b, JRadioButtonMenuItem m, EInstrument instrument) {
        if (instrument == view.getInstrument()) {
            effectMenu.clearSelection();
            effectButtons.clearSelection();
            view.setInstrument(EInstrument.NONE);
            view.useInstrument();
            return;
        }
        boolean isOk = onParameters(instrument);
        if (isOk) {
            effectButtons.clearSelection();
            effectButtons.clearSelection();
            b.setSelected(true);
            m.setSelected(true);
            view.setInstrument(instrument);
            view.useInstrument();
        } else {
            setButton(view.getInstrument());
        }
    }
    private void setButton(EInstrument instrument) {
        effectButtons.clearSelection();
        effectMenu.clearSelection();
        switch (instrument) {
            case AQUARELLE:
                aqua.setSelected(true);
                maqua.setSelected(true);
                break;
            case BLUR:
                blur.setSelected(true);
                mblur.setSelected(true);
                break;
            case EMBOSS:
                emboss.setSelected(true);
                memboss.setSelected(true);
                break;
            case FLOYD:
                floyd.setSelected(true);
                mfloyd.setSelected(true);
                break;
            case GAMMA:
                gamma.setSelected(true);
                mgamma.setSelected(true);
                break;
            case GREY:
                grey.setSelected(true);
                mgrey.setSelected(true);
                break;
            case INVERSION:
                inversion.setSelected(true);
                minversion.setSelected(true);
                break;
            case ORDERED:
                ordered.setSelected(true);
                mordered.setSelected(true);
                break;
            case OTSU:
                otsu.setSelected(true);
                motsu.setSelected(true);
                break;
            case ROBERTS:
                roberts.setSelected(true);
                mroberts.setSelected(true);
                break;
            case ROTATE:
                rotate.setSelected(true);
                mrotate.setSelected(true);
                break;
            case SHARP:
                sharp.setSelected(true);
                msharp.setSelected(true);
                break;
            case SOBEL:
                sobel.setSelected(true);
                msobel.setSelected(true);
                break;
            case NONE:
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    public void onOtsu() {
        selectInstrument(otsu,motsu, EInstrument.OTSU);
    }

    public void onEmboss() {
        selectInstrument(emboss,memboss, EInstrument.EMBOSS);
    }

    public void onOrderedDither() {
        selectInstrument(ordered,mordered, EInstrument.ORDERED);
    }

    public void onGrey() {
        selectInstrument(grey,mgrey, EInstrument.GREY);
    }

    public void onBlur() {
        selectInstrument(blur,mblur, EInstrument.BLUR);
    }

    public void onSharp() {
        selectInstrument(sharp,msharp, EInstrument.SHARP);
    }

    public void onFloydDither() {
        selectInstrument(floyd,mfloyd, EInstrument.FLOYD);
    }

    public void onAquarelle() {
        selectInstrument(aqua,maqua, EInstrument.AQUARELLE);
    }

    public void onRoberts() {
        selectInstrument(roberts,mroberts, EInstrument.ROBERTS);
    }

    public void onSobel() {
        selectInstrument(sobel,msobel, EInstrument.SOBEL);
    }
    public void onRotate() {
        selectInstrument(rotate,mrotate, EInstrument.ROTATE);
    }
    public void onOpen() {
        File image = FileUtils.getOpenImageFileName(this);
        view.open(image);
        view.setInstrument(EInstrument.NONE);
        setButton(EInstrument.NONE);
        if (scrollPane != null) {
            scrollPane.revalidate();
        }

    }

    public boolean onParameters(MyDialog p) {
        System.out.println(p);
        if(p != null) {
            JDialog dialog = new JDialog(this,"Set parameters",true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.add((Component) p);
            dialog.pack();
            dialog.setBounds((int)(screenSize.getWidth()/2 - dialog.getWidth()/2),(int)(screenSize.getHeight()/2 - dialog.getHeight()/2),dialog.getWidth(),dialog.getHeight());
            dialog.setVisible(true);
            return p.isDialogResult();
        }
        return true;
    }

    public boolean onParameters() {
        return onParameters(view.getParametersPanel());
    }

    public void onParametersButton() {
        if(onParameters()) {
            //view.setInstrument(view.getInstrument());
            view.useInstrument();
        }
    }

    public boolean onParameters(EInstrument eInstrument) {
        return onParameters(eInstrument.getInstrument().getParameterDialog());
    }

    public void onFitToScreen() {
        isFitToScreenMode = !isFitToScreenMode;
        setFitToScreen(isFitToScreenMode);
    }
    private void setFitToScreen(boolean isFitToScreenMode) {
        fitToScreenButton.setSelected(isFitToScreenMode);
        fitToScreenMenuItem.setSelected(isFitToScreenMode);
        if (isFitToScreenMode) {
            view.showFitDialog();
            removeScrolls();
        } else {
            addScrolls();
        }
        view.setFitToScreen(isFitToScreenMode);
        panelWithScroll.setPreferredSize(panelWithScroll.getSize());
        pack();
        view.setImage();
        panelWithScroll.setPreferredSize(null);
        repaint();

    }
    public void onFitSettings() {
        view.showFitDialog();
        if(isFitToScreenMode) {
            view.setImage();
        }
    }

    private void addScrolls() {
        panelWithScroll.remove(view);
        view.setAutoscrolls(true);
        scrollPane = new JScrollPane(view);
        panelWithScroll.add(scrollPane);
        ma = new MouseAdapter() {
            private Point origin;
            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, view);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle rect = viewPort.getViewRect();
                        rect.x += deltaX;
                        rect.y += deltaY;
                        view.scrollRectToVisible(rect);
                    }
                }
            }
        };
        view.addMouseListener(ma);
        view.addMouseMotionListener(ma);
        scrollPane.revalidate();
    }
    private void removeScrolls() {
        panelWithScroll.remove(scrollPane);
        view.setAutoscrolls(false);
        view.removeMouseListener(ma);
        view.removeMouseMotionListener(ma);
        panelWithScroll.add(view);
    }
}
