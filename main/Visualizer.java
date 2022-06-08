package main;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JComboBox;

import listeners.TileListener;
import utils.Action;
import utils.Tile;
import utils.Window;

public class Visualizer {
	private Window window;
	private Action execute, reset;
	
	private TileListener tileListener; 
	
	private JComboBox algorithmList;
	
	private Tile[][] tiles; 
	
	private Map<Tile, Boolean> visited; 
	private Thread t;
	
	private int wWidth = 1185, wHeight = 685;
	private int padding = 50;
	private boolean found = false;
	
	private int tileColumn, tileRow;
	
	private final String[] algorithms = {"Breadth-First", "Depth-First"};
	
	public Visualizer() {
		this.window = new Window("Visualizer", this.wWidth, this.wHeight);
		this.window.getJFrame().setVisible(true);
		
		init();
	}
	
	private void init() {
		this.tileListener = new TileListener(this);
		
		initActions();
		insertTiles();
	}
	
	private void initActions() {
		algorithmList = new JComboBox(algorithms);
		algorithmList.setBounds(25, 15, 150, 25);
		
		this.execute = new Action("Start", 200, 15, 75, 25);
		this.execute.setBackground(Color.black);

		this.reset = new Action("Reset", 300, 15, 75, 25);
		this.reset.setBackground(Color.black);
		
		
		this.execute.addMouseListener(this.tileListener);
		this.reset.addMouseListener(this.tileListener);
		
		this.window.getJFrame().add(algorithmList);
		this.window.getJFrame().add(this.execute);
		this.window.getJFrame().add(this.reset);
	}
	
	private void insertTiles() {
		tileColumn = this.wWidth / (Tile.TILE_DIMENSION + Tile.PADDING);
		tileRow = (this.wHeight - this.padding) / (Tile.TILE_DIMENSION + Tile.PADDING) - 1;
		
		System.out.println(tileColumn + " " + tileRow);
		this.tiles = new Tile[tileColumn][tileRow];
		for(int j = 0; j < tileRow; j++) {
			for(int i = 0; i < tileColumn; i++) {
				this.tiles[i][j] = new Tile(new int[] {i, j}, Tile.TILE_DIMENSION * (i) + (Tile.PADDING * i + 1), this.padding + Tile.TILE_DIMENSION * (j) + (Tile.PADDING * j));
				
				this.tiles[i][j].addMouseListener(this.tileListener);
				this.window.getJFrame().add(this.tiles[i][j]);
			}
		}
		updateWindow();
	}
	
	public void resetTiles() {
		for(int j = 0; j < this.tiles[0].length; j++) {
			for(int i = 0; i < this.tiles.length; i++) {
				this.tiles[i][j].updateTile(0);
			}
		}
		Tile.DISABLED = false;
		Tile.TILE_START = Tile.TILE_END = null;
	}
	
	public void startPathFinding() {
		Tile.DISABLED = true;
		this.execute.setText("Stop");

		visited = new HashMap<>();
		found = false;
		
		this.t = new Thread(new Runnable() {
			@Override
			public void run() {
				if(algorithmList.getSelectedIndex() == 0)
					bfsCall(Tile.TILE_START, Tile.TILE_END);
				else if(algorithmList.getSelectedIndex() == 1) 
					dfsCall(Tile.TILE_START, Tile.TILE_END);

				execute.setText("Start");
			}
		});
		this.t.start();
	}
	
	// PathFinding Algorithms 
	
	private void bfsCall(Tile start, Tile end) {
		Queue<Tile> tilePath = new LinkedList<>();
		
		Map<Tile, Tile> paths = new HashMap<>();
		
		tilePath.add(start);
		while(!tilePath.isEmpty() && Tile.DISABLED && !found) {
			Tile t = tilePath.poll();
			int x = t.getID()[0], y = t.getID()[1];
			
			if(t.getColorID() == 3) {
				found = true;
				break;
			}
			
			if(visited.containsKey(t))
				continue;
			
			if(!visited.isEmpty())
				t.setBackground(Color.magenta);
			visited.put(t, true);
			
			try { Thread.sleep(10); } catch (InterruptedException e) {}
			
			if(x + 1 < this.tileColumn && tiles[x + 1][y].getColorID() != 1) {
				tilePath.add(tiles[x + 1][y]);
				if(!paths.containsKey(tiles[x + 1][y]))
					paths.put(tiles[x + 1][y], t);
			}
			
			if(y + 1 < this.tileRow && tiles[x][y + 1].getColorID() != 1) {
				tilePath.add(tiles[x][y + 1]);
				if(!paths.containsKey(tiles[x][y + 1]))
					paths.put(tiles[x][y + 1], t);
			}
			
			if(x - 1 > -1 && tiles[x - 1][y].getColorID() != 1) {
				tilePath.add(tiles[x - 1][y]);
				if(!paths.containsKey(tiles[x - 1][y]))
					paths.put(tiles[x - 1][y], t);
			}
			
			if(y - 1 > -1 && tiles[x][y - 1].getColorID() != 1) {
				tilePath.add(tiles[x][y - 1]);
				if(!paths.containsKey(tiles[x][y - 1]))
					paths.put(tiles[x][y - 1], t);
			}
		}
		
		System.out.println("\n");
		System.out.println(paths.toString());
		// Tracing
		if(Tile.DISABLED && found) {
			for(Tile t = end, prev = null; !paths.isEmpty() && paths.containsKey(t) && t != start; ) {
				prev = paths.get(t);
				paths.remove(t);
				System.out.println(t + " -> " + prev);
				t = prev;
				
				if(prev != start)
					prev.setBackground(Color.yellow);
				try { Thread.sleep(10); } catch (InterruptedException e) {}
			}
		}
	}
	
	private boolean dfsCall(Tile start, Tile end) {
		int x = start.getID()[0], y = start.getID()[1];
		if(visited.containsKey(start))
			return false;

		if(start == end) {
			found = true;
			return true;
		}
		
		if(!visited.isEmpty())
			start.setBackground(Color.magenta);
		visited.put(start, true);
		
		
		try { Thread.sleep(10); } catch (InterruptedException e) {}

		if(x + 1 < this.tileColumn && Tile.DISABLED && !found) {
			if(tiles[x + 1][y].getColorID() != 1)
				found = dfsCall(tiles[x + 1][y], end);
		}
		
		if(y + 1 < this.tileRow && Tile.DISABLED && !found) {
			if(tiles[x][y + 1].getColorID() != 1)
				found = dfsCall(tiles[x][y + 1], end);
		}
		
		if(y - 1 > -1 && Tile.DISABLED && !found) {
			if(tiles[x][y - 1].getColorID() != 1)
				found = dfsCall(tiles[x][y - 1], end);
		}

		if(x - 1 > -1 && Tile.DISABLED && !found) {
			if(tiles[x - 1][y].getColorID() != 1)
				found = dfsCall(tiles[x - 1][y], end);
		}
		
		try { Thread.sleep(5); } catch (InterruptedException e) {}
		
		if(Tile.DISABLED && found && start.getColorID() < 1)
			start.setBackground(Color.yellow);
		return found;
	}
	
	private void updateWindow() {
		this.window.getJFrame().revalidate();
		this.window.getJFrame().repaint();
	}
	
	public static void main(String[] args) {
		new Visualizer();
	}
}
