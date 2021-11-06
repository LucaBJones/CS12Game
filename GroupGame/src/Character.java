import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class Character extends Movable {

	private static HashMap<String, Integer> deaths = new HashMap<String, Integer>();
	
	// health, stamina, and mana bars
	private Bar hp;
	private Bar stamina;
	private Bar mana;
	
	private boolean isPlayer;
	private String id;
	
	// walking animations, is there a better way to do this?
	private Animation walk_s  = new Animation(this, "animations/player/walk_s", ".png", 0, 8);
	private Animation walk_se = new Animation(this, "animations/player/walk_se", ".png", 0, 8);
	private Animation walk_sw = new Animation(this, "animations/player/walk_sw", ".png", 0, 8);
	private Animation walk_n  = new Animation(this, "animations/player/walk_n", ".png", 0, 8);
	private Animation walk_ne = new Animation(this, "animations/player/walk_ne", ".png", 0, 8);
	private Animation walk_nw = new Animation(this, "animations/player/walk_nw", ".png", 0, 8);
	private Animation walk_e  = new Animation(this, "animations/player/walk_e", ".png", 0, 8);
	private Animation walk_w  = new Animation(this, "animations/player/walk_w", ".png", 0, 8);
	
	// constructor
	public Character(String r, int xPos, int yPos, int dx, int dy, boolean isPlayer) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?

		this.isPlayer = isPlayer;
		
		// set up status bars
		if (isPlayer) {
			hp = new Bar(220, 50, 100, Color.DARK_GRAY, Color.RED, 700, 35);
			stamina = new Bar(235, 100, 100, Color.DARK_GRAY, Color.GREEN, 600, 28);
			mana = new Bar(235, 140, 100, Color.DARK_GRAY, Color.BLUE, 600, 28);
		} else {
			hp = new Bar((int) x, (int) y - sprite.getHeight() - 20, 100, Color.DARK_GRAY, Color.RED, 100, 10);
		} // else
		
		animation = walk_s; // temp
		direction = Direction.S; // temp
	} // Player
	
	@Override
	public void move(long delta) {
		super.move(delta);
		
		if (!isPlayer) {
			hp.updatePosition((int) x, (int) y - sprite.getHeight() - 20);
			//System.out.println("hp bar of non-player");
		} // if
	} // move

	public Bar getHp() {
		return hp;
	}
	
	public Bar getMana() {
		return mana;
	}
	
	public Bar getStamina() {
		return stamina;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void setWalkAnimation() { // temp
		switch (direction) {
			case S: 
				animation = walk_s;
				break;
				
			case SE:
				animation = walk_se;
				break;
				
			case SW:
				animation = walk_sw;
				break;
				
			case N:
				animation = walk_n;
				break;
				
			case NE:
				animation = walk_ne;
				break;
				
			case NW:
				animation = walk_nw;
				break;
				
			case E:
				animation = walk_e;
				break;
				
			case W:
				animation = walk_w;
				break;
			default:
				//System.out.println("unimplemented animation");
		} // switch
	} // setWalkAnimation
	
	// draw the player with its health, stamina, and mana bars
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (isPlayer) {
			hp.draw(g);
			stamina.draw(g);
			mana.draw(g);
		} else {
			hp.drawInIso(g, this);
		} // else
		
	} // draw
	
	public boolean isPlayer() {
		return isPlayer;
	}
	
	public void kill() {
		int currentNum = (deaths.containsKey(id)) ? deaths.get(id) : 0;
		deaths.put(id, currentNum + 1);
		Game.removeEntity(this);
	} // kill
	
	public boolean playerCollision(ArrayList<Character> characters) {
		
		if (isPlayer) {
			for (Character c : characters) {
				
				if (hitBox.intersects(c.getHitBox()) && !c.isPlayer()) {
					hp.decrement(10);
					return true;
				} // if
				
			} // for
		}
		return false;
	}

} // Player