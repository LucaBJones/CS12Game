import java.awt.Point;

public class Movable extends Entity {

	// movement
	protected double dx;
	protected double dy;
	
	public Movable() {
		super();
		dx = 0;
		dy = 0;
	}
	
	public Movable(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos);
		
		this.dx = dx;
		this.dy = dy;
	} // Movable
	
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;
		
		Point[] p = getCorners();
			
		if (isOutOfBounds(p)) {
			x -= dx * delta / 1000;
			y -= dy * delta / 1000;
		} // if
		
	} // move
	
	// returns true if any of the points are inside of an inPassable tile
	// or they are outside of the map
	public boolean isOutOfBounds(Point[] p) {
		
		Tile[][] tiles = Game.getTiles();
		
		for (int i = 0; i < p.length; i++) {
			
			// check if is outside of tile map
			if (p[i].x < 0 || p[i].y < 0) {
				return true;
			} // if
			
			// check if is valid array index / tile coordinate
			try {
				
				// check if tile is not passable
				if (!tiles[(int) p[i].x / TILE_LENGTH][(int) p[i].y / TILE_LENGTH].getIsPassable()) {
				return true;
				} // if
			} catch (ArrayIndexOutOfBoundsException e) {
				return true;
			} // catch
			
		} // for
		
		return false;
	} // isOutOfBounds
	
	public void setXVelocity(int xSpeed) {
		dx = xSpeed;
	} // setXVelocity
	
	public void setYVelocity(int ySpeed) {
		dy = ySpeed;
	} // setYVelocity
	
} // Movable
