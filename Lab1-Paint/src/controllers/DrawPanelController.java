package controllers;

import constants.Constants;
import utils.Mode;
import view.DrawPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DrawPanelController {

    private DrawPanelView drawPanelView;

    public DrawPanelController(DrawPanelView drawPanelView) {
        this.drawPanelView = drawPanelView;
    }

    private boolean isIntValueInvalid(String value, int min, int max) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        int parsedInt = Integer.parseInt(value);
        return parsedInt < min || parsedInt > max;
    }

    public ActionListener drawLine = (e) -> {
        String value = JOptionPane.showInputDialog("Line stroke (1 <= x <= 100):");
        if (isIntValueInvalid(value, 1, 100)) {
            return;
        }
        drawPanelView.setMode(Mode.DrawLine);
        drawPanelView.setLineThickness(Integer.parseInt(value));
    };

    public ActionListener drawShape = (e) -> {
        String value = JOptionPane.showInputDialog("Vertices N (3 <= x <= 16):");
        if (isIntValueInvalid(value, 3, 16)) {
            return;
        }
        drawPanelView.setMode(Mode.DrawShape);
        drawPanelView.setVerticesNumber(Integer.parseInt(value));
    };

    public ActionListener drawStar = (e) -> {
        String value = JOptionPane.showInputDialog("Vertices N (3 <= x <= 16):");
        if (isIntValueInvalid(value, 3, 16)) {
            return;
        }
        drawPanelView.setMode(Mode.DrawStar);
        drawPanelView.setVerticesNumber(Integer.parseInt(value));
    };

    public ActionListener fillColor = (e) -> {
        drawPanelView.setMode(Mode.FillColor);
    };

    public ActionListener clean = (e) -> {
        drawPanelView.clean();
    };

    public ActionListener about = (e) -> {
        JOptionPane.showMessageDialog(null, "Task 1: ICGPaint\nAuthor: Mikhail Kobtsev");
    };

    public ActionListener fillColorChange = (e) -> {
        Color color;
        if (e.getSource().getClass() == JButton.class) {
            color = ((JButton) e.getSource()).getBackground();
        } else if (e.getSource().getClass() == JMenuItem.class) {
            color = ((JMenuItem) e.getSource()).getBackground();
        } else {
            return;
        }
        drawPanelView.setFillColor(color);
    };
}
