import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { // can change back to inheriting from Background if needed
	
	private static final int TILE_HEIGHT = 60; // should this be static?
	private static final int TILE_WIDTH = 60; // only use 1 var for height and width?
	
	private static final int X_OFFSET = 160;
	private static final int Y_OFFSET = 50;
	
	private int xPixel;
	private int yPixel;
	
	private Game game; // should this be in entity?
	private boolean isOccupied;
	
	public Tile(String r, int xPos, int yPos, Game game) {
		super(r, xPos, yPos);
		isOccupied = false;
		this.game = game;  
		
		xPixel = (int) x * TILE_WIDTH;
		yPixel = (int) y * TILE_HEIGHT;
	} // Tile
	
	public boolean getIsOccupied() {
		return isOccupied;
	} // getIsOccupied
	
	// draws the sprite to the screen
	@Override
	public void draw(Graphics g) {
		Point isoPoint = game.toIso(xPixel + X_OFFSET, yPixel + Y_OFFSET);
		
		sprite.draw(g, isoPoint.x, isoPoint.y);
	} // draw
	
} // Tile
