package state;

import math.Vector4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State implements Serializable {
    private int n1, m1, n2, m2;
    private double zb, zf, sw, sh;
    private List<Vector4> points = new LinkedList<>();
    private List<Vector4> splinePoints = new LinkedList<>();

    public State() {}

    public State(State state) {
        this.copy(state);
    }

    public static State createDefault() {
        State state = new State();
        state.n1 = 10;
        state.m1 = 10;
        state.n2 = 10;
        state.m2 = 10;
        state.sw = 8;
        state.sh = 8;
        state.zf = 100;
        state.zb = 100;
        state.points = new ArrayList<>();
        state.points.add(new Vector4(-1, -1, 20));
        state.points.add(new Vector4(-1, 1, 20));
        state.points.add(new Vector4(1, -1, 20));
        state.points.add(new Vector4(1, 1, 20));
        return state;
    }

    public void copy(State state) {
        n1 = state.n1;
        m1 = state.m1;
        n2 = state.n2;
        m2 = state.m2;
        zf = state.zf;
        zb = state.zb;
        sh = state.sh;
        sw = state.sw;
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

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }

    public int getM1() {
        return m1;
    }

    public void setM1(int m1) {
        this.m1 = m1;
    }
    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }

    public int getM2() {
        return m2;
    }

    public void setM2(int m2) {
        this.m2 = m2;
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
