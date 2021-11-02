import java.awt.Graphics;
import java.awt.event.MouseEvent;

// stores the item currently being dragged
public class DragItem extends Entity{

	private InventorySlot previousSlot;
	private String itemID;
	private int num;
	
	// constructor
	public DragItem() {	
		previousSlot = null;
		itemID = null;
		num = 0;
	} // DragItem
	
	// place item back to previous slot
	public void backToPrevious() {
		previousSlot.addItem(itemID, num);
		stopDrag();
	} // backToPrevious

	// set up DragItem to drag the new item
	public void startDrag(InventorySlot slot) {
		previousSlot = slot;
		x = previousSlot.x; // is this needed?
		y = previousSlot.y;
		
		itemID = slot.getItem();
		num = slot.getNumber();
		
		slot.removeItem(num);
	} // startDrag
	
	// reset everything and stop dragging
	public void stopDrag() {
		previousSlot = null;
		itemID = null;
		num = 0;
	} // stopDrag
	
	// swap InventoryItems with the slot passed in
	public void swap(InventorySlot otherSlot) {
	
		// if items are the same, add from previous to otherSlot
		if (itemID.equals(otherSlot.getItem())) {
			otherSlot.addItem(itemID, num);
			previousSlot.removeItem(num);
		} else {
			
			// swap items
			previousSlot.removeItem(num);
			previousSlot.addItem(otherSlot.getItem(), otherSlot.getNumber());
			
			otherSlot.removeItem(otherSlot.getNumber());
			otherSlot.addItem(itemID, num);
		} // else
		
		// cancel dragging
		stopDrag();
	} // swap
	
	// return the item being dragged
	public String getItem() { // is this used?
		return itemID;
	} // getItem
	
	public int getNumber() {
		return num;
	}
	
	// return the slot the item came from
	public InventorySlot getSlot() {
		return previousSlot;
	} // getSlot
	
	// follow mouse position
	public void move(MouseEvent e) {
		x = e.getX() - (previousSlot.getWidth() / 2); // need to change (so not always dragging left corner of sprite)
		y = e.getY() - (previousSlot.getHeight() / 2); 
	} // move
	
	// draw sprite if there is one
	@Override
	public void draw(Graphics g) {
		if (num <= 0) {
			return;
		} // if
		
		InventoryItem.getItem(itemID).getSprite().draw(g, (int) x, (int) y);
	} // draw
} // DragItem
