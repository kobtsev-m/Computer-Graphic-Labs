import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.IntFunction;

public class DrawPanel extends JPanel implements MouseListener {

    Mode mode = Mode.DrawLine;
    boolean isClean = false;
    int[] pressCoords = new int[] {0, 0};

    DrawPanel() {
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void clean() {
        isClean = !isClean;
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
            Graphics2D g = (Graphics2D) getGraphics();
            g.setStroke(new BasicStroke(5));
            // g.drawLine(pressCoords[0], pressCoords[1], e.getX(), e.getY());
            this.drawBresenhamLine(pressCoords[0], pressCoords[1], e.getX(), e.getY(), g);
            pressCoords[0] = -1;
            pressCoords[1] = -1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mode != Mode.DrawShape) {
            return;
        }

        Graphics g = getGraphics();
        int centerX = e.getX();
        int centerY = e.getY();

        int N = 5;
        double R = 100.0;
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

        /*
         * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
         * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
         * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
         * по y сдвиг такой отсутствует.
         */
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
                x += xInc; // сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += yInc; // или сместить влево-вправо, если цикл проходит по y
            } else {
                x += pdx; // продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy; // цикл идёт по иксу; сдвинуть вверх или вниз, если по y
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
