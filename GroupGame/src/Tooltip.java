import java.awt.Color;
import java.awt.Graphics;

public class Tooltip {
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private String title;
	private String description;
	private Color background;
	
	private Sprite box;
	
	public Tooltip() {
		x = 0;
		y = 0;
		
		title = "";
		description = "";
		background = Color.LIGHT_GRAY;
		
		width = (int) (Camera.getWidth() * 0.2);
		height = (int) (Camera.getHeight() * 0.24);
		
		box = SpriteStore.get().getSprite("ui/tooltip.png");
	} // Tooltip
	
	// draw the tooltip to the screen
	public void draw(Graphics g) {
		
		// draw background
		box.draw(g, x, y, width, height);
		
		// draw title
		g.setColor(Game.getTextColor());
		g.setFont(Game.getMedievalSharp().deriveFont(32f));
		g.drawString(title, x + (width - g.getFontMetrics().stringWidth(title)) / 2, y + 65);
		
		// draw description
		g.setFont(Game.getMedievalSharp().deriveFont(22f));
		drawString(g, description, x + 10, y + 95);
	} // draw
	
	// position the tooltip relative to the values passed in
	public void position(int xPos, int yPos, int w, int h) {
		
		// determine section of screen the tooltip will be drawn at
		boolean below = yPos > Camera.getHeight() / 2; 
		boolean left = xPos < Camera.getWidth() / 2;
		
		int xToMatch = 0;
		int yToMatch = 0;
		
		// position the tooltip
		if (below) {
			if (left) {
				// at bottom left of screen
				// set bottom left corner of tooltip to top right of xPos/yPos
				xToMatch = xPos + w;
				yToMatch = yPos;
				
				x = xToMatch;
				y = yToMatch - this.height;
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
			
		} else {
			
			// at top right of screen
			// set top right corner of tooltip to bottom left of xPos/yPos
			
			xToMatch = xPos - this.width;
			yToMatch = yPos + h;
			
			x = xToMatch;
			y = yToMatch;
			
		} // else
		
	} // position
	
	// sets the title and description
	public void setText(String title, String description) {
		this.title = title;
		this.description = description;
	} // setText
	
	// draw multi-line Strings, author: John Evans
    private void drawString(Graphics g, String text, int x, int y) {

        // draws each line on a new line
        for (String line : text.split("\n")) {
            g.drawString(line, x + (width - g.getFontMetrics().stringWidth(line)) / 2, y += g.getFontMetrics().getHeight());
        } // for
    } // drawString
	
} // Tooltip
