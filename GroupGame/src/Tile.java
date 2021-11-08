import java.awt.Graphics;
import java.awt.Point;

public class Tile extends Entity { 
	
	private boolean isPassable;
	private Sprite obstacle;
	
	// constructor
	public Tile(String floorTile, String obstacleRef, int xTile, int yTile, boolean isPassable) {
		super(floorTile, xTile, yTile);
		this.isPassable = isPassable;
		
		if (obstacleRef != null) {
			obstacle = SpriteStore.get().getSprite(obstacleRef);
		} // if
		
	} // Tile
	
	// return true if the tile can be passed over
	public boolean getIsPassable() {
		return isPassable;
	} // getIsOccupied
	
	public Sprite getObs() {
		return obstacle;
	} // getObs
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		if (obstacle != null) {
			Point isoPoint = toIso((int) x, (int) y);
			
			// calculate position of obstacle
			screenPosX = isoPoint.x - Camera.getX();
			screenPosY = isoPoint.y + TILE_LENGTH - obstacle.getHeight() - Camera.getY();
		
			// draw obstacle
			obstacle.draw(g, screenPosX, screenPosY);
		} // if
	} // draw
	
} // Tile