import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Entity {
	
	protected static final int TILE_LENGTH = 60;
	
	// position of the top left corner of the tile that the sprite is "in"
	protected double x;
	protected double y;
	
	Sprite sprite; // may change later
	protected Rectangle hitBox;
	
	// constructors
	
	public Entity() {
		x = 0;
		y = 0;
		sprite = null; // how should this be initialized?
		
		hitBox = new Rectangle((int) x, (int) y, TILE_LENGTH, TILE_LENGTH);
	} // default constructor
	
	public Entity(String r, int xTile, int yTile) {
		x = xTile * TILE_LENGTH;
		y = yTile * TILE_LENGTH;
		sprite = (SpriteStore.get()).getSprite(r);
		hitBox = new Rectangle((int) x, (int) y, TILE_LENGTH, TILE_LENGTH);
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
		int xPos = (int) x;
		int yPos = (int) y;
		
		Point isoPoint = toIso(xPos, yPos);
		sprite.draw(g, isoPoint.x - Camera.getX(), isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY());
	} // draw
	
	public void setSprite(String r) {
		sprite = (SpriteStore.get()).getSprite(r);
	} // setSprite
	
	public Rectangle getHitBox() {
		return hitBox;
	} // getHitBox
	
} // Entity