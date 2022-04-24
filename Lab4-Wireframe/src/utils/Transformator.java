package utils;

public class Transformator {
    private final int x0;
    private final int y0;
    private double scale;

    public Transformator(int x, int y, double scale) {
        this.scale = scale;
        this.x0 = x;
        this.y0 = y;
    }
    public double xAToB(int x) {
        return (x - x0) / scale;
    }
    public double yAToB(int y) {
        return  (y - y0) / scale;
    }
    public int xBToA(double x) {
        return (int) Math.round(x*scale + x0);
    }
    public int yBToA(double y) {
        return (int) Math.round(y*scale + y0);
    }
    public void setScale(double scale) {
        if(scale<=0) {
            return;
        }
        this.scale=scale;
    }
    public double getScale(){
        return scale;
    }
}
