import java.awt.Color;
import java.awt.Graphics;

// stores InventoryItems
public class InventorySlot extends Entity {

	private InventoryItem item;			// item in slot
	private int index;					// slot index
	private Sprite sprite;				// sprite of the item
	
	private int width;	// temp? slot side length
	private int height;
	
	// constructor
	public InventorySlot(int x, int y, int i) {
		index = i;
		item = new InventoryItem("sprite" + index, "images/sprite" + index + ".png", "sprite" + index, "this is a description"); // temp
		
		// set screen position of slot
		this.x = (i) * (x - 200) + 200 + 10; // 200 = inv x & 10 is important regardless of positioning
		this.y = y + 10; // change
		updateSprite();
		width = 80; // temp
		height = 80;
	} // InventorySlot
	
	// return slot width/length
	public int getWidth() {
		return width;
	} // getWidth
	
	public int getHeight(){
		return height;
	}
	
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
		g.fillRect(xPos, yPos, width, height); // set dimensions looks nicer
		
		// display item if there is one
		if (sprite != null) {
			item.getSprite().draw(g, xPos + ((80 - item.getSprite().getWidth()) / 2), yPos + (80 - item.getSprite().getHeight()) / 2); // needs to be changed so sprite is centered
		} // if
		
		if (Inventory.getHoveringSlotIndex() == index) {
			item.getTooltip().position((int) x, (int) y, width, height);
			item.getTooltip().draw(g);
		} // if
		
	} // draw
	
	// returns slot index
	public int getIndex() {
		return index;
	} // getIndex
} // InventorySlot
