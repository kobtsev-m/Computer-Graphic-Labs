package view;

import state.State;

import javax.swing.*;
import java.awt.*;

public class MainView extends JPanel {
    Window window;
    SplineView splineView;
    SplineMenu splineMenu;
    ResultView resultView;

    public MainView(Window window, ResultView resultView, State state) {
        this.window = window;
        this.resultView = resultView;

        setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.fill = GridBagConstraints.BOTH;
        constraints1.gridwidth = GridBagConstraints.REMAINDER;
        constraints1.weightx = 0.9f;
        constraints1.weighty = 0.9f;
        constraints1.gridy = 0;

        GridBagConstraints constraints2 = new GridBagConstraints();
        constraints2.fill = GridBagConstraints.BOTH;
        constraints2.gridwidth = GridBagConstraints.REMAINDER;
        constraints2.weightx = 0.1f;
        constraints2.weighty = 0.1f;
        constraints2.gridy = 1;

        splineView = new SplineView(state);
        splineMenu = new SplineMenu(resultView, splineView, state);
        splineView.setMenu(splineMenu);

        add(splineView, constraints1);
        add(splineMenu, constraints2);
        setVisible(true);
    }

    public State getState() {
        return splineView.getState();
    }

    public void setState(State state) {
        splineView.setState(state);
        splineMenu.setState(splineView.getState());
        resultView.setState(splineView.getState());
    }

    public void onClose() {
        splineMenu.onClose();
    }
}