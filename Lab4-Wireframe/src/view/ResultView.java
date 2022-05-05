package view;

import state.State;
import math.Matrix;
import math.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ResultView extends JPanel {
    private static final int DEFAULT_X_ANGLE = 125;
    private static final int DEFAULT_Y_ANGLE = 125;
    private static final int DEFAULT_Z_ANGLE = 235;
    private State state;
    private Matrix R;
    private Matrix Rx;
    private Matrix Ry;
    private Matrix Rz;
    private int prevX = -1;
    private int prevY = -1;

    public ResultView(State state) {
        this.state = state;

        setPreferredSize(new Dimension(600, 600));
        ResultView self = this;

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
        MouseMotionListener mml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - prevX;
                int dy = e.getY() - prevY;
                int dz = 0;
                Rx = Matrix.getRx(Math.toRadians(dy) * 2 / 3);
                Ry = Matrix.getRy(-Math.toRadians(dx) * 2 / 3);
                Rz = Matrix.getRz(Math.toRadians(dz) * 2 / 3);
                R = R.mul(Rx).mul(Ry).mul(Rz);
                prevX = e.getX();
                prevY = e.getY();
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        };
        MouseWheelListener mwl = (event) -> {
            double coef = Math.pow(1.1, event.getWheelRotation());
            self.state.setZf(self.state.getZf() * coef);
            self.state.setZb(self.state.getZb() * coef);
            repaint();
        };

        addMouseListener(ml);
        addMouseMotionListener(mml);
        addMouseWheelListener(mwl);
        resetAngles();
    }

    public void resetAngles() {
        Rx = Matrix.getRx(Math.toRadians(DEFAULT_X_ANGLE));
        Ry = Matrix.getRy(Math.toRadians(DEFAULT_Y_ANGLE));
        Rz = Matrix.getRz(Math.toRadians(DEFAULT_Z_ANGLE));
        R = Rx.mul(Ry).mul(Rz);
        repaint();
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
        return Mpsp.mul(cameraBasis).mul(R);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.BLACK);
        Matrix T = getTransformedMatrix();

        double degBetweenWires = 360.0 / state.getM1();
        for (int j = 0; j < state.getM1(); j++) {
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
                g2d.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
            }
        }
        
        degBetweenWires = 360.0 / (state.getN2() * state.getM1());
        List<List<Vector4>> circles = new ArrayList<>();
        for (int i = 0; i < state.getM2(); i++) {
            List<Vector4> circle = new ArrayList<>();
            circles.add(circle);
        }

        for (int i = 0; i < state.getN2() * state.getM1(); ++i) {
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
                g2d.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
            }
            Vector4 p1 = circles.get(i).get(circles.get(i).size() - 1);
            Vector4 p2 = circles.get(i).get(0);
            int x1 = (int) p1.getMatrix()[0][0];
            int y1 = (int) p1.getMatrix()[1][0];
            int x2 = (int) p2.getMatrix()[0][0];
            int y2 = (int) p2.getMatrix()[1][0];
            g2d.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
        }

        g.drawImage(image, 0, 0, null);
    }

    private void addWirePointsToCircles(Matrix R, List<List<Vector4>> circles) {
        if (circles.isEmpty()) {
            return;
        }
        double wiresBetweenCircles = (double) state.getSplinePoints().size() / state.getM2();
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