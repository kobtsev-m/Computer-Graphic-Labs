package math;

import java.io.Serializable;

public class Vector4 extends Matrix implements Serializable {
    private int pointSize;

    public Vector4(double[][] matrix) {
        super(matrix);
    }

    public Vector4(double x, double y, int size) {
        super(4, 1, new double[] {0, y, x, 1});
        pointSize = size;
    }

    public Vector4(Vector4 vector4) {
        double[][] m = vector4.getMatrix();
        double[][] newMatrix = new double[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            System.arraycopy(m[i], 0, newMatrix[i], 0, m[0].length);
        }
        this.setMatrix(newMatrix);
        this.setPointSize(vector4.getPointSize());
    }

    @Override
    public double getY() {
        return getMatrix()[1][0];
    }

    public void setY(double y) {
        getMatrix()[1][0] = y;
    }

    @Override
    public double getX() {
        return getMatrix()[2][0];
    }

    public void setX(double x) {
        getMatrix()[2][0] = x;
    }

    @Override
    public double getZ() {
        return getMatrix()[3][0];
    }

    public void setZ(double z) {
        getMatrix()[3][0] = z;
    }

    public void normalizeByLastPoint() {
        double last = getMatrix()[getMatrix().length - 1][0];
        for (int i = 0; i < getMatrix().length; i++) {
            getMatrix()[i][0] = getMatrix()[i][0] / last;
        }
        getMatrix()[getMatrix().length - 1][0] = 1;
    }

    public int getPointSize() {
        return pointSize;
    }

    public void setPointSize(int pointSize) {
        if (pointSize >= 0) {
            this.pointSize = pointSize;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
