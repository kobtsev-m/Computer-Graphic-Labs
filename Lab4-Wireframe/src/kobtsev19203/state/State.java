package kobtsev19203.state;

import kobtsev19203.math.Vector4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State implements Serializable {
    private int n, m, circleN, circleM;
    private double zb, zf, sw, sh;
    private List<Vector4> points = new LinkedList<>();
    private List<Vector4> splinePoints = new LinkedList<>();

    public State() {
    }

    public State(State state) {
        this.copy(state);
    }

    public static State createDefault() {
        State state = new State();
        state.m = 10;
        state.n = 10;
        state.circleN = 10;
        state.circleM = 10;
        state.sw = 8;
        state.sh = 8;
        state.zf = 100;
        state.zb = 100;
        state.points = new ArrayList<>();
        state.points.add(new Vector4(-1, -1, 16));
        state.points.add(new Vector4(-1, 1, 16));
        state.points.add(new Vector4(1, -1, 16));
        state.points.add(new Vector4(1, 1, 16));
        return state;
    }

    public void copy(State state) {
        n = state.n;
        m = state.m;
        zf = state.zf;
        zb = state.zb;
        sh = state.sh;
        sw = state.sw;
        circleN = state.circleN;
        circleM = state.circleM;
        for (Vector4 vector4 : state.points) {
            points.add(new Vector4(vector4));
        }
        for (Vector4 vector4 : state.splinePoints) {
            splinePoints.add(new Vector4(vector4));
        }
    }

    public List<Vector4> getSplinePoints() {
        return splinePoints;
    }

    public void setSplinePoints(List<Vector4> splinePoints) {
        this.splinePoints = splinePoints;
    }

    public List<Vector4> getPoints() {
        return points;
    }

    public void setPoints(List<Vector4> points) {
        this.points = points;
    }

    public int getCircleN() {
        return circleN;
    }

    public void setCircleN(int circleN) {
        this.circleN = circleN;
    }

    public int getCircleM() {
        return circleM;
    }

    public void setCircleM(int circleM) {
        this.circleM = circleM;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public double getSw() {
        return sw;
    }

    public void setSw(double sw) {
        this.sw = sw;
    }

    public double getZb() {
        return zb;
    }

    public void setZb(double zb) {
        this.zb = zb;
    }

    public double getSh() {
        return sh;
    }

    public void setSh(double sh) {
        this.sh = sh;
    }

    public double getZf() {
        return zf;
    }

    public void setZf(double zf) {
        this.zf = zf;
    }
}
