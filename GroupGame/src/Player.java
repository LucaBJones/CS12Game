import java.awt.Color;
import java.awt.Graphics;

public class Player extends Movable {
	
	private Bar hp;
	private Bar stamina;
	private Bar mana;
	
	public Player(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?
		
		
		// set up status bars
		hp = new Bar(10, 10, 100, Color.DARK_GRAY, Color.RED);
		stamina = new Bar(10, 30, 100, Color.DARK_GRAY, Color.GREEN);
		mana = new Bar(10, 50, 100, Color.DARK_GRAY, Color.BLUE);

		
	} // Player
	
	public Bar getHp() { // temp?
		return hp;
	} 
	
	@Override
	public void draw(Graphics g) {
		int xPos = (int) x;
		int yPos = (int) y; // enable drawing of larger sprites
		
		sprite.draw(g, xPos + X_OFFSET, yPos + TILE_LENGTH - sprite.getHeight() + Y_OFFSET);
		
		hp.draw(g);
		stamina.draw(g);
		mana.draw(g);
	} // draw

}
