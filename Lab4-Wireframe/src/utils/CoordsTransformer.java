package utils;

public class CoordsTransformer {
    private final int x0;
    private final int y0;
    private double scale;

    public CoordsTransformer(int x0, int y0, double scale) {
        this.x0 = x0;
        this.y0 = y0;
        this.scale = scale;
    }
    public double xAToB(int x) {
        return (x - x0) / scale;
    }
    public double yAToB(int y) {
        return  (y - y0) / scale;
    }
    public int xBToA(double x) {
        return (int) Math.round(x0 + x*scale);
    }
    public int yBToA(double y) {
        return (int) Math.round(y0 + y*scale);
    }
    public void setScale(double scale) {
        if (scale > 0) {
            this.scale = scale;
        }
    }
    public double getScale(){
        return scale;
    }
}
