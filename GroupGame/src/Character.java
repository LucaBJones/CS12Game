import java.awt.Color;
import java.awt.Graphics;

public class Character extends Movable {

	// health, stamina, and mana bars
	private Bar hp;
	private Bar stamina;
	private Bar mana;
	
	private boolean isPlayer;

	// constructor
	public Character(String r, int xPos, int yPos, int dx, int dy, boolean isPlayer) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?

		this.isPlayer = isPlayer;
		
		// set up status bars
		if (isPlayer) {
			hp = new Bar(10, 10, 100, Color.DARK_GRAY, Color.RED);
			stamina = new Bar(10, 30, 100, Color.DARK_GRAY, Color.GREEN);
			mana = new Bar(10, 50, 100, Color.DARK_GRAY, Color.BLUE);
		} else {
			hp = new Bar((int) x, (int) y - sprite.getHeight() - 20, 100, Color.DARK_GRAY, Color.RED);
			stamina = null;
			mana = null;
		} // else
		

	} // Player
	
	@Override
	public void move(long delta) {
		super.move(delta);
		
		if (!isPlayer) {
			hp.updatePosition((int) x, (int) y - sprite.getHeight() - 20);
			System.out.println("hp bar of non-player");
		} // if
	} // move

	public Bar getHp() {
		System.out.println("get hp");
		return hp;
	}
	
	public Bar getMana() {
		return mana;
	}
	
	public Bar getStamina() {
		return stamina;
	}
	
	public int getHpValue() {
		return hp.getValue();
	} // getHpValue

	public int getStaminaValue() {
		return stamina.getValue();
	} // getStaminaValue

	public int getManaValue() {
		return mana.getValue();
	} // getManaValue
	
	public double getX() {
		return x;
	} // getX

	public double getY() {
		return y;
	} // getY
	
	public double getXVelocity() {
		return dx;
	} // getXVelocity
	
	public double getYVelocity() {
		return dy;
	} // getYVelocity

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

} // Player