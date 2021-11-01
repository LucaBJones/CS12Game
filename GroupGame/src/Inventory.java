import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

// stores all the player's current items
public class Inventory extends Entity {

	private static final int X_PADDING = 100; 		// temp for placing inv on screen
	private static final int Y_PADDING = 100; 
	
	private DragItem dragItem; 						// stores item being dragged
	private boolean isDragging = false; 			// whether player is dragging an item
	private static int hoveringSlotIndex = -1; 		// index of the slot the player is hovering over
														// -1 if not hovering over a slot
														// rename?
	
	private int size;								// size of inventory
	private InventorySlot[] slots;					// slots in the inventory
	
	// constructor
	public Inventory(int n) {
		size = n;
		slots = new InventorySlot[size];
		dragItem = new DragItem();
		
		// create new inventory slots
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new InventorySlot(200 + X_PADDING, 120 - Y_PADDING, i); // use vars
		} // for
	} // Inventory
	
	// add item to inventory (not implemented yet..) and returns whether items were successfully added
	public boolean addItem(String id, int n) { // multi-item not implemented
		int emptySlot = getEmptySlotIndex();
		
		// check if there are any empty slots
		if (emptySlot < 0) { return false; }
		
		slots[emptySlot].addItem(InventoryItem.getItem(id));
		
		return true;
	} // addItem
	
	public void removeItem(String id, int n) { // multi-item remove not implemented yet
		if (!contains(id, n)) { return; }
		for (InventorySlot slot : slots) {
			if (slot.getItem() == null) { continue; }
			if (slot.getItem().getID().equals(id)) {
				slot.removeItem();
			} // if
		} // for
	}
	
	// returns whether the inventory has the item with the id passed in
	public boolean contains(String id, int n) { // check for multiple of item not implemented yet
		for (InventorySlot slot : slots) {
			if (slot.getItem() == null) { continue; }
			if (slot.getItem().getID().equals(id)) {
				return true;
			} // if
		} // for
		return false;
	} // contains
	
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
		g.fillRect(200, 120 - Y_PADDING, 300, 100); // use vars

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
		
		// stop hovering if start dragging
		hoveringSlotIndex = -1;
		
		// if is already dragging, make sprite follow mouse
		if (isDragging) {
			dragItem.move(e);
			return; 
		} // if
		
		// get which slot the player is dragging from
		int slotIndex = checkIfMouseOverSlot(e); // should this be declared at top of method?
		
		// if not dragging from inv slot, return
		if (slotIndex < 0) { return; }
		
		// check if slot contains item
		if (slots[slotIndex].getItem() == null) { return; }
		
		// create dragItem to store drag info
		dragItem.startDrag(slots[slotIndex]);
		isDragging = true;
	} // handleDrag

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
	
	public void handleHover(MouseEvent e) {
		int slotIndex = checkIfMouseOverSlot(e);
		
		// check if mouse is over a slot
		if (slotIndex < 0) {
			if (hoveringSlotIndex >= 0) {
				hoveringSlotIndex = -1;
			} // if
			return; 
		} // if
		hoveringSlotIndex = slotIndex;
	} // handleHover
	
	public static int getHoveringSlotIndex() {
		return hoveringSlotIndex;
	} // getHoveringSlotIndex
	
	private int getEmptySlotIndex() {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getItem() == null) {
				return i;
			} // if
		} // for
		
		return -1;
	} // int
	
} // Inventory
