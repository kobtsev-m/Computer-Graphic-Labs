package view;

import constants.Constants;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.function.IntFunction;

public class DrawPanelView extends JPanel implements MouseListener {

    private BufferedImage image;
    private Graphics2D imageG2d;

    private Mode mode = Mode.DrawLine;
    private int lineThickness = Constants.DefaultThickness;
    private int verticesNumber = Constants.DefaultVerticesNumber;
    private Color fillColor = Color.decode(Constants.MainColors[0]);

    private final int[] pressCoords = new int[] {0, 0};

    public DrawPanelView() {
        createImage();
        addMouseListener(this);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public void setVerticesNumber(int verticesNumber) {
        this.verticesNumber = verticesNumber;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    private void createImage() {
        image = new BufferedImage(Constants.PanelWidth, Constants.PanelHeight, BufferedImage.TYPE_INT_RGB);
        imageG2d = image.createGraphics();
        imageG2d.setBackground(Color.WHITE);
        imageG2d.fillRect(0, 0, Constants.PanelWidth, Constants.PanelHeight);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    private void redrawImage() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, this);
    }

    public void clean() {
        createImage();
        repaint();
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
            if (lineThickness > 1) {
                drawLineBase(pressCoords[0], pressCoords[1], e.getX(), e.getY());
            } else {
                drawLineBresenham(pressCoords[0], pressCoords[1], e.getX(), e.getY());
            }
            pressCoords[0] = -1;
            pressCoords[1] = -1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mode == Mode.DrawShape || mode == Mode.DrawStar) {
            drawShape(e.getX(), e.getY());
        } else if (mode == Mode.FillColor) {
            fill(e.getX(), e.getY());
        }
    }

    private void drawLineBase(int xStart, int yStart, int xEnd, int yEnd) {
        imageG2d.setColor(fillColor);
        imageG2d.setStroke(new BasicStroke(lineThickness));
        imageG2d.drawLine(xStart, yStart, xEnd, yEnd);
        redrawImage();
    }

    private void drawLineBresenham(int xStart, int yStart, int xEnd, int yEnd) {

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
        image.setRGB(x, y, fillColor.getRGB());

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
            image.setRGB(x, y, fillColor.getRGB());
        }

        redrawImage();
    }

    private void drawShape(int centerX, int centerY) {

        int N = verticesNumber;
        double R = Constants.DefaultRadius;
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
        
        imageG2d.setStroke(new BasicStroke(Constants.DefaultThickness));
        imageG2d.setColor(fillColor);
        for (int i = 0; i < M + 1; ++i) {
            prevX = x;
            prevY = y;
            x = (int) ((double)centerX + getR.apply(i) * Math.cos(getAngle.apply(i)));
            y = (int) ((double)centerY + getR.apply(i) * Math.sin(getAngle.apply(i)));
            if (prevX != -1 && prevY != -1) {
                imageG2d.drawLine(prevX, prevY, x, y);
            }
        }

        redrawImage();
    }

    private void fill(int xSeed, int ySeed) {
        int prevColor = image.getRGB(xSeed, ySeed);
        Stack<Span> spanStack = new Stack<>();
        spanStack.push(findFirstSpan(xSeed, ySeed, prevColor));

        while (!spanStack.isEmpty()) {
            Span currSpan = spanStack.pop();
            for (int x = currSpan.xLeft; x <= currSpan.xRight; ++x) {
                image.setRGB(x, currSpan.y, fillColor.getRGB());
            }
            findNextSpans(spanStack, currSpan, prevColor, true);
            findNextSpans(spanStack, currSpan, prevColor, false);
        }

        redrawImage();
    }

    private Span findFirstSpan(int xSeed, int ySeed, int prevColor) {

        int xLeft;
        int xRight;

        int x = xSeed;
        while (x > 0 && prevColor == image.getRGB(x - 1, ySeed)) {
            x--;
        }

        xLeft = x;

        x = xSeed;
        while (x < Constants.PanelWidth - 1 && prevColor == image.getRGB(x + 1, ySeed)) {
            x++;
        }

        xRight = x;

        return new Span(xLeft, xRight, ySeed);
    }

    private void findNextSpans(Stack<Span> spanStack, Span currSpan, int prevColor, boolean isUnder) {

        int xLeft = currSpan.xLeft;
        int xRight = currSpan.xRight;

        int x = xLeft;
        int y = isUnder ? currSpan.y - 1 : currSpan.y + 1;

        if (y == -1 || y == Constants.PanelHeight) {
            return;
        }

        int xLeftNew = -1;

        if (prevColor == image.getRGB(x, y)) {
            while (x > 0 && prevColor == image.getRGB(x - 1, y)) {
              x--;
            }
            xLeftNew = x;
        }

        while (x <= xRight && x != Constants.PanelWidth - 1) {
            if (prevColor == image.getRGB(x + 1, y)) {
                if (xLeftNew == -1) {
                    xLeftNew = x + 1;
                }
            } else {
                if (xLeftNew != -1) {
                    spanStack.push(new Span(xLeftNew, x, y));
                    xLeftNew = -1;
                }
            }
            x++;
        }

        if (xLeftNew != -1 && xLeftNew != x) {
            while (x < Constants.PanelWidth - 1 && prevColor == image.getRGB(x + 1, y)) {
                x++;
            }
            spanStack.push(new Span(xLeftNew, x, y));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
