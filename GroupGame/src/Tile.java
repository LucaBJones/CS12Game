import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { // can change back to inheriting from Background if needed
	
	private boolean isOccupied;
	private boolean isPassable;
	
	public Tile(String r, int xTile, int yTile) {
		super(r, xTile, yTile);
		isOccupied = false;
	} // Tile
	
	public boolean getIsOccupied() {
		return isOccupied;
	} // getIsOccupied
	
} // Tile
