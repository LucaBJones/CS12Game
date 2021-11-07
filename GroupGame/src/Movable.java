import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

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
		int boundary = 0;
		
		if(this instanceof Attack) {
			boundary = -TILE_LENGTH;
		} // if
		
		for (int i = 0; i < p.length; i++) {
			
			// check if point is above or left of the map
			// apparently y < 0 doesn't work
			if (p[i].x < 0 || p[i].y < boundary) { 
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
		
		if (this instanceof Character && !((Character) this).isPlayer()) {
			// this makes it so that a given pair is run only once
			ArrayList<Character>chars = Game.getCharacters();
			for (int i = chars.indexOf((Character) this); i < chars.size(); i ++) {//Character c : Game.characters
				Character c = chars.get(i);
				if (hitBox.intersects(c.getHitBox()) && !c.isPlayer() && !c.equals(this)) {
					return true;
				} // if
			} // for
		} // if
		
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
		
	}
	
	// return the direction an entity is moving
	// if not moving in a cardinal direction (N, NE, etc.), round to the nearest one
	public void updateDirection() {
		if (dy / dx <= 0.414 && dy / dx >= -0.414) {
			if (dx >= 0) {
				direction = Direction.SE;
			} else {
				direction = Direction.NW;
			}
		} else if (dy / dx >= 0.414 && dy / dx <= 2.414) {
			if (dx >= 0) {
				direction = Direction.S;
			} else {
				direction = Direction.N;
			}
		} else if (dy / dx >= 2.414 || dy / dx <= -2.414) {
			if (dy >= 0) {
				direction = Direction.SW;
			} else {
				direction = Direction.NE;
			}
		} else {
			if (dx < 0) {
				direction = Direction.W;
			} else {
				direction = Direction.E;
			}
		} // if
	} // direction
	
} // Movable
