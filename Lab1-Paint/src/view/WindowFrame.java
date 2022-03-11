package view;

import constants.Constants;
import controllers.ActionListeners;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class WindowFrame extends JFrame {

    public WindowFrame() {
        super("ICG Paint");
        setPreferredSize(new Dimension(Constants.WindowWidth, Constants.WindowHeight));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((dimension.getWidth() - Constants.WindowWidth) / 2);
        int y = (int)((dimension.getHeight() - Constants.WindowHeight) / 2);
        setLocation(x, y);

        DrawPanel drawPanel = new DrawPanel();
        ActionListeners AL = new ActionListeners(drawPanel);

        LinkedHashMap<Mode, ButtonData> buttonsData = new LinkedHashMap<>();
        buttonsData.put(Mode.DrawLine, new ButtonData("Draw Line", "pencil-16.png", AL.drawLine));
        buttonsData.put(Mode.DrawShape, new ButtonData("Draw Shape", "polygon-16.png", AL.drawShape));
        buttonsData.put(Mode.DrawStar, new ButtonData("Draw Star", "star-16.png", AL.drawStar));
        buttonsData.put(Mode.FillColor, new ButtonData("Fill Color", "bucket-16.png", AL.fillColor));
        buttonsData.put(Mode.Clean, new ButtonData("Clean", "eraser-16.png", AL.clean));

        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        for (ButtonData buttonData: buttonsData.values()) {
            JMenuItem menuItem = new JMenuItem(buttonData.label);
            menuItem.addActionListener(buttonData.actionListener);
            viewMenu.add(menuItem);
        }
        menuBar.add(viewMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(AL.about);
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        JToolBar toolBar = new JToolBar();
        toolBar.setSize(Constants.WindowWidth, Constants.ToolbarHeight);
        toolBar.setFloatable(false);
        toolBar.add(Box.createHorizontalStrut(10));
        for (ButtonData buttonData: buttonsData.values()) {
            JButton btn = new JButton(new ImageIcon("assets/" + buttonData.icon));
            btn.setToolTipText(buttonData.label);
            btn.addActionListener(buttonData.actionListener);
            toolBar.add(btn);
            toolBar.add(Box.createHorizontalStrut(10));
        }
        toolBar.add(Box.createHorizontalStrut(30));
        for (String hexColor: Constants.MainColors) {
            JButton btn = new JButton();
            btn.setBackground(Color.decode(hexColor));
            btn.addActionListener(AL.fillColorChange);
            toolBar.add(btn);
            toolBar.add(Box.createHorizontalStrut(10));
        }

        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.PAGE_START);
        add(drawPanel);

        setVisible(true);
        pack();
    }
}
