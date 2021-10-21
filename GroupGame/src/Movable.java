
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
	
}
