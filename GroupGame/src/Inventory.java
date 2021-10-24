import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

// stores all the player's current items
public class Inventory extends Entity {

	private static final int X_PADDING = 100; // temp for placing inv on screen
	private static final int Y_PADDING = 100; 

	private DragItem dragItem; 						// stores item being dragged
	private static boolean isDragging = false; 		// whether player is dragging an item
	
	private int size;								// size of inventory
	private InventorySlot[] slots;					// slots in the inventory
	
	// constructor
	public Inventory(int n) {
		size = n;
		slots = new InventorySlot[size];
		dragItem = new DragItem();
		
		// create new inventory slots
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new InventorySlot(X_PADDING, 500 - Y_PADDING, i); // use vars
		} // for
	} // Inventory
	
	// add item to inventory (not implemented yet..)
	public void addItem(InventoryItem item) {
		
	}
	
	// draw the inventory slots
	public void drawSlots(Graphics g) {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) {
				continue;
			} // if
			slots[i].draw(g);
		} // for
	} // drawSlots
	
	// draw Inventory
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(100, 500 - Y_PADDING, 300, 100); // use vars

		drawSlots(g);
		
		if (dragItem != null && dragItem.getItem() != null) {
			dragItem.draw(g);
		} // if
	} // draw

	
	public static int getXPadding() {
		return X_PADDING;
	} // getXPadding

	public static int getYPadding() {
		return Y_PADDING;
	} // getYPadding
	
	// handle mouse drag
	public void handleDrag(MouseEvent e) {
		
		// if is already dragging, make sprite follow mouse
		if (isDragging) {
			dragItem.move(e);
			return; 
		} // if
		
		// get which slot the player is dragging from
		int slotIndex = checkIfMouseOverSlot(e);
		
		// if not dragging from inv slot, return
		if (slotIndex < 0) { return; }
		
		// check if slot contains item
		if (slots[slotIndex].getItem() == null) { return; }
		
		// create dragItem to store drag info
		dragItem.startDrag(slots[slotIndex]);
		isDragging = true;
	}

	// returns the index of the slot the mouse event occurred at
	private int checkIfMouseOverSlot(MouseEvent e) { // rename!!
		
		// go through slots and check if mouse event occurred inside bounds of slot
		for (int i = 0; i < slots.length; i++) {
			if (e.getX() > slots[i].x && e.getX() < slots[i].x + slots[i].getWidth()
					&& e.getY() > slots[i].y && e.getY() < slots[i].y + slots[i].getWidth()) {
				return i;
			} // if
		} // for
		
		return -1;
	} // checkIfMouseOverSlot

	// handle when the player stops dragging
	public void stopDrag(MouseEvent e) {
		
		// check if the player is dragging
		if (!isDragging) { return; }
		isDragging = false;
		
		// find the slot that the player stopped dragging at
		int slotIndex = checkIfMouseOverSlot(e);
		
		// if not dropping on a slot or is dropping on original slot, 
		// put back to original slot
		if (slotIndex < 0 || slotIndex == dragItem.getSlot().getIndex()) {
			dragItem.backToPrevious();
			return;
		} // if
		
		// swap items with other slot
		dragItem.swap(slots[slotIndex]);
	} // stopDrag
	
}
