import java.awt.Color;
import java.awt.Graphics;

// stores InventoryItems
public class InventorySlot extends Entity {

	private InventoryItem item;			// item in slot
	private int index;					// slot index
	private Sprite sprite;				// sprite of the item
	
	private int width;	// temp? slot side length
	
	// constructor
	public InventorySlot(int x, int y, int i) {
		index = i;
		item = new InventoryItem("images/sprite" + index + ".png", "sprite" + index); // temp
		
		// set screen position of slot
		this.x = (i + 1) * x + 10; // can change
		this.y = y + 10; // change
		
		width = 80; // temp
		
		updateSprite();
	} // InventorySlot
	
	// return slot width/length
	public int getWidth() {
		return width;
	} // getWidth
	
	// add item passed in into slot
	public void addItem(InventoryItem item) {
		
		// check if this slot is full
		if (this.item != null) { return; }
		
		this.item = item;
		updateSprite();
	} // addItem
	
	// remove item from slot
	public void removeItem() {
		item = null;
		updateSprite();
	} // removeItem
	
	// returns item currently in slot
	public InventoryItem getItem() {
		return item;
	} // getItem
	
	// update slot sprite to be same as current item
	public void updateSprite() {
		if (item != null) {
			sprite = item.getSprite();
			return;
		} // if
		sprite = null;
	} // updateSprite
	
	// draw slot background and item if there is one
	public void draw(Graphics g) {
		int xPos = (int) x; // temp, can change
		int yPos = (int) y;  // temp, can change
		
		// draw slot background
		g.setColor(Color.cyan);
		g.fillRect(xPos, yPos, 80, 80);
		
		// display item if there is one
		if (sprite != null) {
			item.getSprite().draw(g, xPos, yPos); // needs to be changed so sprite is centered
		} // if
	} // draw
	
	// returns slot index
	public int getIndex() {
		return index;
	} // getIndex
}
