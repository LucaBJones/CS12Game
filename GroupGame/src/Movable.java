import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Movable extends Entity {

	// movement
	protected double dx;
	protected double dy;
	protected Rectangle hitBox;
	
	protected Direction direction; 
	
	public Movable() {
		super();
		dx = 0;
		dy = 0;
		sprite.getWidth();
	} // default constructor
	
	public Movable(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos);
		
		this.dx = dx;
		this.dy = dy;
		
		Point isoPoint = toIso((int) x, (int) y);
		
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		// these should be in their respective classes, but are here rn to look at
		
		if (this instanceof Character) {
			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, sprite.getWidth(), TILE_LENGTH);
		} else {
			hitBox = new Rectangle(screenPosX, screenPosY, sprite.getWidth(), sprite.getHeight());
		}
		
		//System.out.println(screenPosX + " " + screenPosY);
		//System.out.println("construct movable");
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
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		// moves hitBox with the movable
		hitBox.setLocation(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH);
	} // move
	
	// returns true if any of the points are inside of an obstacle
	// or they are outside of the map
	public boolean isOutOfBounds(Point[] p) {
		
		Tile[][] tiles = Game.getTiles();
		
		for (int i = 0; i < p.length; i++) {
			
			// check if point is above or left of the map
			// apparently y < 0 doesn't work
			if (p[i].x < 0 || p[i].y < -TILE_LENGTH) { 
				System.out.println("help");
				return true;
			} // if
			
			// check if is valid array index / tile coordinate
			try {
				//System.out.println(!tiles[(int) p[i].y / TILE_LENGTH][(int) p[i].x / TILE_LENGTH].getIsPassable());
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
	
	public double getXVelocity() {
		return dx;
	} // getXVelocity
	
	public double getYVelocity() {
		return dy;
	} // getYVelocity
	
	public void setDirection(Direction d) {
		direction = d;
	} // setDirection
	
	public Rectangle getHitBox() {
		return hitBox;
	} // getHitBox
	
	public void drawHitbox(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
	}
	
} // Movable