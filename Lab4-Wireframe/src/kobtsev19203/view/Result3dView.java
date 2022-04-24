package kobtsev19203.view;

import kobtsev19203.state.State;
import kobtsev19203.math.Matrix;
import kobtsev19203.math.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Result3dView extends JPanel {
    private static final int DEFAULT_X_ANGLE = 125;
    private static final int DEFAULT_Y_ANGLE = 125;
    private static final int DEFAULT_Z_ANGLE = 235;
    private Matrix RoxFirst;
    private Matrix Rox;
    private Matrix Roz;
    private Matrix Roy;
    private Matrix Rsum;
    private int prevX = -1;
    private int prevY = -1;
    private double angleX = DEFAULT_X_ANGLE;
    private double angleZ = DEFAULT_Z_ANGLE;
    private double angleY = DEFAULT_Y_ANGLE;
    private State state;

    public Result3dView(State state) {
        this.state = state;
        this.setPreferredSize(new Dimension(600, 600));
        Result3dView t = this;
        addMouseWheelListener((event) -> {
            double coef = Math.pow(1.1, event.getWheelRotation());
            t.state.setZf(t.state.getZf() * coef);
            t.state.setZb(t.state.getZb() * coef);
            repaint();
        });

        MouseListener ml = new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        addMouseListener(ml);

        MouseMotionListener mml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dy = 0;
                int dx = 0;
                int dz = 0;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dy = e.getY() - prevY;
                    dx = e.getX() - prevX;

                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    dz = e.getY() - prevY + (e.getX() - prevX);
                }
                Rox = Matrix.getRx(Math.toRadians((dy)) * 2 / 3);
                Roy = Matrix.getRy(Math.toRadians((-dx)) * 2 / 3);
                Roz = Matrix.getRz(Math.toRadians(dz) * 2 / 3);
                Rsum = Rox.mul(Roy).mul(Roz).mul(Rsum);
                prevX = e.getX();
                prevY = e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        };
        addMouseMotionListener(mml);
        resetAngles();
    }

    public void resetAngles() {
        angleX = DEFAULT_X_ANGLE;
        angleY = DEFAULT_Y_ANGLE;
        angleZ = DEFAULT_Z_ANGLE;
        RoxFirst = Matrix.getRx(0);
        calcAngles();
        repaint();
    }

    private void calcAngles() {
        Rox = Matrix.getRx(Math.toRadians(angleX));
        Roy = Matrix.getRy(Math.toRadians(angleY));
        Roz = Matrix.getRz(Math.toRadians(angleZ));
        Rsum = Rox.mul(Roy).mul(Roz).mul(RoxFirst);
    }

    private Matrix getTransformedMatrix() {
        double zoom = 10;
        Matrix cameraBasis = new Matrix(4, 4, new double[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, zoom,
            0, 0, 0, 1
        });
        Matrix Mpsp = Matrix.getMproj(state.getSw(), state.getSh(), state.getZf(), state.getZb());
        return Mpsp.mul(cameraBasis).mul(Rsum);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D gi = (Graphics2D) image.getGraphics();
        gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gi.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gi.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        gi.setColor(Color.white);
        gi.fillRect(0, 0, image.getWidth(), image.getHeight());
        gi.setColor(Color.black);
        Matrix T = getTransformedMatrix();
        double degBetweenWires = 360.0 / state.getM();
        for (int j = 0; j < state.getM(); j++) {
            double deg = j * degBetweenWires;
            Matrix Rz = Matrix.getRz(Math.toRadians(deg));
            Matrix R = T.mul(Rz);
            for (int i = 0; i < state.getSplinePoints().size() - 1; i++) {
                Vector4 p1 = state.getSplinePoints().get(i);
                Vector4 p2 = state.getSplinePoints().get(i + 1);
                Vector4 result1 = R.mul(p1);
                Vector4 result2 = R.mul(p2);
                result1.normalizeByLastPoint();
                result2.normalizeByLastPoint();
                int x1 = (int) result1.getMatrix()[0][0];
                int y1 = (int) result1.getMatrix()[1][0];
                int x2 = (int) result2.getMatrix()[0][0];
                int y2 = (int) result2.getMatrix()[1][0];
                gi.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
            }
        }
        degBetweenWires = 360.0 / (state.getCircleN() * state.getM());
        List<List<Vector4>> circles = new ArrayList<>();

        for (int i = 0; i < state.getCircleM(); i++) {
            List<Vector4> circle = new ArrayList<>();
            circles.add(circle);
        }

        addWirePointsToCircles(T, circles);

        for (int i = 1; i < state.getCircleN() * state.getM(); ++i) {
            double deg = i * degBetweenWires;
            Matrix Rz = Matrix.getRz(Math.toRadians(deg));
            Matrix R = T.mul(Rz);
            addWirePointsToCircles(R, circles);
        }

        for (int i = 0; i < circles.size(); ++i) {
            for (int j = 0; j < circles.get(i).size() - 1; j++) {
                Vector4 p1 = circles.get(i).get(j);
                Vector4 p2 = circles.get(i).get(j + 1);
                int x1 = (int) p1.getMatrix()[0][0];
                int y1 = (int) p1.getMatrix()[1][0];
                int x2 = (int) p2.getMatrix()[0][0];
                int y2 = (int) p2.getMatrix()[1][0];
                gi.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
            }
            Vector4 p1 = circles.get(i).get(circles.get(i).size() - 1);
            Vector4 p2 = circles.get(i).get(0);
            int x1 = (int) p1.getMatrix()[0][0];
            int y1 = (int) p1.getMatrix()[1][0];
            int x2 = (int) p2.getMatrix()[0][0];
            int y2 = (int) p2.getMatrix()[1][0];
            gi.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
        }
        g.drawImage(image, 0, 0, null);
    }

    private void addWirePointsToCircles(Matrix R, List<List<Vector4>> circles) {
        if (circles.isEmpty()) {
            return;
        }
        double wiresBetweenCircles = (double) state.getSplinePoints().size() / state.getCircleM();
        for (int i = 0; i < circles.size() - 1; i += 1) {
            int sn = (int) Math.round(i * wiresBetweenCircles);
            Vector4 v = state.getSplinePoints().get(sn);
            v = R.mul(v);
            v.normalizeByLastPoint();
            circles.get(i).add(v);
        }
        Vector4 v = state.getSplinePoints().get(state.getSplinePoints().size() - 1);
        v = R.mul(v);
        v.normalizeByLastPoint();
        circles.get(circles.size() - 1).add(v);
    }

    public void setState(State state) {
        this.state = state;
        repaint();
    }
}