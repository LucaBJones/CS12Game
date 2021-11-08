import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

public class Character extends Movable {
	
	private static HashMap<String, Integer> killRecord = new HashMap<String, Integer>();
	
	// health, stamina, and mana bars
	private Bar hp;
	private Bar stamina;
	private Bar mana;
	
	private int charType; // -1 = player, 0 = skeleton, 1 = boss, 2 = krampus
	private boolean isDead;
	
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
			hp = new Bar(210, 50, 100000, "ui/frame1.png", "ui/hpBar.png", 700, 35);
			stamina = new Bar(225, 100, 100000, "ui/frame2.png", "ui/staminaBar.png", 600, 28);
			mana = new Bar(225, 140, 1000000, "ui/frame2.png", "ui/manaBar.png", 600, 28);
			
			// setup player animations
			walk_s  = new Animation(this, "animations/player/walk_s", ".png", 0, 7, 130, true);
			walk_se = new Animation(this, "animations/player/walk_se", ".png", 0, 7, 130, true);
			walk_sw = new Animation(this, "animations/player/walk_sw", ".png", 0, 7, 130, true);
			walk_n  = new Animation(this, "animations/player/walk_n", ".png", 0, 7, 130, true);
			walk_ne = new Animation(this, "animations/player/walk_ne", ".png", 0, 7, 130, true);
			walk_nw = new Animation(this, "animations/player/walk_nw", ".png", 0, 7, 130, true);
			walk_e  = new Animation(this, "animations/player/walk_e", ".png", 0, 7, 130, true);
			walk_w  = new Animation(this, "animations/player/walk_w", ".png", 0, 7, 130, true);
			
			run_s  = new Animation(this, "animations/player/run_s", ".png", 1, 8, 130, true); 
			run_se = new Animation(this, "animations/player/run_se", ".png", 1, 8, 130, true);
			run_sw = new Animation(this, "animations/player/run_sw", ".png", 1, 8, 130, true);
			run_n  = new Animation(this, "animations/player/run_n", ".png", 1, 8, 130, true); 
			run_ne = new Animation(this, "animations/player/run_ne", ".png", 1, 8, 130, true);
			run_nw = new Animation(this, "animations/player/run_nw", ".png", 1, 8, 130, true);
			run_e  = new Animation(this, "animations/player/run_e", ".png", 1, 8, 130, true); 
			run_w  = new Animation(this, "animations/player/run_w", ".png", 1, 8, 130, true); 
			
