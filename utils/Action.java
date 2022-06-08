package utils;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Action extends JPanel {
	private JLabel label;
	
	public Action(String text, int x, int y, int width, int height) {
		this.setBounds(x, y, width, height);
		
		this.label = new JLabel(text);
		this.label.setForeground(Color.white);
		this.add(this.label);
	}
	
	public void setText(String text) {
		this.label.setText(text);
	}
	
	public String toString() {
		return this.label.getText();
	}
}
