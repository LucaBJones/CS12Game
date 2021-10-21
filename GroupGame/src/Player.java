import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JProgressBar;

public class Player extends Movable {
	
	JProgressBar hp;
	JProgressBar stamina;
	JProgressBar mana;
	
	public Player(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?
		
		hp = new JProgressBar(0, 100);
		stamina = new JProgressBar(0, 100);
		mana = new JProgressBar(0, 100);
		
		hp.setForeground(Color.RED);
		
		Game.getStatusPanel().add(hp);
		Game.getStatusPanel().add(stamina);
		Game.getStatusPanel().add(mana);
		
	} // Player
	
	@Override
	public void draw(Graphics g) {
		int xPos = (int) x;
		int yPos = (int) y; // enable drawing of larger sprites
		
		sprite.draw(g, xPos + X_OFFSET, yPos + TILE_LENGTH - sprite.getHeight() + Y_OFFSET);
	} // draw
	
}
