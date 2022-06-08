package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.Visualizer;
import utils.Action;
import utils.Tile;

public class TileListener implements MouseListener {
	private Visualizer visualizer;
	private Tile t;
	
	private boolean isClicked = false;
	private int mode = 0;
	
	public TileListener(Visualizer visualizer) {
		this.visualizer = visualizer;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!Tile.DISABLED && e.getButton() == MouseEvent.BUTTON1 && this.t != null) {
			this.t.updateTile((this.t.getColorID() + 1) % 2);
		}
		
		if(!Tile.DISABLED && e.getButton() == MouseEvent.BUTTON3 && this.t != null) {
			if(Tile.TILE_START == null && this.t != Tile.TILE_END) {
				Tile.TILE_START = this.t;
				this.t.updateTile(2);
			} else if(Tile.TILE_END == null) {
				if(this.t == Tile.TILE_START)
					Tile.TILE_START = null;
				Tile.TILE_END = this.t;
				this.t.updateTile(3);
			} else {
				if(this.t == Tile.TILE_END)
					Tile.TILE_END = null;
				this.t.updateTile(0);
			}
		}
		
		if(e.getSource() instanceof Action) {
			if(e.getSource().toString().equals("Reset")) {
				this.visualizer.resetTiles();
			} else if(e.getSource().toString().equals("Start")) {
				if(Tile.TILE_START != null && Tile.TILE_END != null) {
					this.visualizer.startPathFinding();
				}
			} else if(e.getSource().toString().equals("Stop")) {
				Tile.DISABLED = false;
				((Action) e.getSource()).setText("Start");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(Tile.DISABLED)
			return;
		this.isClicked = true;
		this.mode = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(Tile.DISABLED)
			return;
		this.isClicked = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(Tile.DISABLED)
			return;
		if(!(e.getSource() instanceof Tile))
			return;
		this.t = (Tile) e.getSource();
		if(this.isClicked && this.mode == MouseEvent.BUTTON1) {
			this.t.updateTile((t.getColorID() + 1) % 2);
			if(this.t == Tile.TILE_START)
				Tile.TILE_START = null;
			if(this.t == Tile.TILE_END)
				Tile.TILE_END = null;
		}
		System.out.println(this.t);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.t = null;
	}
}
