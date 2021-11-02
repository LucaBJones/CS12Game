import java.awt.Color;
import java.awt.Graphics;

// stores InventoryItems
public class InventorySlot extends Entity {

	private String itemID;			// item in slot
	private int numberOfItems;
	
	private int index;					// slot index
	private Sprite sprite;				// sprite of the item
	
	private int width;	// temp? slot side length
	private int height;
	
	// constructor
	public InventorySlot(int x, int y, int i) {
		index = i;
		itemID = "";
		numberOfItems = 0;
		
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
	public void addItem(String itemID, int n) {
		
		// if the slot is empty, add item
		if (numberOfItems == 0) {
			this.itemID = itemID;
			numberOfItems = n;
		} // if
		
		// if the item is same as current, add n to numberOfItems
		else if (this.itemID.equals(itemID)) {
			numberOfItems += n;
		} // else if
		
		updateSprite();
	} // addItem
	
	// remove item from slot
	public void removeItem(int n) {
		numberOfItems -= n;
		
		if (numberOfItems <= 0) {
			numberOfItems = 0;
			itemID = "";
		}
		
		updateSprite();
	} // removeItem
	
	// returns item currently in slot
	public String getItem() {
		return itemID;
	} // getItem
	
	// update slot sprite to be same as current item
	public void updateSprite() {
		if (numberOfItems > 0) {
			sprite = InventoryItem.getItem(itemID).getSprite();
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
			InventoryItem item = InventoryItem.getItem(itemID);
			item.getSprite().draw(g, xPos + ((80 - item.getSprite().getWidth()) / 2), yPos + (80 - item.getSprite().getHeight()) / 2); // needs to be changed so sprite is centered
		
			// display number of items
			if (numberOfItems > 0) {
				g.setColor(Color.BLACK);
				g.drawString(numberOfItems + "", xPos + width - 20, yPos + height - 15);
			} // if
			
			// draw tooltip if hovering over
			if (Inventory.getHoveringSlotIndex() == index) {
				Game.getTooltip().setText(item.getName(), item.getDescription());
				Game.getTooltip().position((int) x, (int) y, width, height);
				Game.getTooltip().draw(g);
			} // if
		} // if
		
	} // draw
	
	public int getNumber() {
		return numberOfItems;
	}
	
	// returns slot index
	public int getIndex() {
		return index;
	} // getIndex
} // InventorySlot
