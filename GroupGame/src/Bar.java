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
	private Color fgColor;      // colour used for filled part of bar
	private Color frameColor;

	// constructor
	Bar(int xPixel, int yPixel, int max, Color bg, Color fg, int width, int height) {
		maxValue = max;
		currentValue = maxValue;
		
		bgColor = bg;
		fgColor = fg;
		frameColor = new Color(135, 116, 104); // needed?
		
		x = xPixel;
		y = yPixel;
		
		barWidth = width;
		barHeight = height;
		
	} // Bar

	// set color of bar
	public void setForeground(Color c) {
		fgColor = c;
	} // setColor

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
		
		// draw bar background
		g.setColor(bgColor);
		g.fillRect((int) x, (int) y, barWidth, barHeight);
	
		// draw bar foreground
		g.setColor(fgColor);
		g.fillRect((int) x, (int) y, (int) (barWidth * percent), barHeight);
		
		// draw frame
		g.setColor(frameColor);
		g.drawRect((int) x, (int) y, barWidth, barHeight);
		
	} // draw
	
	public void drawInIso(Graphics g, Character c) {
		double percent = (double) currentValue / maxValue;
		
		// do these need to be variables?
		int barPosX = c.getScreenPosX() - (barWidth - c.sprite.getWidth()) / 2;
		int barPosY = c.getScreenPosY() - yOffset;
		
		g.setColor(bgColor);
		g.fillRect(barPosX, barPosY, barWidth, barHeight);

		g.setColor(fgColor);
		g.fillRect(barPosX, barPosY, (int) (barWidth * percent), barHeight);
	}
	

} // Bar