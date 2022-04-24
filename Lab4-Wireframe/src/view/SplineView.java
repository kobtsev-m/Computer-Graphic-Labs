package view;

import state.State;
import utils.Transformator;
import math.Matrix;
import math.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class SplineView extends JPanel {
    Transformator transformator;
    State state;
    private List<Vector4> subPoints = new LinkedList<>();
    private SplineMenu menu;
    private final int pointSize = 16;
    private int selectedPoint;
    private int movingPoint;

    public SplineView(State state) {
        this.state = state;
        setPreferredSize(new Dimension(1200, 600));
        transformator = new Transformator(getWidth() / 2, getHeight() / 2, 16);
        generateSubpoint();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                transformator = new Transformator(getWidth() / 2, getHeight() / 2, transformator.getScale());
                repaint();
            }
        });
        SplineView v = this;
        MouseMotionListener mml = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingPoint != -1) {
                    if (e.getX() < 0 || e.getX() > getWidth() - 1 || e.getY() < 0 || e.getY() > getHeight() - 1) {
                        return;
                    }
                    v.state.getPoints().get(movingPoint).setX(transformator.xAToB(e.getX()));
                    v.state.getPoints().get(movingPoint).setY(transformator.yAToB(e.getY()));
                    sendSelectedPointInfo();
                    generateSubpoint();
                    repaint();
                }
            }
        };
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (int i = 0; i < v.subPoints.size(); i++) {
                    Vector4 point = v.subPoints.get(i);
                    int px = transformator.xBToA(point.getX());
                    int py = transformator.yBToA(point.getY());
                    int size = pointSize / 2;
                    if ((px > x - size && px < x + size) && (py > y - size && py < y + size)) {
                        createPoint(x, y, i + 1);
                        return;
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (v.state.getPoints().size() <= 4) {
                        return;
                    }
                    for (int i = 0; i < v.state.getPoints().size(); i++) {
                        Vector4 point = v.state.getPoints().get(i);
                        int px = transformator.xBToA(point.getX());
                        int py = transformator.yBToA(point.getY());
                        int size = 8;
                        if ((px > x - size && px < x + size) && (py > y - size && py < y + size)) {
                            v.state.getPoints().remove(point);
                            resetPoint();
                            // Переотправить всё о новой точке
                            generateSubpoint();
                            sendSelectedPointInfo();
                            repaint();
                            break;
                        }
                    }
                    menu.setCountPoints(state.getPoints().size());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                findPoint(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resetPoint();
                repaint();
            }
        };
        addMouseListener(ml);
        addMouseMotionListener(mml);
        addMouseWheelListener(e -> {
            if (e.getUnitsToScroll() > 0) {
                transformator.setScale(transformator.getScale() / 2);
            } else if (e.getUnitsToScroll() < 0) {
                transformator.setScale(transformator.getScale() * 2);
            }
            repaint();
        });
    }

    public void setMenu(SplineMenu menu) {
        this.menu = menu;
    }

    private void createPoint(int x, int y, int num) {
        state.getPoints().add(num, new Vector4(transformator.xAToB(x), transformator.yAToB(y), pointSize));
        generateSubpoint();
        menu.setCountPoints(state.getPoints().size());
    }

    private void resetPoint() {
        movingPoint = -1;
    }

    private void findPoint(int x, int y) {
        int px;
        int py;
        for (int i = 0; i < state.getPoints().size(); i++) {
            Vector4 point = state.getPoints().get(i);
            px = transformator.xBToA(point.getX());
            py = transformator.yBToA(point.getY());
            int size = point.getPointSize();
            if ((px > x - size && px < x + size / 2) && (py > y - size / 2 && py < y + size / 2)) {
                selectedPoint = i;
                movingPoint = i;
                menu.setSelectedPoint(selectedPoint);
                sendSelectedPointInfo();
                return;
            }
        }
        movingPoint = -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        generateSubpoint();
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics gi = image.getGraphics();
        ((Graphics2D) gi).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) gi).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) gi).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        gi.setColor(Color.WHITE);
        gi.fillRect(0, 0, image.getWidth(), image.getHeight());
        gi.setColor(Color.BLACK);
        gi.drawLine(0, transformator.yBToA(0), getWidth(), transformator.yBToA(0));
        gi.drawLine(transformator.xBToA(0), 0, transformator.xBToA(0), getHeight());
        gi.setColor(Color.BLACK);
        int riskSize = 5;
        gi.drawLine(transformator.xBToA(1), getHeight() / 2 - riskSize, transformator.xBToA(1), getHeight() / 2 + riskSize);
        gi.drawLine(getWidth() / 2 - riskSize, transformator.yBToA(1), getWidth() / 2 + riskSize, transformator.yBToA(1));
        calcPointsToDraw();
        gi.setColor(Color.BLACK);
        for (int i = 0; i < state.getSplinePoints().size() - 1; i++) {
            Vector4 point1 = state.getSplinePoints().get(i);
            Vector4 point2 = state.getSplinePoints().get(i + 1);
            gi.drawLine(transformator.xBToA(point1.getX()), transformator.yBToA(point1.getY()), transformator.xBToA(point2.getX()), transformator.yBToA(point2.getY()));
        }
        gi.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < state.getPoints().size() - 1; i++) {
            Vector4 point1 = state.getPoints().get(i);
            Vector4 point2 = state.getPoints().get(i + 1);
            gi.drawLine(transformator.xBToA(point1.getX()), transformator.yBToA(point1.getY()), transformator.xBToA(point2.getX()), transformator.yBToA(point2.getY()));
        }
        gi.setColor(Color.BLUE);
        for (int i = 0; i < state.getPoints().size(); i++) {
            Vector4 point = state.getPoints().get(i);
            drawPoint(image, transformator.xBToA(point.getX()), transformator.yBToA(point.getY()), point.getPointSize(), Color.RED);
        }

        for (Vector4 point : subPoints) {
            drawPoint(image, transformator.xBToA(point.getX()), transformator.yBToA(point.getY()), point.getPointSize(), Color.RED);
        }
        if (selectedPoint != -1 && selectedPoint < state.getPoints().size()) {
            Vector4 point = state.getPoints().get(this.selectedPoint);
            drawPoint(image, transformator.xBToA(point.getX()), transformator.yBToA(point.getY()), point.getPointSize(), Color.BLUE);

        }
        gi.setColor(Color.GREEN);
        g.drawImage(image, 0, 0, null);
    }

    private void drawPoint(BufferedImage image, int x, int y, int size, Color color) {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - size / 2, y - size / 2, size, size);
    }

    private void generateSubpoint() {
        subPoints = new LinkedList<>();
        for (int i = 0; i < state.getPoints().size() - 1; i++) {
            Vector4 point1 = state.getPoints().get(i);
            Vector4 point2 = state.getPoints().get(i + 1);
            int subPointSize = pointSize / 2;
            Vector4 subPoint = new Vector4((point2.getX() - point1.getX()) / 2 + point1.getX(), (point2.getY() - point1.getY()) / 2 + point1.getY(), subPointSize);
            subPoints.add(subPoint);
        }
    }

    public void calcPointsToDraw() {
        Matrix m = new Matrix(new double[][]{
            {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}
        });
        state.setSplinePoints(new LinkedList<>());
        int K = state.getPoints().size();
        for (int i = 3; i < K; ++i) {
            Matrix gx = new Matrix(4, 1, new double[]{
                    state.getPoints().get(i - 3).getX(),
                    state.getPoints().get(i - 2).getX(),
                    state.getPoints().get(i - 1).getX(),
                    state.getPoints().get(i).getX()
            });
            Matrix gy = new Matrix(4, 1, new double[]{
                    state.getPoints().get(i - 3).getY(),
                    state.getPoints().get(i - 2).getY(),
                    state.getPoints().get(i - 1).getY(),
                    state.getPoints().get(i).getY()
            });
            double dt = 1.0 / state.getN();
            for (int j = 0; j < state.getN() + 1; ++j) {
                double t = j * dt;
                Matrix T = new Matrix(1, 4, new double[]{Math.pow(t, 3), Math.pow(t, 2), Math.pow(t, 1), 1.0});
                Matrix tm = T.mul(m);
                double x = tm.mul(gx).mul(1.0 / 6.0).getValue();
                double y = tm.mul(gy).mul(1.0 / 6.0).getValue();
                state.getSplinePoints().add(new Vector4(x, y, 0));
            }
        }
    }

    public void setX(double x) {
        if (selectedPoint >= state.getPoints().size()) {
            return;
        }
        state.getPoints().get(selectedPoint).setX(x);
        repaint();
    }

    public void setY(double y) {
        if (selectedPoint >= state.getPoints().size()) {
            return;
        }
        state.getPoints().get(selectedPoint).setY(y);
        repaint();
    }

    public void setSelectedPoint(int value) {
        if (value >= state.getPoints().size()) {
            return;
        }
        selectedPoint = value;
        sendSelectedPointInfo();
        repaint();
    }

    public void sendSelectedPointInfo() {
        if (selectedPoint >= state.getPoints().size()) {
            return;
        }
        menu.setSelectedPoint(selectedPoint);
        menu.setX(state.getPoints().get(selectedPoint).getX());
        menu.setY(state.getPoints().get(selectedPoint).getY());
    }

    public void calcBestScale() {
        double maxX;
        double maxY;
        Vector4 v1 = state.getPoints().iterator().next();
        if (v1 != null) {
            maxX = Math.abs(v1.getX());
            maxY = Math.abs(v1.getY());
        } else {
            return;
        }
        for (Vector4 v : state.getPoints()) {
            double absX = Math.abs(v.getX());
            double absY = Math.abs(v.getY());
            if (maxX < absX) {
                maxX = absX;
            }
            if (maxY < absY) {
                maxY = absY;
            }
        }
        double dx = maxX * 1.3;
        double dy = maxY * 1.3;
        int h = getHeight();
        int w = getWidth();
        if (w == 0 || h == 0) {
            h = 600;
            w = 800;
        }
        double scaleX = (double) w / 2 / dx;
        double scaleY = (double) h / 2 / dy;
        double newScale = (Math.min(scaleX, scaleY));
        transformator.setScale(newScale);
        repaint();
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        state = newState;
        calcBestScale();
        calcPointsToDraw();
    }

    public double getSelectedY() {
        if (selectedPoint >= state.getPoints().size() || selectedPoint < 0) {
            return 0;
        }
        return state.getPoints().get(selectedPoint).getY();
    }

    public double getSelectedX() {
        if (selectedPoint >= state.getPoints().size() || selectedPoint < 0) {
            return 0;
        }
        return state.getPoints().get(selectedPoint).getX();
    }

    public void removeSelectedPoint() {
        if (state.getPoints().size() < 5) {
            return;
        }
        state.getPoints().remove(selectedPoint);
        if (selectedPoint >= state.getPoints().size()) {
            setSelectedPoint(state.getPoints().size() - 1);
        }
        resetPoint();
        generateSubpoint();
        sendSelectedPointInfo();
        repaint();
        menu.setCountPoints(state.getPoints().size());
    }

    public void addPointAfterSelected() {
        int x;
        int y;
        if (selectedPoint >= subPoints.size()) {
            Vector4 subP = subPoints.get(selectedPoint - 1);
            Vector4 p = state.getPoints().get(selectedPoint);
            x = transformator.xBToA(2 * p.getX() - subP.getX());
            y = transformator.yBToA(2 * p.getY() - subP.getY());
        } else {
            Vector4 subP = subPoints.get(selectedPoint);
            x = transformator.xBToA(subP.getX());
            y = transformator.yBToA(subP.getY());
        }
        createPoint(x, y, selectedPoint + 1);
        resetPoint();
        generateSubpoint();
        repaint();
        menu.setCountPoints(state.getPoints().size());
    }

    public int getSelectedNumber() {
        return Math.max(selectedPoint, 0);
    }
}