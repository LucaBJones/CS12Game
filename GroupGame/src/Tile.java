import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { // can change back to inheriting from Background if needed
	
	private static final int X_OFFSET = 200;
	private static final int Y_OFFSET = 50;
	
	private boolean isOccupied;
	private boolean isPassable;
	
	public Tile(String r, int xPos, int yPos, Game game) {
		super(r, xPos, yPos);
		
		isOccupied = false;
		
		x *= TILE_LENGTH;
		y *= TILE_LENGTH;
	} // Tile
	
	public boolean getIsOccupied() {
		return isOccupied;
	} // getIsOccupied
	
	@Override
	// draws the tile to the screen with offset
	public void draw(Graphics g) { // can probably be removed once scrolling is implemented
		int xPos = (int) x;
		int yPos = (int) y + TILE_LENGTH - sprite.getHeight(); // enable drawing of larger sprites
		
		Point isoPoint = toIso(xPos, yPos);
		sprite.draw(g, isoPoint.x + X_OFFSET, isoPoint.y + Y_OFFSET);
	} // draw
	
} // Tile
