import java.awt.Graphics;
import java.awt.event.MouseEvent;

// stores the item currently being dragged
public class DragItem extends Entity{

	private InventorySlot previousSlot;
	private InventoryItem item;
	
	// constructor
	public DragItem() {	
		previousSlot = null;
		item = null;
	} // DragItem
	
	// place item back to previous slot
	public void backToPrevious() {
		previousSlot.addItem(item);
		stopDrag();
	} // backToPrevious

	// set up DragItem to drag the new item
	public void startDrag(InventorySlot slot) {
		previousSlot = slot;
		x = previousSlot.x; // is this needed?
		y = previousSlot.y;
		
		item = slot.getItem();
		
		slot.removeItem();
	} // startDrag
	
	// reset everything and stop dragging
	public void stopDrag() {
		previousSlot = null;
		item = null;
	} // stopDrag
	
	// swap InventoryItems with the slot passed in
	public void swap(InventorySlot otherSlot) {
		
		// get itemID to later look it up in ItemLookUp
		String id = item.getID();
		
		// swap items
		previousSlot.removeItem();
		previousSlot.addItem(otherSlot.getItem());
		
		otherSlot.removeItem();
		otherSlot.addItem(InventoryItem.getItem(id));
		
		// cancel dragging
		stopDrag();
	} // swap
	
	// return the item being dragged
	public InventoryItem getItem() {
		return item;
	} // getItem
	
	// return the slot the item came from
	public InventorySlot getSlot() {
		return previousSlot;
	} // getSlot
	
	// follow mouse position
	public void move(MouseEvent e) {
		x = e.getX(); // need to change (so not always dragging left corner of sprite)
		y = e.getY(); 
	} // move
	
	// draw sprite if there is one
	@Override
	public void draw(Graphics g) {
		if (item.getSprite() == null) {
			return;
		} // if
		
		item.getSprite().draw(g, (int) x, (int) y);
	} // draw
} // DragItem
