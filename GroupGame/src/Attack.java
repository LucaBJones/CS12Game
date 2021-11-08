import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Attack extends Movable {
	
	private double totalDistTravelled; 
	private int maxDistance;
	
	public Attack() {
		totalDistTravelled = 0;
		maxDistance = 1000;
	} // Attack
	
	public Attack(String r, int xPos, int yPos, int dx, int dy, int range, Character shooter) {
		super(r, xPos, yPos, dx, dy);
		x /= TILE_LENGTH;
		y /= TILE_LENGTH;

		totalDistTravelled = 0;
		maxDistance = range;
	} // Attack
	
	// updates position of the attack
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;

		totalDistTravelled += Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5) / 1000;
		
		Point[] p = getCorners();
		
		// remove attack if it will be out of bounds after movement
		if (isOutOfBounds(p) || totalDistTravelled > maxDistance) {
			System.out.println(isOutOfBounds(p));
			Game.removeEntity(this);
			Game.spawnExplosion((int) x, (int) y);
		} // if
		
		// convert position to isometric coordinates
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		// move the attack's hitBox with it 
		hitBox.setLocation(screenPosX, screenPosY);
		
	} // move
	
	// checks if attack collides with a character
	public boolean collidesWith(ArrayList<Character> characters, Graphics g){
		for (Character c : characters) {
			if (hitBox.intersects(c.getHitBox())) {
				
				// decrements hp of character that was collided with
				c.takeDamage(10);
				Game.spawnExplosion((int) x, (int) y);
				
				return true;
			} // if
		} // for
		
		return false;
	} // collidesWith
	
} // Attack
