
public class Movable extends Entity {

	// movement
	protected double dx;
	protected double dy;
	
	public Movable() {
		super();
		dx = 0;
		dy = 0;
	}
	
	public Movable(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos);
		
		this.dx = dx;
		this.dy = dy;
	} // Movable
	
	public void move(long delta) {
		x += dx * delta / 1000;
		y += dy * delta / 1000;
	} // move
	
	public void setXVelocity(int xSpeed) {
		dx = xSpeed;
	} // setXVelocity
	
	public void setYVelocity(int ySpeed) {
		dy = ySpeed;
	} // setYVelocity
	
	public void setSprite(Sprite a){
		
	} // setSprite
	
	
} // Movable
