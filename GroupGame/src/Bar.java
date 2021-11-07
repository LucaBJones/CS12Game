import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Bar extends Entity {

	private int barWidth;
	private int barHeight;
	
	private int yOffset = 20; // final?

	private int currentValue;   // how much the bar is currently filled
	private int maxValue;       // how much the bar is filled when full
	private Color bgColor;      // colour used for empty part of bar
	
	private Sprite frame;

	// constructor
	Bar(int xPixel, int yPixel, int max, String frameRef, String barRef, int width, int height) {
		maxValue = max;
		currentValue = maxValue;
		
		bgColor = Color.DARK_GRAY;
		
		x = xPixel;
		y = yPixel;
		
		barWidth = width;
		barHeight = height;
		
		frame = SpriteStore.get().getSprite(frameRef);
		sprite = SpriteStore.get().getSprite(barRef);
		
	} // Bar

	// set maxValue
	public void setMaxValue(int max) {
		maxValue = max;
	} // setMaxValue

	// decrement currentValue
	public void decrement(int num) {
		currentValue = Math.max(0, currentValue - num);
		System.out.println("current value: " + currentValue);
	} // decrement

	// increment currentValue
	public void increment(int num) {
		currentValue = Math.min(maxValue, currentValue + num);
	} // increment
	
	// return the current value
	public int getValue() {
		return currentValue;
	} // getValue
	
	public void updatePosition(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}

	// draw the bar (too obvious?) 
	@Override
	public void draw(Graphics g) {
		double percent = (double) currentValue / maxValue;
	
		// draw bar foreground
		sprite.draw(g, (int) x, (int) y, barWidth, barHeight);
		
		// draw bar background
		g.setColor(bgColor);
		g.fillRect((int) (x + barWidth * percent), (int) y + 5, (int) (barWidth * (1 - percent)), barHeight - 10);
		
		// draw frame
		frame.draw(g, (int) x, (int) y, barWidth, barHeight);
		
	} // draw
	
	public void drawInIso(Graphics g, Character c) {
		double percent = (double) currentValue / maxValue;
		
		// calculate position
		int barPosX = c.getScreenPosX() - (barWidth - c.sprite.getWidth()) / 2;
		int barPosY = c.getScreenPosY() - yOffset;
		
		// draw background
		g.setColor(bgColor);
		g.fillRect(barPosX, barPosY, barWidth, barHeight);

		// draw foreground
		g.setColor(new Color(160, 20, 5));
		g.fillRect(barPosX, barPosY, (int) (barWidth * percent), barHeight);
		
		// draw frame
		frame.draw(g, barPosX, barPosY, barWidth, barHeight);
	} // drawInIso
	

} // Bar