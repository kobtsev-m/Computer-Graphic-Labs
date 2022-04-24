package kobtsev19203;

import kobtsev19203.state.State;
import kobtsev19203.view.MainView;
import kobtsev19203.view.Result3dView;
import kobtsev19203.utils.WindowBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Window extends WindowBase {
	MainView mainView;
	Result3dView result3dView;

	public Window() {
		super(600, 400, "Wireframe");
		try {
			addSubMenu("File");
			addMenuItem("File/Save", "save.png", "onSave");
			addMenuItem("File/Load", "open.png", "onLoad");
			addSubMenu("Scene");
			addMenuItem("Scene/Spline", "spline.png", "onParameters");
			addMenuItem("Scene/Reset", "reset.png", "onReset");
			addSubMenu("About");
			addMenuItem("About/About", "about.png", "onAbout");
			addToolBarButton("File/Save");
			addToolBarButton("File/Load");
			addToolBarSeparator();
			addToolBarButton("Scene/Reset");
			addToolBarButton("Scene/Spline");
			addToolBarSeparator();
			addToolBarButton("About/About");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		State state = State.createDefault();
		State stateFromFile = loadState(new File("default_state.bin"));
		if (stateFromFile != null) {
			state = stateFromFile;
		}
		mainView = new MainView(this, result3dView, state);
		result3dView = new Result3dView(state);
		result3dView.setState(mainView.getState());
		add(result3dView);
		onParameters();
		pack();
	}


	public static void main(String[] args) {
		Window mainFrame = new Window();
		mainFrame.setVisible(true);
	}

	public State loadState(File file) {
		FileInputStream fis = null;
		ObjectInputStream oin = null;
		State state = null;
		try {
			fis = new FileInputStream(file);
			oin = new ObjectInputStream(fis);
			state = (State) oin.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("error on opening file '" + file.getName() + "'");
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (oin != null) {
					oin.close();
				}
			} catch (IOException e) {
				System.err.println("error on closing file '" + file.getName() + "'");
			}
		}
		return state;
	}

	public void saveState(State state, File file) throws IOException {
		FileOutputStream fos;
		ObjectOutputStream oos;
		fos = new FileOutputStream(file);
		oos = new ObjectOutputStream(fos);
		oos.writeObject(state);
		oos.flush();
		oos.close();
	}

	public void onLoad() {
		File file = getOpenFileName("bin", "state");
		if (file == null) {
			return;
		}
		mainView.setState(loadState(file));
		result3dView.setState(mainView.getState());
	}

	public void onParameters() {
		JDialog dialog = new JDialog(this, "Set parameters", true);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				mainView.onClose();
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainView = new MainView(this, result3dView, mainView.getState());
		dialog.add(mainView);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setBounds(
			(int) (screenSize.getWidth() / 2.0 - dialog.getWidth() / 2.0),
			(int) (screenSize.getHeight() / 2.0 - dialog.getHeight() / 2.0),
			dialog.getWidth(),
			dialog.getHeight()
		);
		dialog.setVisible(true);
	}

	public void onAbout() {
		JOptionPane.showMessageDialog(
			this,
			"Task 4: Wireframe\nAuthor: Mikhail Kobtsev",
			"About",
			JOptionPane.INFORMATION_MESSAGE
		);
	}

	public void onSave() {
		File file = getSaveFileName("bin", "state");
		if (file == null) {
			return;
		}
		try {
			saveState(mainView.getState(), file);
		} catch (IOException e) {
			System.err.println("error on save operation");
		}
		remove(mainView);
	}

	public void onReset() {
		result3dView.resetAngles();
	}
}
