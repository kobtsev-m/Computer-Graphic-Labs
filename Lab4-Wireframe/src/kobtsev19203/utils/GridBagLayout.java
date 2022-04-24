package kobtsev19203.utils;

import javax.swing.*;
import java.awt.*;

public class GridBagLayout {
    private final JPanel panel;
    private int xCursor = 0;
    private final int yCursor = 0;

    public GridBagLayout(JPanel panel) {
        this.panel = panel;
    }

    public GridBagConstraints getConstr(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        getConstr(x, y, c);
        return c;
    }

    public GridBagConstraints getConstr(int x, int y, GridBagConstraints c) {
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 5, 5, 0);
        c.gridx = x;
        c.gridy = y;
        return c;
    }

    public void addOnRow(Component comp, int ipadx) {
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = ipadx;
        addOnRow(comp, c);
    }

    public void addOnRow(Component comp) {
        panel.add(comp, getConstr(xCursor++, yCursor));
    }

    public void addOnRow(Component comp, GridBagConstraints c) {
        panel.add(comp, getConstr(xCursor++, yCursor, c));
    }
}
