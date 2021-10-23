import java.awt.Graphics;
import java.awt.Point;

public class Entity {
	
	protected static final int TILE_LENGTH = 60; // should this be static?
	protected static final int X_OFFSET = 200;
	protected static final int Y_OFFSET = 250;
	
	// position of the top left corner of the tile that the sprite is "in"
	protected double x;
	protected double y;
	
	Sprite sprite; // may change later
	
	public Entity() {
		x = 0;
		y = 0;
		sprite = null; // how should this be initialized?
	}
	
	public Entity(String r, int xTile, int yTile) {
		x = xTile * TILE_LENGTH;
		y = yTile * TILE_LENGTH;
		sprite = (SpriteStore.get()).getSprite(r);
	} // constructor
	
	// returns the isometric coordinates for the cartesian coordinates passed in
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
	
	// draws the sprite to the screen
	public void draw(Graphics g, Camera c) {
		int xPos = (int) x;
		int yPos = (int) y;
		
		Point isoPoint = toIso(xPos, yPos);
		sprite.draw(g, isoPoint.x - c.getX(), isoPoint.y + TILE_LENGTH - sprite.getHeight() - c.getY());
	} // draw
	
	// draws the sprite to the screen
	public void draw(Graphics g) {
		int xPos = (int) x;
		int yPos = (int) y;
		
		Point isoPoint = toIso(xPos, yPos);
		sprite.draw(g, isoPoint.x + X_OFFSET, isoPoint.y + TILE_LENGTH - sprite.getHeight() + Y_OFFSET);
	} // draw
	
	public void setSprite(String r) {
		sprite = (SpriteStore.get()).getSprite(r);
	} // setSprite
}
