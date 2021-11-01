import java.awt.Color;
import java.awt.Graphics;


// displays a title and a description
// (currently used in inventory but can be used for other stuff too)
public class Tooltip {
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private String title;
	private String description;
	private Color background;
	
	public Tooltip() { // needed?
		x = 0;
		y = 0;
		
		title = "";
		description = "";
		background = Color.GRAY;
		
		width = 150; // temp
		height = 80; // temp
	} // Tooltip
	
	public Tooltip(String t, String d, int xPos, int yPos) { // needed?
		title = t;
		description = d;
		background = Color.GRAY;
		
		width = 150; // temp
		height = 80; // temp
		
	} // Tooltip
	
	// draw the tooltip to the screen
	public void draw(Graphics g) {
		// draw background
		g.setColor(background);
		g.fillRect(x, y, width, height); // change to calc width/height of background 
											//depending on length of text
		
		// draw text
		g.setColor(Color.black);
		g.drawString(title, x + 10, y + 15);
		g.drawString(description, x + 10, y + 35);
	} // draw
	
	// position the tooltip relative to the values passed in
	public void position(int xPos, int yPos, int w, int h) {
		boolean below = yPos > Camera.getHeight() / 2; 	// determine section of screen the tooltip will be drawn at
		boolean left = xPos < Camera.getWidth() / 2;
		
		int xToMatch = 0;
		int yToMatch = 0;
		
		if (below) {
			if (left) {
				// at bottom left of screen
				// set bottom left corner of tooltip to top right of xPos/yPos
				xToMatch = xPos + w;
				yToMatch = yPos;
				
				x = xToMatch;
				y = yToMatch - this.height;
				System.out.println("bottom left");
			} // if
			
			// at bottom right of screen
			// set bottom right of tooltip to top left of xPos/yPos
			x = xPos - this.width;
			y = yPos - this.height;
			
		} else if (left) {
			// at top left of screen
			// set top left corner of tooltip to bottom right of xPos/yPos
			xToMatch = xPos + w;
			yToMatch = yPos + h;
			
			x = xToMatch;
			y = yToMatch;
			System.out.println("top left of screen");
			
			
		} else {
			
			// at top right of screen
			// set top right corner of tooltip to bottom left of xPos/yPos
			
			System.out.println("top right");
			
			xToMatch = xPos - this.width;
			yToMatch = yPos + h;
			
			System.out.println("xPos: " + xPos);
			System.out.println("yToMatch: " + yToMatch);
			
			x = xToMatch;
			y = yToMatch;
			
			System.out.println("tooltip, x: " + x + ", y: " + y);
			
		} // else
		
	} // position
	
} // Tooltip
