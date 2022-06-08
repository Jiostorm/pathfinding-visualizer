package utils;
import java.awt.Color;

import javax.swing.JPanel;

public class Tile extends JPanel {
	public static Tile TILE_START = null,
					   TILE_END = null;
	
	public static int TILE_DIMENSION = 25;
	public static final int PADDING = 1;
	public static boolean DISABLED = false;
	
	private final Color[] colors = {Color.gray, Color.black, Color.green, Color.red};
	
	private int[] id;
	private int colorID;
	
	public Tile(int[] id, int x, int y) {
		this.id = id;
		this.colorID = 0;
		
		this.setBounds(x, y, Tile.TILE_DIMENSION, Tile.TILE_DIMENSION);
		this.setBackground(this.colors[this.colorID]);
	}
	
	public void updateTile(int colorID) {
		this.colorID = colorID;
		this.setBackground(this.colors[this.colorID]);
		
		this.revalidate();
		this.repaint();
	}
	
	public int getCoordinate() {
		return Math.max(this.id[0], 1) * Math.max(this.id[1], 1);
	}
	
	public int[] getID() {
		return this.id;
	}
	
	public int getColorID() {
		return this.colorID;
	}
	
	public Color getColors(int id) {
		return this.colors[id];
	}
	
	@Override
	public String toString() {
		return this.id[0] + ", " + this.id[1];
	}
}
