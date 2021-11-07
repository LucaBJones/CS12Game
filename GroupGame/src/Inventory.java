import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

// stores all the player's current items
public class Inventory extends Entity {

	private static int padding = (int) (Camera.getHeight() * 0.029); 		// temp for placing inv on screen
	
	private DragItem dragItem; 						// stores item being dragged
	private boolean isDragging = false; 			// whether player is dragging an item
	private static int hoveringSlotIndex = -1; 		// index of the slot the player is hovering over
														// -1 if not hovering over a slot
														// rename?
	
	private int size;								// size of inventory
	private InventorySlot[] slots;					// slots in the inventory
	
	private static int invWidth;
	private static int invHeight;
	
	private Sprite sprite;
	
	// constructor
	public Inventory(int n) {
		size = n;
		slots = new InventorySlot[size];
		dragItem = new DragItem();
		
		invWidth = (int) (Camera.getWidth() * 0.125);
		invHeight = (int) (Camera.getHeight() * 0.64);
		
		x = Camera.getWidth() * 0.8;
		y = (Camera.getHeight() - invHeight) / 2;
		
		sprite = SpriteStore.get().getSprite("ui/inventory.png");
		
		// create new inventory slots
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new InventorySlot((int) x, (int) y, i); // use vars
		} // for
	} // Inventory
	
	// add items to inventory and returns whether items were successfully added
	public boolean addItem(String id, int n) {
		int availableSlots = getAvailableSlot(id);
		
		// check if there are any empty slots
		if (availableSlots < 0) { return false; }
		
		slots[availableSlots].addItem(id, n);
		
		return true;
	} // addItem
	
	// removes items from inventory
	public void removeItem(String id, int n) {
		if (!contains(id, n)) { return; }
		int count = n;
		
		for (InventorySlot slot : slots) {
			if (slot.getNumber() <= 0 || slot.getItem().isEmpty()) { continue; }
			if (slot.getItem().equals(id)) {
				count -= slot.getNumber();
				System.out.println("slot num: " + slot.getNumber());
				
				// remove all from slot
				if (count >= 0) {
					slot.removeItem(slot.getNumber());
					System.out.println("removed all, count: " + count + ", n: " + n);
					if (count == 0) { return; } // return if finished removing
					continue;
				} // if
				
				// remove some from slot if removed enough already
				if (count < 0) {
					slot.removeItem(slot.getNumber() + count); // count is negative
					System.out.println("removed " + (0 - count) + ", n: " + n + ", count: " + count);
					return;
				} // if
				
			} // if
		} // for
	} // removeItem
	
	// returns whether the inventory has the item with the id passed in
	public boolean contains(String id, int n) { // check for multiple of item not implemented yet
		int count = 0;
		
		for (InventorySlot slot : slots) {
			if (slot.getNumber() <= 0) { continue; }
			if (slot.getItem().equals(id)) {
				count += slot.getNumber();
			} // if
		} // for
		
		return count >= n;
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
		sprite.draw(g, (int) x, (int) y, invWidth, invHeight);
		
		drawSlots(g);
		
		if (dragItem != null && dragItem.getNumber() > 0) {
			dragItem.draw(g);
		} // if
	} // draw
	
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
		if (slots[slotIndex].getNumber() <= 0) { return; }
		
		// create dragItem to store drag info
		dragItem.startDrag(slots[slotIndex]);
		isDragging = true;
	} // handleDrag

	// returns the index of the slot the mouse event occurred at
	private int checkIfMouseOverSlot(MouseEvent e) { // rename!!
		
		// go through slots and check if mouse event occurred inside bounds of slot
		for (int i = 0; i < slots.length; i++) {
			if (e.getX() > slots[i].x && e.getX() < slots[i].x + slots[i].getLength()
					&& e.getY() > slots[i].y && e.getY() < slots[i].y + slots[i].getLength()) {
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
	
	public void stopDrag() {
		if (!isDragging) { return; }
		isDragging = false;
		
		if (dragItem.getItem() != null) {
			dragItem.backToPrevious();
		}
	}
	
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
	
	private int getAvailableSlot(String itemID) {
		boolean checkForItem = itemID != null && !itemID.isEmpty();
		
		for (int i = 0; i < slots.length; i++) {
			if (slots[i].getNumber() <= 0 || (checkForItem && slots[i].getItem().equals(itemID))) {
				return i;
			} // if
		} // for
		
		return -1;
	} // int

	
	public static int getWidth() {
		return invWidth;
	}
	
	public static int getPadding() {
		return padding;
	}
} // Inventory
