public enum Direction {

	N("n"),
	E("e"),
	S("s"),
	W("w"),
	NE("ne"),
	NW("nw"),
	SE("se"),
	SW("sw");
	
	private final String direction;
	
	Direction(String direction) {
		this.direction = direction;
	} // Direction
	
	public String getDirection() {
		return direction;
	} // getHorizontalDirection
	
} // Direction
