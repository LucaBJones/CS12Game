import java.awt.Point;

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
		
		for (int i = 0; i < 4; i++) {
			Point p = getCorners()[i];
			
			if (!isOutOfBounds(p)) {
				x -= dx * delta / 1000;
				y -= dy * delta / 1000;
			}
			
		}
		
	} // move
	
	// needs to be fixed
	public boolean isOutOfBounds(Point p) {
		
//		Tile[][] tiles = Game.getTiles();
//		
//		// check if the point is on any of the passable tiles
//		for (int i = 0; i < tiles.length; i++) {
//			for (int j = 0; j < tiles[i].length; j++) {
//				Tile tile = tiles[i][j];
//				
//				if(tile.getIsPassable()) {
//					Point[] tileCorners = tile.getCorners();
//					if (p.getY() > tileCorners[0].getY() && p.getY() < tileCorners[1].getY() && p.getX() > tileCorners[2].getX() && p.getX() < tileCorners[3].getX()) {
//						return true;
//					}
//				}
//				
//			}
//		}
//		
//		return false;
		
		return true;
	}
	
	public void setXVelocity(int xSpeed) {
		dx = xSpeed;
	} // setXVelocity
	
	public void setYVelocity(int ySpeed) {
		dy = ySpeed;
	} // setYVelocity
	
} // Movable
