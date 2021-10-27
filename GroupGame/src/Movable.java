import java.awt.Graphics;
import java.awt.Point;

public class Movable extends Entity {

	// movement
	protected double dx;
	protected double dy;
	
	// constructors
	
	public Movable() {
		super();
		dx = 0;
		dy = 0;
	} // default constructor
	
	public Movable(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos);
		
		this.dx = dx;
		this.dy = dy;
	} // constructor
	
	// move the entity
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;
		
		Point[] p = getCorners();
		
		// check if the entity will be out of bounds after movement
		// if so don't move it
		if (isOutOfBounds(p)) {
			x -= dx * delta / 1000;
			y -= dy * delta / 1000;
		} // if
		
		// moves hitBox with the movable
		hitBox.setLocation((int)x, (int)y);
		
	} // move
	
	// returns true if any of the points are inside of an obstacle
	// or they are outside of the map
	public boolean isOutOfBounds(Point[] p) {
		
		Tile[][] tiles = Game.getTiles();
		
		for (int i = 0; i < p.length; i++) {
			
			// check if point is above or left of the map
			if (p[i].x < 0 || p[i].y < 0) {
				return true;
			} // if
			
			// check if is valid array index / tile coordinate
			try {
				
				// check if the tile the point is on is impassible
				if (!tiles[(int) p[i].y / TILE_LENGTH][(int) p[i].x / TILE_LENGTH].getIsPassable()) {
					return true;
				} // if
				
			} catch (ArrayIndexOutOfBoundsException e) {
				return true;  // also return true if the point is outside the map
			} // catch
			
		} // for
		
		return false;
		
	} // isOutOfBounds
	
	// set the entity's speed and direction
	
	public void setXVelocity(int xSpeed) {
		dx = xSpeed;
	} // setXVelocity
	
	public void setYVelocity(int ySpeed) {
		dy = ySpeed;
	} // setYVelocity
	
} // Movable