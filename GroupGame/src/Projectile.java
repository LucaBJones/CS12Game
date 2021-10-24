import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Projectile extends Movable {
	
	private double totalDistTravelled;
	final private int maxDistance = 1000;
	
	// constructor
	public Projectile() {
		totalDistTravelled = 0;
	} // Projectile
	
	// constructor
	public Projectile(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos, dx, dy);
		totalDistTravelled = 0;
	} // Projectile
	
//	// updates position of projectile
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;
//
//		System.out.println("projectile moving, dx: " + dx + ", dy: " + dy);
//		
//		totalDistTravelled += Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5) / 1000;
//		
////		Point[] p = getCorners();
////		
////		// check if the entity will be out of bounds after movement
////		// other condition for distance as well
////		if (isOutOfBounds(p) || totalDistTravelled > maxDistance) {
////			Game.removeEntity(this);
////		} // if
//		
//		
	} // move
	
	// temp
	public void draw(Graphics g, Camera c) {
		super.draw(g);
		
		System.out.println("draw projectile, x: " + x + ", y: " + y);
	}
}
