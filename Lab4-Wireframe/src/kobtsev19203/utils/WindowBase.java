package kobtsev19203.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.MenuElement;

public class WindowBase extends JFrame {
	private final JMenuBar menuBar;
	protected JToolBar toolBar;

	public WindowBase() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		toolBar = new JToolBar("Main toolbar");
		toolBar.setRollover(true);
		add(toolBar, BorderLayout.PAGE_START);
	}

	public WindowBase(int x, int y, String title) {
		this();
		setSize(x, y);
		setLocationByPlatform(true);
		setTitle(title);
	}

	public JMenuItem createMenuItem(String title, String icon, String actionMethod) throws NoSuchMethodException {
		JMenuItem item = new JMenuItem(title);
		if (icon != null) {
			item.setIcon(new ImageIcon(getClass().getResource("assets/" + icon), title));
		}
		Method method = getClass().getMethod(actionMethod);
		item.addActionListener((event) -> {
			try {
				method.invoke(WindowBase.this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return item;
	}

	public JMenu createSubMenu(String title) {
		return new JMenu(title);
	}

	public void addSubMenu(String title) {
		MenuElement element = getParentMenuElement(title);
		if (element == null) {
			throw new InvalidParameterException("menu path not found: " + title);
		}
		JMenu subMenu = createSubMenu(getMenuPathName(title));
		if (element instanceof JMenuBar) {
			((JMenuBar) element).add(subMenu);
		} else if (element instanceof JMenu) {
			((JMenu) element).add(subMenu);
		} else if (element instanceof JPopupMenu) {
			((JPopupMenu) element).add(subMenu);
		} else {
			throw new InvalidParameterException("invalid menu path: " + title);
		}
	}

	public void addMenuItem(String title, String icon, String actionMethod) throws NoSuchMethodException {
		MenuElement element = getParentMenuElement(title);
		if (element == null) {
			throw new InvalidParameterException("menu path not found: " + title);
		}
		JMenuItem item = createMenuItem(getMenuPathName(title), icon, actionMethod);
		if (element instanceof JMenu) {
			((JMenu) element).add(item);
		} else if(element instanceof JPopupMenu) {
			((JPopupMenu) element).add(item);
		} else {
			throw new InvalidParameterException("invalid menu path: " + title);
		}
	}

	private String getMenuPathName(String menuPath) {
		int pos = menuPath.lastIndexOf('/');
		return pos > 0 ? menuPath.substring(pos + 1) : menuPath;
	}

	private MenuElement getParentMenuElement(String menuPath) {
		int pos = menuPath.lastIndexOf('/');
		return pos > 0 ? getMenuElement(menuPath.substring(0, pos)) : menuBar;
	}

	public MenuElement getMenuElement(String menuPath) {
		MenuElement element = menuBar;
		for (String pathElement: menuPath.split("/")) {
			MenuElement newElement = null;
			for (MenuElement subElement: element.getSubElements()) {
				if (
					(subElement instanceof JMenu && ((JMenu)subElement).getText().equals(pathElement)) ||
					(subElement instanceof JMenuItem && ((JMenuItem)subElement).getText().equals(pathElement))
				) {
					if (subElement.getSubElements().length==1 && subElement.getSubElements()[0] instanceof JPopupMenu) {
						newElement = subElement.getSubElements()[0];
					} else {
						newElement = subElement;
					}
					break;
				}
			}
			if (newElement == null) {
				return null;
			}
			element = newElement;
		}
		return element;
	}

	public JButton createToolBarButton(JMenuItem item) {
		JButton button = new JButton(item.getIcon());
		for (ActionListener listener: item.getActionListeners()) {
			button.addActionListener(listener);
		}
		button.setToolTipText(item.getToolTipText());
		return button;
	}

	public JButton createToolBarButton(String menuPath) {
		JMenuItem item = (JMenuItem)getMenuElement(menuPath);
		if (item == null) {
			throw new InvalidParameterException("menu path not found: " + menuPath);
		}
		return createToolBarButton(item);
	}

	public void addToolBarButton(String menuPath) {
		toolBar.add(createToolBarButton(menuPath));
	}

	public void addToolBarSeparator() {
		toolBar.addSeparator();
	}

	public File getSaveFileName(String extension, String description) {
		return FileUtils.getSaveFileName(this, extension, description);
	}

	public File getOpenFileName(String extension, String description) {
		return FileUtils.getOpenFileName(this, extension, description);
	}
}
