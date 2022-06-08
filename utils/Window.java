package utils;
import javax.swing.JFrame;

public class Window {
	private JFrame frame;
	
	public Window(String title, int width, int height) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
	}
	
	public JFrame getJFrame() {
		return this.frame;
	}
}