			idle_s = new Animation(this, "animations/player/idle_s", ".png", 1, 1, 500, false);
			idle_n = new Animation(this, "animations/player/idle_n", ".png", 1, 1, 500, false);
			idle_e = new Animation(this, "animations/player/idle_e", ".png", 1, 1, 500, false);
			idle_w = new Animation(this, "animations/player/idle_w", ".png", 1, 1, 500, false);
			
			
			// fix this**** (fix moving of hitbox too)
			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, 30, 30);
		} else if (charType == 0 || charType == 1) {
			hp = new Bar((int) x, (int) y - sprite.getHeight() - 20, 100, "ui/frame2.png", "ui/hpBar.png", 100, 10);
			
			// setup enemy animations
			walk_s  = new Animation(this, "animations/" + getName() + "/walk_s", ".png", 1, 8, 130, true); 
			walk_se = new Animation(this, "animations/" + getName() + "/walk_se", ".png", 1, 8, 130, true); 
			walk_sw = new Animation(this, "animations/" + getName() + "/walk_sw", ".png", 1, 8, 130, true); 
			walk_n  = new Animation(this, "animations/" + getName() + "/walk_n", ".png", 1, 8, 130, true);  
			walk_ne = new Animation(this, "animations/" + getName() + "/walk_ne", ".png", 1, 8, 130, true); 
			walk_nw = new Animation(this, "animations/" + getName() + "/walk_nw", ".png", 1, 8, 130, true); 
			walk_e  = new Animation(this, "animations/" + getName() + "/walk_e", ".png", 1, 8, 130, true);  
			walk_w  = new Animation(this, "animations/" + getName() + "/walk_w", ".png", 1, 8, 130, true);  
			
			idle_s = new Animation(this, "animations/" + getName() + "/idle_s", ".png", 0, 0, 130, false);
			idle_n = new Animation(this, "animations/" + getName() + "/idle_n", ".png", 0, 0, 130, false);
			idle_e = new Animation(this, "animations/" + getName() + "/idle_e", ".png", 0, 0, 130, false);
			idle_w = new Animation(this, "animations/" + getName() + "/idle_w", ".png", 0, 0, 130, false);
			
			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight() - TILE_LENGTH, sprite.getWidth(), TILE_LENGTH);
		} else {
			hp = new Bar((int) x, (int) y - sprite.getHeight() - 20, 100, "ui/frame2.png", "ui/hpBar.png", 100, 10);
			
			// setup enemy animations
			walk_s  = new Animation(this, "animations/" + getName() + "/walk_s", ".png", 0, 7, 130, true); 
			walk_se = new Animation(this, "animations/" + getName() + "/walk_se", ".png", 0, 7, 130, true); 
			walk_sw = new Animation(this, "animations/" + getName() + "/walk_sw", ".png", 0, 7, 130, true); 
			walk_n  = new Animation(this, "animations/" + getName() + "/walk_n", ".png", 0, 7, 130, true);  
			walk_ne = new Animation(this, "animations/" + getName() + "/walk_ne", ".png", 0, 7, 130, true); 
			walk_nw = new Animation(this, "animations/" + getName() + "/walk_nw", ".png", 0, 7, 130, true); 
			walk_e  = new Animation(this, "animations/" + getName() + "/walk_e", ".png", 0, 7, 130, true);  
			walk_w  = new Animation(this, "animations/" + getName() + "/walk_w", ".png", 0, 7, 130, true);  
			
			idle_s = new Animation(this, "animations/" + getName() + "/walk_s", ".png", 0, 0, 130, false);
			idle_n = new Animation(this, "animations/" + getName() + "/walk_n", ".png", 0, 0, 130, false);
			idle_e = new Animation(this, "animations/" + getName() + "/walk_e", ".png", 0, 0, 130, false);
			idle_w = new Animation(this, "animations/" + getName() + "/walk_w", ".png", 0, 0, 130, false);
			
			hitBox = new Rectangle(screenPosX, screenPosY + sprite.getHeight(), sprite.getWidth() / 2, TILE_LENGTH);
			
		}
		
		animation = idle_s; // temp
		direction = Direction.S; // temp
		isDead = false;
		
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
			animation.start();
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
//				System.out.println("unimplemented animation: " + direction.getDirection());
		} // switch
		if (charType >= 0) {
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
			
			case SW: 
				animation = idle_s;
				break;
				
			case NE:
				animation = idle_n;
				break;
				
			case SE:
				animation = idle_e;
				break;
				
			case NW:
				animation = idle_w; 
				break;
				
			default:
//				System.out.println("unimplemented animation: " + direction.getDirection());
			} // switch
		} // if
	} // setIdleAnimation
	
	private void setDeathAnimation() {
		animation = new Animation(this, "animations/" + getName() + "/die_" + direction.getDirection(), ".png", 0, 2, 100, false);
		animation.start();
	}

	private String getName() {
		String name = "";
		
		switch (charType) {
			case -1:
				name = "player";
				break;
			case 1: 
				name = "boss";
				break;
			case 2:
				name = "krampus";
				break;
			default:
				name = "enemy";
		} // switch
		
		return name;
	} // getName
	
	// draw the player with its health, stamina, and mana bars
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (charType == -1) {
			hp.draw(g);
			stamina.draw(g);
			mana.draw(g);
		} else {
			if (isDead) { return; }
			hp.drawInIso(g, this);
		} // else
		
	} // draw
	
	public boolean isPlayer() {
		return charType == -1;
	}
	
	public void die() {
		isDead = true;
		
		// record death
		int currentNum = (killRecord.containsKey(getName())) ? killRecord.get(getName()) : 0;
		killRecord.put(getName(), currentNum + 1);
		
		System.out.println("dead: " + charType);
		setDeathAnimation();

		// if not player, remove after a while
		if (charType == 0 || charType == 2) {
			Timer timer = new Timer(3000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					remove();
				} // actionPerformed
			});
			
			timer.start();
		} else if (charType == -1) {
			Timer timer = new Timer(2000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Game.gameOver();
				} // actionPerformed
			});
			
			timer.setRepeats(false);
			timer.start();
		} else if (charType == 1) {
			Timer timer = new Timer(2000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Game.gameWon();
				} // actionPerformed
			});
			
			timer.setRepeats(false);
			timer.start();
		}
		
	} // kill

	public boolean playerCollision(ArrayList<Character> characters) {
		
		if (charType == -1) {
			for (Character c : characters) {
				
				if (hitBox.intersects(c.getHitBox()) && !c.isPlayer()) {
					takeDamage(10); // vars for amount of damage instead?
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
	
	public void takeDamage(int n) {
		if (isDead) { return; }
		
		hp.decrement(n);
		
		if (hp.getValue() <= 0) {
			die();
		} // if
	} //
	
	public boolean getIsDead() {
		return isDead;
	}
	
	private void remove() {
		Game.removeEntity(this);
	}

	// returns the number a character that has been killed
	// returns -1 if no death for that character has been recorded
	public static int getKills(String characterName) {
		if (killRecord.containsKey(characterName)) {
			return killRecord.get(characterName);
		} // if
		
		return -1;
	} // getKills
	
} // Player