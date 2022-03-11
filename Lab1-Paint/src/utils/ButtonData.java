package utils;

import java.awt.event.ActionListener;

public class ButtonData {

    public String label;
    public String icon;
    public ActionListener actionListener;

    public ButtonData(String label, String icon, ActionListener actionListener) {
        this.label = label;
        this.icon = icon;
        this.actionListener = actionListener;
    }
}
