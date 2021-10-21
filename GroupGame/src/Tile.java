import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { 
	
	private boolean isPassable;
	
	public Tile(String r, int xTile, int yTile, boolean isPassable) {
		super(r, xTile, yTile);
		this.isPassable = isPassable;
	} // Tile
	
	public boolean getIsPassable() {
		return isPassable;
	} // getIsOccupied
	
} // Tile
