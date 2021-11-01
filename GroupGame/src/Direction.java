
public enum Direction {

	// fix nums
	N(1,0),
	E(1,0),
	S(1,0),
	W(1,0),
	NE(1,0),
	NW(1,0),
	SE(1,0),
	SW(1,0);
	
	private final int horizontalDirection;
	private final int verticalDirection;
	
	Direction(int horizontalDirection, int verticalDirection) {
		this.horizontalDirection = horizontalDirection;
		this.verticalDirection = verticalDirection;
	} // Direction
	
	public int getHorizontalDirection() {
		return horizontalDirection;
	} // getHorizontalDirection
	
	public int getVerticalDirection() {
		return verticalDirection;
	} // getVerticalDirection
	
} // Direction