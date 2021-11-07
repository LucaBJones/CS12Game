import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Entity {
	
	protected static final int TILE_LENGTH = 60;
	
	// position of the top left corner of the tile that the sprite is "in"
	protected double x;
	protected double y;
	
	protected int screenPosX;
	protected int screenPosY;
	
	
	Sprite sprite; // may change later, should it be private?
	
	protected Animation animation;
	
	
	public Entity() {
		x = 0;
		y = 0;
		sprite = null; // how should this be initialized?
		animation = null;
		screenPosX = 0;
		screenPosY = 0;
		
	} // default constructor
	
	public Entity(String r, int xTile, int yTile) {
		x = xTile; // * TILE_LENGTH
		y = yTile; // " "
		Point isoPoint = toIso((int) x, (int) y);
		
		
		sprite = (SpriteStore.get()).getSprite(r);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
	} // constructor
	
	// convert cartesian to isometric
	public Point toIso(int x, int y) {
		int isoX = x - y;
		int isoY = (x + y) / 2;
		return new Point(isoX, isoY);
	} // toIso
	
	// gets the four corners of the "tile" in cartesian coordinates
	public Point[] getCorners() {
		Point[] corners = new Point[4];
		corners[0] = new Point((int) x, (int) y); 	// top left
		corners[1] = new Point((int) x + TILE_LENGTH, (int) y); // top right
		corners[2] = new Point((int) x, (int) y + TILE_LENGTH); // bottom left
		corners[3] = new Point((int) x + TILE_LENGTH, (int) y + TILE_LENGTH); // bottom right
		return corners;
	} // getCorners
	
	// draws the sprite to the screen relative to camera position
	public void draw(Graphics g) {	
		Point isoPoint = toIso((int) x, (int) y);
		
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		if (sprite != null) { // needed?
			sprite.draw(g, screenPosX, screenPosY);
		}

	} // draw
	
	// draws the sprite to the screen relative to camera position
		public void draw(Graphics g, int xOffset, int yOffset) {	
			Point isoPoint = toIso((int) x, (int) y);
			
			screenPosX = isoPoint.x - Camera.getX();
			screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
			
			if (sprite != null) { // needed?
				sprite.draw(g, screenPosX + xOffset, screenPosY + yOffset);
			}

		} // draw
	
	public void setSprite(String r) {
		sprite = (SpriteStore.get()).getSprite(r);
	} // setSprite
	
	public void setAnimation(String animID) { // temp
		animation = SpriteStore.get().getAnimation(animID);
	}
	
	public int getScreenPosX() {
		return screenPosX;
	}
	
	public int getScreenPosY() {
		return screenPosY;
	}
	
	public double getX() {
		return x;
	} // getX

	public double getY() {
		return y;
	} // getY
	
} // Entity