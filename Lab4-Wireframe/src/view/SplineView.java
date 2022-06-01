package view;

import state.State;
import utils.CoordsTransformer;
import math.Matrix;
import math.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class SplineView extends JPanel {
    private final int WIDTH = 1100;
    private final int HEIGHT = 550;
    private final int POINT_SIZE = 20;
    private SplineMenu menu;
    private CoordsTransformer coordsTransformer;
    State state;
    private List<Vector4> subPoints = new LinkedList<>();
    private int selectedPoint;
    private int movingPoint;

    public SplineView(State state) {
        this.state = state;
        this.coordsTransformer = new CoordsTransformer(getWidth() / 2, getHeight() / 2, 10);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        SplineView self = this;

        ComponentListener cl = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                coordsTransformer = new CoordsTransformer(getWidth() / 2, getHeight() / 2, coordsTransformer.getScale());
                repaint();
            }
        };
        MouseMotionListener mml = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (movingPoint != -1) {
                    if (e.getX() < 0 || e.getX() > getWidth() - 1 || e.getY() < 0 || e.getY() > getHeight() - 1) {
                        return;
                    }
                    self.state.getPoints().get(movingPoint).setX(coordsTransformer.xAToB(e.getX()));
                    self.state.getPoints().get(movingPoint).setY(coordsTransformer.yAToB(e.getY()));
                    sendSelectedPointInfo();
                    generateSubPoints();
                    repaint();
                }
            }
        };
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (int i = 0; i < self.subPoints.size(); i++) {
                    Vector4 point = self.subPoints.get(i);
                    int px = coordsTransformer.xBToA(point.getX());
                    int py = coordsTransformer.yBToA(point.getY());
                    int size = POINT_SIZE / 2;
                    if ((px > x - size && px < x + size) && (py > y - size && py < y + size)) {
                        createPoint(x, y, i + 1);
                        return;
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (self.state.getPoints().size() <= 4) {
                        return;
                    }
                    for (int i = 0; i < self.state.getPoints().size(); i++) {
                        Vector4 point = self.state.getPoints().get(i);
                        int px = coordsTransformer.xBToA(point.getX());
                        int py = coordsTransformer.yBToA(point.getY());
                        int size = 8;
                        if ((px > x - size && px < x + size) && (py > y - size && py < y + size)) {
                            self.state.getPoints().remove(point);
                            resetPoint();
                            generateSubPoints();
                            sendSelectedPointInfo();
                            repaint();
                            break;
                        }
                    }
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
        MouseWheelListener mwl = (event) -> {
            if (event.getUnitsToScroll() > 0) {
                coordsTransformer.setScale(coordsTransformer.getScale() / 2);
            } else if (event.getUnitsToScroll() < 0) {
                coordsTransformer.setScale(coordsTransformer.getScale() * 2);
            }
            repaint();
        };

        addComponentListener(cl);
        addMouseListener(ml);
        addMouseMotionListener(mml);
        addMouseWheelListener(mwl);
    }

    public void setMenu(SplineMenu menu) {
        this.menu = menu;
    }

    private void createPoint(int x, int y, int num) {
        state.getPoints().add(num, new Vector4(coordsTransformer.xAToB(x), coordsTransformer.yAToB(y), POINT_SIZE));
        generateSubPoints();
    }

    private void resetPoint() {
        movingPoint = -1;
    }

    private void findPoint(int x, int y) {
        for (int i = 0; i < state.getPoints().size(); i++) {
            Vector4 point = state.getPoints().get(i);
            int px = coordsTransformer.xBToA(point.getX());
            int py = coordsTransformer.yBToA(point.getY());
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
        generateSubPoints();
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, coordsTransformer.yBToA(0), getWidth(), coordsTransformer.yBToA(0));
        g2d.drawLine(coordsTransformer.xBToA(0), 0, coordsTransformer.xBToA(0), getHeight());
        calculatePointsToDraw();

        g2d.setColor(Color.CYAN);
        for (int i = 0; i < state.getSplinePoints().size() - 1; i++) {
            Vector4 point1 = state.getSplinePoints().get(i);
            Vector4 point2 = state.getSplinePoints().get(i + 1);
            g2d.drawLine(
                coordsTransformer.xBToA(point1.getX()),
                coordsTransformer.yBToA(point1.getY()),
                coordsTransformer.xBToA(point2.getX()),
                coordsTransformer.yBToA(point2.getY())
            );
        }

        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < state.getPoints().size() - 1; i++) {
            Vector4 point1 = state.getPoints().get(i);
            Vector4 point2 = state.getPoints().get(i + 1);
            g2d.drawLine(
                coordsTransformer.xBToA(point1.getX()),
                coordsTransformer.yBToA(point1.getY()),
                coordsTransformer.xBToA(point2.getX()),
                coordsTransformer.yBToA(point2.getY())
            );
        }
        for (int i = 0; i < state.getPoints().size(); i++) {
            Vector4 point = state.getPoints().get(i);
            drawPoint(
                image,
                coordsTransformer.xBToA(point.getX()),
                coordsTransformer.yBToA(point.getY()),
                point.getPointSize(),
                Color.RED
            );
        }
        for (Vector4 point : subPoints) {
            drawPoint(
                image,
                coordsTransformer.xBToA(point.getX()),
                coordsTransformer.yBToA(point.getY()),
                point.getPointSize(),
                Color.RED
            );
        }
        if (selectedPoint != -1 && selectedPoint < state.getPoints().size()) {
            Vector4 point = state.getPoints().get(this.selectedPoint);
            drawPoint(
                image,
                coordsTransformer.xBToA(point.getX()),
                coordsTransformer.yBToA(point.getY()),
                point.getPointSize(),
                Color.WHITE
            );
        }

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

    private void generateSubPoints() {
        subPoints = new LinkedList<>();
        for (int i = 0; i < state.getPoints().size() - 1; i++) {
            Vector4 point1 = state.getPoints().get(i);
            Vector4 point2 = state.getPoints().get(i + 1);
            int subPointSize = POINT_SIZE / 2;
            Vector4 subPoint = new Vector4(
                (point2.getX() - point1.getX()) / 2 + point1.getX(),
                (point2.getY() - point1.getY()) / 2 + point1.getY(),
                subPointSize
            );
            subPoints.add(subPoint);
        }
    }

    public void calculatePointsToDraw() {
        Matrix M = new Matrix(new double[][] {
            {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}
        });
        M = M.mul(1.0 / 6.0);
        state.setSplinePoints(new LinkedList<>());
        int K = state.getPoints().size();
        for (int i = 3; i < K; ++i) {
            Matrix Gx = new Matrix(4, 1, new double[] {
                state.getPoints().get(i - 3).getX(),
                state.getPoints().get(i - 2).getX(),
                state.getPoints().get(i - 1).getX(),
                state.getPoints().get(i).getX()
            });
            Matrix Gy = new Matrix(4, 1, new double[] {
                state.getPoints().get(i - 3).getY(),
                state.getPoints().get(i - 2).getY(),
                state.getPoints().get(i - 1).getY(),
                state.getPoints().get(i).getY()
            });
            for (int j = 0; j < state.getN1() + 1; ++j) {
                double t = (double) j / state.getN1();
                Matrix T = new Matrix(1, 4, new double[] { Math.pow(t, 3), Math.pow(t, 2), Math.pow(t, 1), 1.0 });
                Matrix TM = T.mul(M);
                double x = TM.mul(Gx).getMatrix()[0][0];
                double y = TM.mul(Gy).getMatrix()[0][0];
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

    public void calculateBestScale() {
        Vector4 v1 = state.getPoints().iterator().next();
        if (v1 == null) {
            return;
        }
        double maxX = Math.abs(v1.getX());
        double maxY = Math.abs(v1.getY());
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
        double dx = maxX * 1.25;
        double dy = maxY * 1.25;
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) {
            w = WIDTH;
            h = HEIGHT;
        }
        double scaleX = (double) w / 2 / dx;
        double scaleY = (double) h / 2 / dy;
        double newScale = (Math.min(scaleX, scaleY));
        coordsTransformer.setScale(newScale);
        repaint();
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        state = newState;
        calculateBestScale();
        calculatePointsToDraw();
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
        generateSubPoints();
        sendSelectedPointInfo();
        repaint();
    }

    public void addPointAfterSelected() {
        int x;
        int y;
        if (selectedPoint >= subPoints.size()) {
            Vector4 subP = subPoints.get(selectedPoint - 1);
            Vector4 p = state.getPoints().get(selectedPoint);
            x = coordsTransformer.xBToA(2 * p.getX() - subP.getX());
            y = coordsTransformer.yBToA(2 * p.getY() - subP.getY());
        } else {
            Vector4 subP = subPoints.get(selectedPoint);
            x = coordsTransformer.xBToA(subP.getX());
            y = coordsTransformer.yBToA(subP.getY());
        }
        createPoint(x, y, selectedPoint + 1);
        resetPoint();
        generateSubPoints();
        repaint();
    }

    public int getSelectedNumber() {
        return Math.max(selectedPoint, 0);
    }
}