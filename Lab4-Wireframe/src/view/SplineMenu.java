package view;

import state.State;
import utils.GridBagLayout;

import javax.swing.*;
import java.awt.*;

public class SplineMenu extends JPanel {
    private final int WIDTH = 1100;
    private final int HEIGHT = 50;
    private boolean defaultClose = false;
    private final SplineView splineView;
    private final ResultView resultView;
    private State state;
    private State newState;
    private final JSpinner n1Spinner;
    private final JSpinner m1Spinner;
    private final JSpinner n2Spinner;
    private final JSpinner m2Spinner;
    private final JSpinner xSpinner;
    private final JSpinner ySpinner;
    private final JSpinner pointIndexSpinner;

    public SplineMenu(ResultView resultView, SplineView splineView, State state) {
        this.resultView = resultView;
        this.state = state;
        this.newState = new State(state);
        this.splineView = splineView;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        splineView.setState(newState);
        setLayout(new java.awt.GridBagLayout());
        GridBagLayout layout = new GridBagLayout(this);
        SplineMenu self = this;

        layout.addOnRow(new JLabel("N1"));
        n1Spinner = createSpinner(state.getN1(), 2, 100);
        n1Spinner.addChangeListener((event) -> {
            self.newState.setN1(((int) (n1Spinner.getValue())));
            splineView.repaint();
        });
        layout.addOnRow(n1Spinner);

        layout.addOnRow(new JLabel("M1"));
        m1Spinner = createSpinner(state.getM1(), 1, 100);
        m1Spinner.addChangeListener((event) -> {
            self.newState.setM1(((int) (m1Spinner.getValue())));
            splineView.repaint();
        });
        layout.addOnRow(m1Spinner);

        layout.addOnRow(new JLabel("N2"));
        n2Spinner = createSpinner(state.getN2(), 1, 100);
        layout.addOnRow(n2Spinner);

        layout.addOnRow(new JLabel("M2"));
        m2Spinner = createSpinner(state.getM2(), 2, 100);
        layout.addOnRow(m2Spinner);

        layout.addOnRow(new JLabel("X"));
        xSpinner = createDoubleSpinner(splineView.getSelectedX(), -100, 100, 1);
        xSpinner.addChangeListener((event) -> splineView.setX((Double) xSpinner.getValue()));
        layout.addOnRow(xSpinner);

        layout.addOnRow(new JLabel("Y"));
        ySpinner = createDoubleSpinner(splineView.getSelectedY(), -100, 100, 1);
        ySpinner.addChangeListener((event) -> splineView.setY((Double) ySpinner.getValue()));
        layout.addOnRow(ySpinner);

        layout.addOnRow(new JLabel("Point"));
        pointIndexSpinner = createSpinner(splineView.getSelectedNumber(), 0, 100);
        layout.addOnRow(pointIndexSpinner);
        pointIndexSpinner.addChangeListener((event) -> {
            if (self.newState.getPoints().size() == 0) {
                pointIndexSpinner.setValue("");
                return;
            }
            if ((int) (pointIndexSpinner.getValue()) >= self.newState.getPoints().size()) {
                pointIndexSpinner.setValue(self.newState.getPoints().size() - 1);
            }
            splineView.setSelectedPoint((int) (pointIndexSpinner.getValue()));
        });

        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener((event) -> {
            applyParams();
            JDialog frame = (JDialog) self.getRootPane().getParent();
            defaultClose = true;
            frame.dispose();
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener((event) -> {
            SplineMenu.this.splineView.setState(self.state);
            resetParams();
            JDialog frame = (JDialog) self.getRootPane().getParent();
            defaultClose = true;
            frame.dispose();
        });

        JButton autoScaleBtn = new JButton("Auto Scale");
        autoScaleBtn.addActionListener((event) -> splineView.calculateBestScale());
        JButton plusBtn = new JButton("+");
        JButton minusBtn = new JButton("-");
        minusBtn.addActionListener((event) -> splineView.removeSelectedPoint());
        plusBtn.addActionListener((event) -> splineView.addPointAfterSelected());
        layout.addOnRow(plusBtn);
        layout.addOnRow(minusBtn);
        layout.addOnRow(autoScaleBtn);
        layout.addOnRow(applyBtn);
        layout.addOnRow(cancelBtn);
    }

    private JSpinner createDoubleSpinner(double defaultValue, double min, double max, double step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, step));
        spinner.addChangeListener((event) -> {
            if ((double) spinner.getValue() < min) {
                spinner.setValue(min);
            } else if ((double) spinner.getValue() > max) {
                spinner.setValue(max);
            }
        });
        spinner.setValue(defaultValue);
        return spinner;
    }

    private JSpinner createSpinner(int defaultValue, int min, int max) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, 1));
        spinner.addChangeListener((event) -> {
            if ((int) spinner.getValue() < min) {
                spinner.setValue(min);
            } else if ((int) spinner.getValue() > max) {
                spinner.setValue(max);
            }
        });
        spinner.setValue(defaultValue);
        return spinner;
    }

    private void applyParams() {
        newState.setN2((Integer) n2Spinner.getValue());
        newState.setM2((Integer) m2Spinner.getValue());
        state = newState;
        newState = new State(state);
        resultView.setState(state);
        splineView.setState(state);
    }

    private void resetParams() {
        splineView.setState(state);
        newState = new State(state);
        resultView.setState(state);
        n1Spinner.setValue(state.getN1());
        m1Spinner.setValue(state.getM1());
        n2Spinner.setValue(state.getN2());
        m2Spinner.setValue(state.getM2());
        pointIndexSpinner.setValue(0);
    }

    public void setSelectedPoint(int number) {
        pointIndexSpinner.setValue(number);
    }

    public void setX(double x) {
        xSpinner.setValue(x);
    }

    public void setY(double y) {
        ySpinner.setValue(y);
    }

    public void setState(State state) {
        this.state = state;
        resetParams();
    }

    public void onClose() {
        if (!defaultClose) {
            resetParams();
        }
    }
}
