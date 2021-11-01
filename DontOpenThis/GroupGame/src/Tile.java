import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { 
	
	private boolean isPassable;
	
	// constructor
	public Tile(String r, int xTile, int yTile, boolean isPassable) {
		super(r, xTile, yTile);
		this.isPassable = isPassable;
	} // Tile
	
	// return true if the tile can be passed over
	public boolean getIsPassable() {
		return isPassable;
	} // getIsOccupied
	
} // Tile