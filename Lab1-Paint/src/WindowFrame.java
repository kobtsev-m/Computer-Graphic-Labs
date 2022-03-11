import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

enum Mode {
    DrawLine,
    DrawShape,
    FillColor,
    Clean
}

class ButtonData {

    String label;
    String icon;
    ActionListener actionListener;

    ButtonData(String label, String icon, ActionListener actionListener) {
        this.label = label;
        this.icon = icon;
        this.actionListener = actionListener;
    }
}

public class WindowFrame extends JFrame {

    private final int windowWidth = 640;
    private final int windowHeight = 480;
    private final int toolbarHeight = 20;

    WindowFrame() {
        super("ICG Paint");
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)((dimension.getWidth() - windowWidth) / 2);
        int y = (int)((dimension.getHeight() - windowHeight) / 2);
        setLocation(x, y);

        DrawPanel drawPanel = new DrawPanel();

        ActionListener drawLineAL = (e) -> drawPanel.setMode(Mode.DrawLine);
        ActionListener drawShapeAL = (e) -> drawPanel.setMode(Mode.DrawShape);
        ActionListener fillColorAl = (e) -> drawPanel.setMode(Mode.DrawShape);
        ActionListener cleanAL = (e) -> drawPanel.clean();

        LinkedHashMap<Mode, ButtonData> buttonsData = new LinkedHashMap<>();
        buttonsData.put(Mode.DrawLine, new ButtonData("Draw Line", "pencil-16.png", drawLineAL));
        buttonsData.put(Mode.DrawShape, new ButtonData("Draw Shape", "star-16.png", drawShapeAL));
        buttonsData.put(Mode.FillColor, new ButtonData("Fill Color", "bucket-16.png", fillColorAl));
        buttonsData.put(Mode.Clean, new ButtonData("Clean", "eraser-16.png", cleanAL));

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
        aboutMenuItem.addActionListener((e) -> System.out.println("Hello World"));
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        JToolBar toolBar = new JToolBar();
        toolBar.setSize(windowWidth, toolbarHeight);
        toolBar.setFloatable(false);
        toolBar.add(Box.createHorizontalStrut(10));
        for (ButtonData buttonData: buttonsData.values()) {
            JButton btn = new JButton(new ImageIcon("assets/" + buttonData.icon));
            btn.addActionListener(buttonData.actionListener);
            toolBar.add(btn);
            toolBar.add(Box.createHorizontalStrut(10));
        }
        toolBar.add(Box.createHorizontalStrut(30));
        for (String hexColor: Constants.MainColors) {
            JButton btn = new JButton();
            btn.setBackground(Color.decode(hexColor));
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
