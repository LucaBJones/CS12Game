/********************************************************************
* Name: Sprite.java
* Author: Mrs. Wear
* Date: March 23, 2006
* Purpose:  Store no state information, this allows the image to be 
* stored only once, but to be used in many different places. 
* For example, one copy of alien.gif can be used over and over.
*********************************************************************/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite {

	public Image image; // the image to be drawn for this sprite

	// constructor
	public Sprite(Image i) {
		image = i;
	} // constructor

	// return width of image in pixels
	public int getWidth() {
		return image.getWidth(null);
	} // getWidth

	// return height of image in pixels
	public int getHeight() {
		return image.getHeight(null);
	} // getHeight

	// draw the sprite in the graphics object provided at location (x,y)
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	} // draw
	
	// draw the sprite in the graphics object provided at location (x,y) with a size of (width x height)
	public void draw(Graphics g, int x, int y, int width, int height) {
		g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(null), image.getHeight(null), null);
	} // draw

} // Sprite
