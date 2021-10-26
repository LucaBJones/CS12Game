import java.awt.Color;
import java.awt.Graphics;

public class Character extends Movable {

	// health, stamina, and mana bars
	private Bar hp;
	private Bar stamina;
	private Bar mana;

	// constructor
	public Character(String r, int xPos, int yPos, int dx, int dy) {
		super(r, xPos, yPos, dx, dy); // double or int for speed?

		// set up status bars
		hp = new Bar(10, 10, 100, Color.DARK_GRAY, Color.RED);
		stamina = new Bar(10, 30, 100, Color.DARK_GRAY, Color.GREEN);
		mana = new Bar(10, 50, 100, Color.DARK_GRAY, Color.BLUE);

	} // Player

	public Bar getHp() {
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
	public void draw(Graphics g, Camera c) {
		super.draw(g, c);
		hp.draw(g);
		stamina.draw(g);
		mana.draw(g);
	} // draw

} // Player