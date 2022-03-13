package view;

import constants.Constants;
import controllers.DrawPanelController;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class WindowFrameView extends JFrame {

    private DrawPanelView drawPanelView;
    private DrawPanelController drawPanelController;
    private LinkedHashMap<Mode, ButtonData> menuButtonsData;
    private JMenuBar menuBar;
    private JToolBar toolBar;

    public WindowFrameView() {
        super("ICG Paint");
        setPreferredSize(new Dimension(Constants.WindowWidth, Constants.WindowHeight));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((dimension.getWidth() - Constants.WindowWidth) / 2);
        int y = (int)((dimension.getHeight() - Constants.WindowHeight) / 2);
        setLocation(x, y);

        createDrawPanel();
        createMenuButtonsData();
        createMenuBar();
        createToolBar();

        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.PAGE_START);
        add(drawPanelView);

        setVisible(true);
        pack();
    }

    private void createDrawPanel() {
        drawPanelView = new DrawPanelView();
        drawPanelController = new DrawPanelController(drawPanelView);
    }

    private void createMenuButtonsData() {
        menuButtonsData = new LinkedHashMap<>();
        menuButtonsData.put(
            Mode.DrawLine,
            new ButtonData("Draw Line", "pencil-16.png", drawPanelController.drawLine)
        );
        menuButtonsData.put(
            Mode.DrawShape,
            new ButtonData("Draw Shape", "polygon-16.png", drawPanelController.drawShape)
        );
        menuButtonsData.put(
            Mode.DrawStar,
            new ButtonData("Draw Star", "star-16.png", drawPanelController.drawStar)
        );
        menuButtonsData.put(
            Mode.FillColor,
            new ButtonData("Fill Color", "bucket-16.png", drawPanelController.fillColor)
        );
        menuButtonsData.put(
            Mode.Clean,
            new ButtonData("Clean", "eraser-16.png", drawPanelController.clean)
        );
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        JMenu toolsMenu = new JMenu("Tools");
        for (ButtonData buttonData: menuButtonsData.values()) {
            JMenuItem menuItem = new JMenuItem(buttonData.label);
            menuItem.addActionListener(buttonData.actionListener);
            toolsMenu.add(menuItem);
        }
        menuBar.add(toolsMenu);
        JMenu colorsMenu = new JMenu("Colors");
        for (String hexColor: Constants.MainColors) {
            JMenuItem menuItem = new JMenuItem(hexColor);
            menuItem.setBackground(Color.decode(hexColor));
            menuItem.addActionListener(drawPanelController.fillColorChange);
            colorsMenu.add(menuItem);
        }
        menuBar.add(colorsMenu);
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(drawPanelController.about);
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);
    }
    
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setSize(Constants.WindowWidth, Constants.ToolbarHeight);
        toolBar.setFloatable(false);
        toolBar.add(Box.createHorizontalStrut(10));
        for (ButtonData buttonData: menuButtonsData.values()) {
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
            btn.setToolTipText(hexColor);
            btn.addActionListener(drawPanelController.fillColorChange);
            toolBar.add(btn);
            toolBar.add(Box.createHorizontalStrut(10));
        }
    }
}
