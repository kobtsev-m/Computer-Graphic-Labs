package controllers;

import utils.Mode;
import view.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ActionListeners {

    private DrawPanel drawPanel;

    public ActionListeners(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    public ActionListener drawLine = (e) -> {
        String inputValue = JOptionPane.showInputDialog("Line stroke:");
        if (inputValue == null || inputValue.isEmpty()) {
            return;
        }
        int parsedInt = Integer.parseInt(inputValue);
        if (parsedInt == 0) {
            return;
        }
        drawPanel.setMode(Mode.DrawLine);
        drawPanel.setLineThickness(parsedInt);
    };

    public ActionListener drawShape = (e) -> {
        String inputValue = JOptionPane.showInputDialog("Radius:");
        if (inputValue == null || inputValue.isEmpty()) {
            return;
        }
        int parsedInt = Integer.parseInt(inputValue);
        if (parsedInt == 0) {
            return;
        }
        drawPanel.setMode(Mode.DrawShape);
        drawPanel.setLineThickness(parsedInt);
    };
    public ActionListener drawStar = (e) -> {
        String inputValue = JOptionPane.showInputDialog("Radius:");
        if (inputValue == null || inputValue.isEmpty()) {
            return;
        }
        int parsedInt = Integer.parseInt(inputValue);
        if (parsedInt == 0) {
            return;
        }
        drawPanel.setMode(Mode.DrawStar);
        drawPanel.setLineThickness(parsedInt);
    };
    public ActionListener fillColor = (e) -> {

    };
    public ActionListener clean = (e) -> {
        drawPanel.clean();
    };

    public ActionListener about = (e) -> {
        JOptionPane.showMessageDialog(null, "Laboratory 1: ICGPaint\nAuthor: Mikhail Kobtsev");
    };

    public ActionListener fillColorChange = (e) -> {
        Color color = ((JButton) e.getSource()).getBackground();
        drawPanel.setFillColor(color);
    };
}
