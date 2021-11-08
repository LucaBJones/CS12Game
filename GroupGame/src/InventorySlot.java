import java.awt.Graphics;

// stores InventoryItems
public class InventorySlot extends Entity {

	private String itemID; // item in slot
	private int numberOfItems;

	private int index; // slot index
	private Sprite sprite; // sprite of the item
	private Sprite frameSprite;

	private int length;

	public InventorySlot(int x, int y, int i) {
		index = i;
		itemID = "";
		numberOfItems = 0;

		// set slot size
		length = (int) (Camera.getWidth() * 0.07);

		// set screen position of slot
		this.x = x + (Inventory.getWidth() - length) / 2;
		this.y = y + (length * i) + Inventory.getPadding() * (i + 1);

		frameSprite = SpriteStore.get().getSprite("ui/invSlot.png");
		
		updateSprite();
	} // InventorySlot

	// return slot width/length
	public int getLength() {
		return length;
	} // getWidth

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
		} // if

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
		int yPos = (int) y; // temp, can change

		// draw slot frame
		frameSprite.draw(g, xPos, yPos, length, length);

		// display item if there is one
		if (sprite != null) {
			InventoryItem item = InventoryItem.getItem(itemID);
			item.getSprite().draw(g, xPos + ((length - item.getSprite().getWidth()) / 2),
					yPos + (length - item.getSprite().getHeight()) / 2);

			// display number of items
			if (numberOfItems > 0) {
				g.setColor(Game.getTextColor());
				g.setFont(Game.getMedievalSharp().deriveFont(20f));
				g.drawString(numberOfItems + "", xPos + length - 20, yPos + length - 15);
			} // if

			// draw tooltip if hovering over
			if (Inventory.getHoveringSlotIndex() == index) {
				Game.getTooltip().setText(item.getName(), item.getDescription());
				Game.getTooltip().position((int) x, (int) y, length, length);
				Game.getTooltip().draw(g);
			} // if
		} // if

	} // draw

	public int getNumber() {
		return numberOfItems;
	} // getNumber

	public int getIndex() {
		return index;
	} // getIndex
	
} // InventorySlot
