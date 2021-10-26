import java.awt.Point;
import java.util.ArrayList;

public class Attack extends Movable {
	
	private double totalDistTravelled;
	private int maxDistance;
	
	// constructor
	public Attack() {
		totalDistTravelled = 0;
		maxDistance = 1000;
	} // Projectile
	
	// constructor
	public Attack(String r, int xPos, int yPos, int dx, int dy, int range) {
		super(r, xPos, yPos, dx, dy);
		x /= TILE_LENGTH;
		y /= TILE_LENGTH;
		totalDistTravelled = 0;
		maxDistance = range;
	} // Projectile
	
	// updates position of projectile
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;

		//System.out.println("projectile location, x: " + x + ", y: " + y);
		
		totalDistTravelled += Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5) / 1000;
		
		Point[] p = getCorners();
		
		// check if the entity will be out of bounds after movement
		// other condition for distance as well
		if (isOutOfBounds(p) || totalDistTravelled > maxDistance) {
			Game.removeEntity(this);
		} // if
		
		
	} // move

	
	// check if attack collides with a character
	// decrements hp of character that was collided with
	public boolean collidesWith(ArrayList<Character> characters){
		for (Character c : characters) {
			if (hitBox.intersects(c.getHitBox())) {
				c.getHp().decrement(10);
				return true;
			} // if
		} // for
		
		return false;
	} // collidesWith
}
