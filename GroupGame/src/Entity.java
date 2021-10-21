import java.awt.Graphics;
import java.awt.Point;

public class Entity {
	
	protected static final int TILE_LENGTH = 60; // should this be static?
	
	// position
	protected double x;
	protected double y;
	
	Sprite sprite; // may change later
	
	public Entity() {
		x = 0;
		y = 0;
//		sprite = null; // how should this be initialized?
	}
	
	public Entity(String r, int xPos, int yPos) {
		x = xPos;
		y = yPos;
		sprite = (SpriteStore.get()).getSprite(r);
	} // constructor
	
	// returns the isometric coordinates for the cartesian coordinates passed in
	public Point toIso(int x, int y) {
		int isoX = x - y;
		int isoY = (x + y) / 2;
		return new Point(isoX, isoY);
	} // toIso
	
	// draws the sprite to the screen
	public void draw(Graphics g) {
		int xPos = (int) x;
		int yPos = (int) y + TILE_LENGTH - sprite.getHeight(); // enable drawing of larger sprites
		
		Point isoPoint = toIso(xPos, yPos);
		sprite.draw(g, isoPoint.x, isoPoint.y);
	} // draw
}
