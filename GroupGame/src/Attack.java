import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Attack extends Movable {
	
	private double totalDistTravelled;
	private int maxDistance;
	private Character shooter;
	
	// probably shouldn't have each instance instantiate each animation....
	private Animation shot_s = new Animation(this, "animations/projectile/shot_s", ".png", 1, 8);
	private Animation shot_se = new Animation(this, "animations/projectile/shot_se", ".png", 1, 8);
	private Animation shot_sw = new Animation(this, "animations/projectile/shot_sw", ".png", 1, 8);
	private Animation shot_n = new Animation(this, "animations/projectile/shot_n", ".png", 1, 8);
	private Animation shot_ne = new Animation(this, "animations/projectile/shot_ne", ".png", 1, 8);
	private Animation shot_nw = new Animation(this, "animations/projectile/shot_nw", ".png", 1, 8);
	private Animation shot_e = new Animation(this, "animations/projectile/shot_e", ".png", 1, 8);
	private Animation shot_w = new Animation(this, "animations/projectile/shot_w", ".png", 1, 8);
	
	// constructor
	public Attack() {
		totalDistTravelled = 0;
		maxDistance = 1000;
	} // Projectile
	
	// constructor
	public Attack(String r, int xPos, int yPos, int dx, int dy, int range, Character shooter, Direction d) {
		super(r, xPos, yPos, dx, dy); // temp
		x /= TILE_LENGTH;
		y /= TILE_LENGTH;

		totalDistTravelled = 0;
		maxDistance = range;
		this.shooter = shooter;
		direction = d;
		System.out.println("string r: " + r);
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
			System.out.println(isOutOfBounds(p));
			Game.removeEntity(this);
		} // if
		
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - sprite.getHeight() - Camera.getY();
		
		hitBox.setLocation(screenPosX, screenPosY);
		
	} // move

	public void setAnimation() { // temp?
		switch (direction) {
			case S: 
				animation = shot_s;
				break;
				
			case SE:
				animation = shot_se;
				break;
				
			case SW:
				animation = shot_sw;
				break;
				
			case N:
				animation = shot_n;
				break;
				
			case NE:
				animation = shot_ne;
				break;
				
			case NW:
				animation = shot_nw;
				break;
				
			case E:
				animation = shot_e;
				break;
				
			case W:
				animation = shot_w;
				break;
			default:
				//System.out.println("unimplemented animation");
		} // switch
	} // setWalkAnimation
	
	// check if attack collides with a character
	// decrements hp of character that was collided with
	public boolean collidesWith(ArrayList<Character> characters, Graphics g){
		//System.out.println("collides with");
		for (Character c : characters) {
			
			if (hitBox.intersects(c.getHitBox()) && !c.equals(shooter)) {
				c.getHp().decrement(10);
				if (c.getHp().getValue() <= 0) {
					Game.removeEntity(c);
				} // if
				System.out.println(this.getHitBox());
				System.out.println(c.getHitBox());
				//System.out.println("intersects");
				return true;
			} // if
			
		} // for
		return false;
	} // collidesWith
	
}
