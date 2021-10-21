import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Bar extends Entity {
	
	private final int BAR_WIDTH = 100;
	private final int BAR_HEIGHT = 10;
	
	private int currentValue;
	private int maxValue;
	private Color bgColor;
	private Color fgColor;
	
	Bar(int xPixel, int yPixel, int max, Color bg, Color fg) {
		maxValue = max;
		currentValue = maxValue;
		bgColor = bg;
		fgColor = fg;
		
		x = xPixel;
		y = yPixel;
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
	} // decrement
	
	// increment currentValue
	public void increment(int num) {
		currentValue = Math.min(maxValue, currentValue + num);
	} // increment
	
	@Override
	public void draw(Graphics g) {
		double percent = (double) currentValue / maxValue;

		// draw bar background
		g.setColor(bgColor);
		g.fillRect((int) x, (int) y, BAR_WIDTH, BAR_HEIGHT);
		
		// draw bar foreground
		g.setColor(fgColor);
		g.fillRect((int) x, (int) y, (int) (BAR_WIDTH * percent), BAR_HEIGHT);
	} // draw
	
	
}