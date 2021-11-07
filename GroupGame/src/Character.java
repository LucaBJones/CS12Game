import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class Character extends Movable {

	private static HashMap<String, Integer> deaths = new HashMap<String, Integer>();
	
	// health, stamina, and mana bars
	private Bar hp;
	private Bar stamina;
	private Bar mana;
	
	private int charType; // -1 = player, 0 = skeleton, 1 = boss
	private String id; // used?
	
	// walking animations, is there a better way to do this?
	private Animation walk_s;
	private Animation walk_se;
	private Animation walk_sw;
	private Animation walk_n;
	private Animation walk_ne;
	private Animation walk_nw;
	private Animation walk_e;
	private Animation walk_w;
	
	private Animation run_s;
	private Animation run_se;
	private Animation run_sw;
	private Animation run_n;
	private Animation run_ne;
	private Animation run_nw;
	private Animation run_e;
	private Animation run_w;
	
	private Animation idle_s;
	private Animation idle_n;
	private Animation idle_e;
	private Animation idle_w;
	
	// constructor
	public Character(String r, int xPos, int yPos, int dx, int dy, int charType) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?

		this.charType = charType;
		
		// set up status bars
		if (charType == -1) {
			hp = new Bar(220, 50, 100, Color.DARK_GRAY, Color.RED, 700, 35);
			stamina = new Bar(235, 100, 100, Color.DARK_GRAY, Color.GREEN, 600, 28);
			mana = new Bar(235, 140, 100, Color.DARK_GRAY, Color.BLUE, 600, 28);
			
			// setup player animations
			walk_s  = new Animation(this, "animations/player/walk_s", ".png", 0, 7, 130);
			walk_se = new Animation(this, "animations/player/walk_se", ".png", 0, 7, 130);
			walk_sw = new Animation(this, "animations/player/walk_sw", ".png", 0, 7, 130);
			walk_n  = new Animation(this, "animations/player/walk_n", ".png", 0, 7, 130);
			walk_ne = new Animation(this, "animations/player/walk_ne", ".png", 0, 7, 130);
			walk_nw = new Animation(this, "animations/player/walk_nw", ".png", 0, 7, 130);
			walk_e  = new Animation(this, "animations/player/walk_e", ".png", 0, 7, 130);
			walk_w  = new Animation(this, "animations/player/walk_w", ".png", 0, 7, 130);
			
			run_s  = new Animation(this, "animations/player/run_s", ".png", 1, 8, 130); 
			run_se = new Animation(this, "animations/player/run_se", ".png", 1, 8, 130);
			run_sw = new Animation(this, "animations/player/run_sw", ".png", 1, 8, 130);
			run_n  = new Animation(this, "animations/player/run_n", ".png", 1, 8, 130); 
			run_ne = new Animation(this, "animations/player/run_ne", ".png", 1, 8, 130);
			run_nw = new Animation(this, "animations/player/run_nw", ".png", 1, 8, 130);
			run_e  = new Animation(this, "animations/player/run_e", ".png", 1, 8, 130); 
			run_w  = new Animation(this, "animations/player/run_w", ".png", 1, 8, 130); 
			
			idle_s = new Animation(this, "animations/player/idle_s", ".png", 1, 1, 500);
			idle_n = new Animation(this, "animations/player/idle_n", ".png", 1, 1, 500);
			idle_e = new Animation(this, "animations/player/idle_e", ".png", 1, 1, 500);
			idle_w = new Animation(this, "animations/player/idle_w", ".png", 1, 1, 500);
			
//			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
		} else {
			hp = new Bar((int) x, (int) y - sprite.getHeight() - 20, 100, Color.DARK_GRAY, Color.RED, 100, 10);
			
			String name = (charType == 1) ? "boss" : "enemy";
			
			// setup enemy animations
			walk_s  = new Animation(this, "animations/" + name + "/walk_s", ".png", 1, 8, 130); 
			walk_se = new Animation(this, "animations/" + name + "/walk_se", ".png", 1, 8, 130); 
			walk_sw = new Animation(this, "animations/" + name + "/walk_sw", ".png", 1, 8, 130); 
			walk_n  = new Animation(this, "animations/" + name + "/walk_n", ".png", 1, 8, 130);  
			walk_ne = new Animation(this, "animations/" + name + "/walk_ne", ".png", 1, 8, 130); 
			walk_nw = new Animation(this, "animations/" + name + "/walk_nw", ".png", 1, 8, 130); 
			walk_e  = new Animation(this, "animations/" + name + "/walk_e", ".png", 1, 8, 130);  
			walk_w  = new Animation(this, "animations/" + name + "/walk_w", ".png", 1, 8, 130);  
			
			idle_s = new Animation(this, "animations/" + name + "/idle_s", ".png", 0, 0, 130);
			idle_n = new Animation(this, "animations/" + name + "/idle_n", ".png", 0, 0, 130);
			idle_e = new Animation(this, "animations/" + name + "/idle_e", ".png", 0, 0, 130);
			idle_w = new Animation(this, "animations/" + name + "/idle_w", ".png", 0, 0, 130);
			
//			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, sprite.getWidth(), TILE_LENGTH);
		} // else
		
		animation = walk_s; // temp
		direction = Direction.S; // temp
		
		hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, sprite.getWidth(), TILE_LENGTH);
	} // Player
	
	@Override
	public void move(long delta) {
		super.move(delta);
		
		if (charType != -1) {
			hp.updatePosition((int) x, (int) y - sprite.getHeight() - 20);
			//System.out.println("hp bar of non-player");
		} // if
		
		if (dx == 0 && dy == 0) {
			setIdleAnimation();
		}
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
	
	public void setRunAnimation() { // temp
		switch (direction) {
			case S: 
				animation = run_s;
				break;
				
			case SE:
				animation = run_se;
				break;
				
			case SW:
				animation = run_sw;
				break;
				
			case N:
				animation = run_n;
				break;
				
			case NE:
				animation = run_ne;
				break;
				
			case NW:
				animation = run_nw;
				break;
				
			case E:
				animation = run_e;
				break;
				
			case W:
				animation = run_w; 
				break;
			default:
				//System.out.println("unimplemented animation");
		} // switch
	} // setRunAnimation
	
	public void setIdleAnimation() { 
		switch (direction) {
			case S: 
				animation = idle_s;
				break;
				
			case N:
				animation = idle_n;
				break;
				
			case E:
				animation = idle_e;
				break;
				
			case W:
				animation = idle_w; 
				break;
				
			default:
				System.out.println("unimplemented animation");
		} // switch
		
		animation.start();
	} // setIdleAnimation
	
	// draw the player with its health, stamina, and mana bars
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (charType == -1) {
			hp.draw(g);
			stamina.draw(g);
			mana.draw(g);
		} else {
			hp.drawInIso(g, this);
		} // else
		
		g.setColor(Color.red);
		g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		
	} // draw
	
	public boolean isPlayer() {
		return charType == -1;
	}
	
	public void kill() {
		int currentNum = (deaths.containsKey(id)) ? deaths.get(id) : 0;
		deaths.put(id, currentNum + 1);
		Game.removeEntity(this);
	} // kill
	
	public boolean playerCollision(ArrayList<Character> characters) {
		
		if (charType == -1) {
			for (Character c : characters) {
				
				if (hitBox.intersects(c.getHitBox()) && !c.isPlayer()) {
					hp.decrement(10);
					return true;
				} // if
				
			} // for
		}
		return false;
	}
	
	public boolean onItem(PickupItem item) {
		
		if (charType == -1) {
			Rectangle itemHitbox = new Rectangle(item.screenPosX, item.screenPosY, item.sprite.getWidth(), item.sprite.getHeight());
			if (hitBox.intersects(itemHitbox)) {
				return true;
			} // if
		} // if
		return false;
	}

} // Player