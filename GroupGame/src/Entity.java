import java.awt.Graphics;

public class Entity {
	
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
	
	// draws the sprite to the screen
	public void draw(Graphics g) {
		sprite.draw(g, (int) x, (int) y);
	} // draw
}
