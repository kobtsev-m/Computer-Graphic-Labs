package view;

import state.State;
import utils.GridBagLayout;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SplineMenu extends JPanel {
    private final SplineView splineView;
    private State state;
    private final JSpinner nText;
    private final JSpinner mText;
    private final JSpinner numberText;
    private final JSpinner circleNSpinner;
    private final JSpinner xText;
    private final JSpinner yText;
    private final JSpinner circleMSpinner;
    private final Result3dView result3dView;
    private State newState;
    private boolean defaultClose = false;
    private ChangeListener circleMListener;
    public SplineMenu(Result3dView result3DView, SplineView splineView, State state) {
        setPreferredSize(new Dimension(600, 50));
        
        this.result3dView = result3DView;
        this.state = state;
        this.newState = new State(state);
        this.splineView = splineView;
        
        SplineMenu t = this;
        splineView.setState(newState);
        setLayout(new java.awt.GridBagLayout());
        GridBagLayout layout = new GridBagLayout(this);

        layout.addOnRow(new JLabel("n"));
        nText = createSpinner(state.getN(), 2, 100);
        nText.addChangeListener((event) -> {
            t.newState.setN(((int) (nText.getValue())));
            splineView.repaint();
        });
        layout.addOnRow(nText);

        layout.addOnRow(new JLabel("m"));
        mText = createSpinner(state.getM(), 1, 100);
        layout.addOnRow(mText);
        mText.addChangeListener((event) -> {
            t.newState.setM(((int) (mText.getValue())));
            splineView.repaint();
        });

        layout.addOnRow(new JLabel("circle n"));
        circleNSpinner = createSpinner(state.getCircleN(), 1, 1000);
        layout.addOnRow(circleNSpinner);

        layout.addOnRow(new JLabel("circle m"));
        circleMSpinner = createMSpinner(state.getCircleM(), 2, state.getPoints().size());
        layout.addOnRow(circleMSpinner, 30);

        layout.addOnRow(new JLabel("x"));
        xText = createDoubleSpinner(splineView.getSelectedX(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        xText.addChangeListener((event) -> splineView.setX((Double) xText.getValue()));
        layout.addOnRow(xText);

        layout.addOnRow(new JLabel("y"));
        yText = createDoubleSpinner(splineView.getSelectedY(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        yText.addChangeListener((event) -> splineView.setY((Double) yText.getValue()));
        layout.addOnRow(yText);

        layout.addOnRow(new JLabel("point"));
        numberText = createSpinner(splineView.getSelectedNumber(), 0, 1000);
        layout.addOnRow(numberText);
        numberText.addChangeListener((event) -> {
            if (t.newState.getPoints().size() == 0) {
                numberText.setValue("");
                return;
            }
            if ((int) (numberText.getValue()) >= t.newState.getPoints().size()) {
                numberText.setValue(t.newState.getPoints().size() - 1);
            }
            splineView.setSelectedPoint((int) (numberText.getValue()));
        });

        JButton applyBtn = new JButton("apply");
        applyBtn.addActionListener((event) -> {
            applyParams();
            JDialog frame = (JDialog) t.getRootPane().getParent();
            defaultClose = true;
            frame.dispose();
        });

        JButton cancelBtn = new JButton("cancel");
        cancelBtn.addActionListener((event) -> {
            SplineMenu.this.splineView.setState(t.state);
            resetParams();
            JDialog frame = (JDialog) t.getRootPane().getParent();
            defaultClose = true;
            frame.dispose();
        });

        JButton autoScaleBtn = new JButton("auto scale");
        autoScaleBtn.addActionListener((event) -> splineView.calcBestScale());
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
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "0.0##E0"));
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

    private JSpinner createMSpinner(int defaultValue, int min, int max) {
        if (defaultValue < min || defaultValue > max) {
            defaultValue = 2;
        }
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, 1));
        circleMListener = createCircleMListener(spinner, min, max);
        spinner.addChangeListener(circleMListener);
        spinner.setValue(defaultValue);
        return spinner;
    }

    private void changeSpinner(JSpinner spinner, int defaultValue, int min, int max) {
        spinner.setModel(new SpinnerNumberModel(defaultValue, min, max, 1));
        spinner.removeChangeListener(circleMListener);
        circleMListener = createCircleMListener(spinner, min, max);
        spinner.addChangeListener(circleMListener);
        spinner.setValue(defaultValue);
    }

    private ChangeListener createCircleMListener(JSpinner spinner, int min, int max) {
        return (event) -> {
            if ((int) spinner.getValue() < min) {
                spinner.setValue(min);
            } else if ((int) spinner.getValue() > max) {
                spinner.setValue(max);
            }
        };
    }

    private void applyParams() {
        newState.setCircleN((Integer) circleNSpinner.getValue());
        newState.setCircleM((Integer) circleMSpinner.getValue());
        state = newState;
        newState = new State(state);
        result3dView.setState(state);
        splineView.setState(state);
    }

    private void resetParams() {
        splineView.setState(state);
        newState = new State(state);
        result3dView.setState(state);
        nText.setValue(state.getN());
        mText.setValue(state.getM());
        circleNSpinner.setValue(state.getCircleN());
        circleMSpinner.setValue(state.getCircleM());
        numberText.setValue(0);
    }

    public void setSelectedPoint(int number) {
        numberText.setValue(number);
    }

    public void setX(double x) {
        xText.setValue(x);
    }

    public void setY(double y) {
        yText.setValue(y);
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

    public void setCountPoints(int maxCount) {
        changeSpinner(circleMSpinner, Math.min((int) circleMSpinner.getValue(), maxCount), 2, maxCount);
    }
}
