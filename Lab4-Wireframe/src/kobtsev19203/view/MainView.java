package kobtsev19203.view;

import kobtsev19203.Window;
import kobtsev19203.state.State;

import javax.swing.*;
import java.awt.*;

public class MainView extends JPanel {
    Window mw;
    SplineView view;
    SplineMenu menu;
    Result3dView result3dView;

    public MainView(Window mw, Result3dView result3dView, State state) {
        this.mw = mw;
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

        view = new SplineView(state);
        menu = new SplineMenu(result3dView, view, state);
        view.setMenu(menu);

        add(view, constraints1);
        add(menu, constraints2);
        setVisible(true);
    }

    public JPanel getParametersPanel() {
        return this;
    }

    public State getState() {
        return view.getState();
    }

    public void setState(State state) {
        view.setState(state);
        menu.setState(view.getState());
        result3dView.setState(view.getState());
    }

    public void onClose() {
        menu.onClose();
    }
}