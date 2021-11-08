import java.awt.Graphics;
import java.awt.Point;

public class Entity {
	
	protected static final int TILE_LENGTH = 60;
	
	// position of the top left corner of the tile that the sprite is "in"
	protected double x;
	protected double y;
	
	protected int screenPosX;
	protected int screenPosY;
	
	protected Sprite sprite; 
	
	protected Animation animation;
	
	public Entity() {
		x = 0;
		y = 0;
		sprite = null; // how should this be initialized?
		animation = null;
		screenPosX = 0;
		screenPosY = 0;
	} // Entity
	
	public Entity(String r, int xTile, int yTile) {
		x = xTile;
		y = yTile;
		
		// calculate screen position
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		sprite = (SpriteStore.get()).getSprite(r);
	} // Entity
	
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
		} // if
	} // draw
	
	// draws the sprite to the screen relative to camera position
		public void draw(Graphics g, int xOffset, int yOffset) {	
			Point isoPoint = toIso((int) x, (int) y);
			
			screenPosX = isoPoint.x - Camera.getX();
			screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
			
			if (sprite != null) { // needed?
				sprite.draw(g, screenPosX + xOffset, screenPosY + yOffset);
			} // if
		} // draw
	
	public void setSprite(String r) {
		sprite = (SpriteStore.get()).getSprite(r);
	} // setSprite
	
	public int getScreenPosX() {
		return screenPosX;
	} // getScreenPosX
	
	public int getScreenPosY() {
		return screenPosY;
	} // getScrenPosY
	
	public double getX() {
		return x;
	} // getX

	public double getY() {
		return y;
	} // getY
	
} // Entity
