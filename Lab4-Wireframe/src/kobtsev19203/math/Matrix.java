package kobtsev19203.math;

import java.io.Serializable;

public class Matrix implements Serializable {
    private double[][] matrix;

    public Matrix() {}

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix(int rows, int columns, double[] matrix) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("invalid size");
        }
        this.matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix, i * columns, this.matrix[i], 0, columns);
        }
    }

    public Vector4 mul(Vector4 operand) {
        double[][] a = matrix;
        double[][] b = operand.getMatrix();
        double[][] c = mul(a, b);
        return new Vector4(c);
    }

    public Matrix mul(Matrix operand) {
        double[][] a = matrix;
        double[][] b = operand.matrix;
        double[][] c = mul(a, b);
        return new Matrix(c);
    }

    private double[][] mul(double[][] a, double[][] b) {
        double[][] c = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < b.length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    public Matrix mul(double k) {
        double[][] a = matrix;
        double[][] c = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                c[i][j] = a[i][j] * k;
            }
        }
        return new Matrix(c);
    }

    public int getRows() {
        return matrix.length;
    }

    public int getCols() {
        return matrix[0].length;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    protected void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public static Matrix getRx(double angle) {
        return new Matrix(4, 4, new double[] {
            1, 0, 0, 0,
            0, Math.cos(angle), -Math.sin(angle), 0,
            0, Math.sin(angle), Math.cos(angle), 0,
            0, 0, 0, 1
        });
    }

    public static Matrix getRy(double angle) {
        return new Matrix(4, 4, new double[] {
            Math.cos(angle), 0, Math.sin(angle), 0,
            0, 1, 0, 0,
            -Math.sin(angle), 0, Math.cos(angle), 0,
            0, 0, 0, 1
        });
    }

    public static Matrix getRz(double angle) {
        return new Matrix(4, 4, new double[] {
            Math.cos(angle), -Math.sin(angle), 0, 0,
            Math.sin(angle), Math.cos(angle), 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        });
    }

    public static Matrix getMproj(double Sw, double Sh, double zf, double zb) { //55
        return new Matrix(4, 4, new double[]{
            2.0 * zf * Sw, 0, 0, 0,
            0, 2.0 * zf * Sh, 0, 0,
            0, 0, zb / (zb - zf), (-zf * zb) / (zb - zf),
            0, 0, 1, 0
        });
    }

    public double getValue() {
        if (matrix.length != 1 || matrix[0].length != 1) {
            throw new UnsupportedOperationException("not 1x1 matrix");
        }
        return matrix[0][0];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                builder.append(matrix[i][j]).append(", ");
            }
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    public double getX() {
        return matrix[0][0];
    }

    public double getY() {
        return matrix[1][0];
    }

    public double getZ() {
        return matrix[2][0];
    }
}
