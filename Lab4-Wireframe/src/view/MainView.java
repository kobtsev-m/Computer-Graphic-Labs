package view;

import state.State;

import javax.swing.*;
import java.awt.*;

public class MainView extends JPanel {
    Window window;
    SplineView splineView;
    SplineMenu splineMenu;
    Result3dView result3dView;

    public MainView(Window window, Result3dView result3dView, State state) {
        this.window = window;
        this.result3dView = result3dView;

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
        splineMenu = new SplineMenu(result3dView, splineView, state);
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
        result3dView.setState(splineView.getState());
    }

    public void onClose() {
        splineMenu.onClose();
    }
}