package view;

import constants.Constants;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.IntFunction;

public class DrawPanel extends JPanel implements MouseListener {

    private boolean isClean = false;
    private final int[] pressCoords = new int[] {0, 0};

    private Mode mode = Mode.DrawLine;
    private int lineThickness = Constants.DefaultThickness;
    private int shapeRadius = Constants.DefaultRadius;
    private Color fillColor = Color.decode(Constants.MainColors[0]);

    public DrawPanel() {
        addMouseListener(this);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public void setShapeRadius(int shapeRadius) {
        this.shapeRadius = shapeRadius;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void clean() {
        isClean = !isClean;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == Mode.DrawLine) {
            pressCoords[0] = e.getX();
            pressCoords[1] = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mode == Mode.DrawLine) {
            Graphics2D g = (Graphics2D) getGraphics();
            g.setColor(fillColor);
            if (lineThickness > 1) {
                g.setStroke(new BasicStroke(lineThickness));
                g.drawLine(pressCoords[0], pressCoords[1], e.getX(), e.getY());
            } else {
                this.drawBresenhamLine(pressCoords[0], pressCoords[1], e.getX(), e.getY(), g);
            }
            pressCoords[0] = -1;
            pressCoords[1] = -1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mode != Mode.DrawShape && mode != Mode.DrawStar) {
            return;
        }

        Graphics g = getGraphics();
        g.setColor(fillColor);
        int centerX = e.getX();
        int centerY = e.getY();

        int N = 5;
        double R = shapeRadius;
        double betta = -Math.PI / (2.0 * N);
        double alpha = Math.PI / N;

        int prevX;
        int prevY;
        int x = -1;
        int y = -1;

        int M;
        IntFunction<Double> getR;
        IntFunction<Double> getAngle;

        if (mode == Mode.DrawShape) {
            M = N;
            getR = (i) -> R;
            getAngle = (i) -> betta + 2.0 * alpha * i;
        } else {
            M = 2*N;
            getR = (i) -> i % 2 == 1 ? 0.375 * R : R;
            getAngle = (i) -> betta + alpha * i;
        }

        for (int i = 0; i < M + 1; ++i) {
            prevX = x;
            prevY = y;
            x = (int) ((double)centerX + getR.apply(i) * Math.cos(getAngle.apply(i)));
            y = (int) ((double)centerY + getR.apply(i) * Math.sin(getAngle.apply(i)));
            if (prevX != -1 && prevY != -1) {
                g.drawLine(prevX, prevY, x, y);
            }
        }
    }

    public void drawBresenhamLine(int xStart, int yStart, int xEnd, int yEnd, Graphics g) {

        int x, y, dx, dy, xInc, yInc, pdx, pdy, es, el, err;

        dx = xEnd - xStart;
        dy = yEnd - yStart;
        xInc = Integer.compare(dx, 0);
        yInc = Integer.compare(dy, 0);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        if (dx > dy) {
            pdx = xInc;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = yInc;
            es = dx;
            el = dy;
        }

        x = xStart;
        y = yStart;
        err = el / 2;
        g.fillRect(x, y, 1, 1);

        for (int i = 0; i < el; ++i) {
            err -= es;
            if (err < 0) {
                err += el;
                x += xInc;
                y += yInc;
            } else {
                x += pdx;
                y += pdy;
            }
            g.fillRect(x, y, 1, 1);
        }
    }

    private void fillSpan() {}

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
