import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Movable extends Entity {

	// movement
	protected double dx;
	protected double dy;
	protected Direction direction;
	
	protected Rectangle hitBox;
	
	public Movable() {
		super();
		dx = 0;
		dy = 0;
		sprite.getWidth();
	} // Movable
	
	public Movable(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos);
		
		this.dx = dx;
		this.dy = dy;
		
		if (!(this instanceof Character)) {
			hitBox = new Rectangle(screenPosX, screenPosY, sprite.getWidth(), sprite.getHeight());
		} // if
	} // Movable
	
	// move the entity
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;
		
		Point[] p = getCorners();
		
		// set character walk animation
		if (this instanceof Character && !((Character) this).getIsDead()) {
			updateDirection();
			((Character) this).setWalkAnimation();
			animation.start();
		} // if
		
		// don't move entity if it will be out of bounds after movement
		if (isOutOfBounds(p)) {
			x -= dx * delta / 1000;
			y -= dy * delta / 1000;
			
			// set character idle animation
			if (this instanceof Character && !((Character) this).getIsDead()) {
				updateDirection();
				((Character) this).setIdleAnimation();
				animation.start();
			} // if
		} // if
		
		if (dx == 0 && dy == 0) {
			updateDirection();
			((Character) this).setIdleAnimation();
			animation.start();
		} // if
		
		// calculates screen position
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		// moves hitBox with the movable
		hitBox.setLocation((sprite.getWidth() - hitBox.width) / 2 + screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH);
	} // move
	
	// returns true if any of the points are inside of an obstacle
	// or are outside of the map
	public boolean isOutOfBounds(Point[] p) {
		Tile[][] tiles = Game.getTiles();
		int boundary = 0;
		
		if (this instanceof Attack) {
			boundary = -TILE_LENGTH;
		} // if
		
		for (int i = 0; i < p.length; i++) {
			
			// check if point is above or left of the map
			if (p[i].x < 0 || p[i].y < boundary) { 
				return true;
			} // if
			
			// check if is valid array index / tile coordinate
			try {

				// check if the tile the point is on is not passable
				if (!tiles[(int) p[i].y / TILE_LENGTH][(int) p[i].x / TILE_LENGTH].getIsPassable()) {
					return true;
				} // if
				
			} catch (ArrayIndexOutOfBoundsException e) {
				
				return true;
				
			} // catch
			
		} // for
		
		// check if character is colliding with other character
		if (this instanceof Character && !((Character) this).isPlayer()) {
			
			// any given pair is checked only once
			ArrayList<Character> chars = Game.getCharacters();
			for (int i = chars.indexOf((Character) this); i < chars.size(); i ++) {
				Character c = chars.get(i);
				if (hitBox.intersects(c.getHitBox()) && !c.isPlayer() && !c.equals(this)) {
					return true;
				} // if
			} // for
		} // if
		
		return false;
		
	} // isOutOfBounds
	
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
	
	// return the direction an entity is moving
	// if not moving in a cardinal direction (N, NE, etc.), round to the nearest one
	public void updateDirection() {
		if (!(dx == 0 && dy == 0)) {
			if (dy / dx <= 0.414 && dy / dx >= -0.414) {
				if (dx >= 0) {
					direction = Direction.SE;
				} else {
					direction = Direction.NW;
				} // else
			} else if (dy / dx >= 0.414 && dy / dx <= 2.414) {
				if (dx >= 0) {
					direction = Direction.S;
				} else {
					direction = Direction.N;
				} // else
			} else if (dy / dx >= 2.414 || dy / dx <= -2.414) {
				if (dy >= 0) {
					direction = Direction.SW;
				} else {
					direction = Direction.NE;
				} // else
			} else {
				if (dx < 0) {
					direction = Direction.W;
				} else {
					direction = Direction.E;
				} // else
			} // else if
		} // if
	} // direction
	
} // Movable
