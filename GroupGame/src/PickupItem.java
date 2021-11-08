public class PickupItem extends Entity {

	private String itemID;
	private int num;
	
	public PickupItem(String ref, String item, int numOfItem, int xPos, int yPos) {
		super(ref, xPos, yPos);
		itemID = item;
		num = numOfItem;
	} // PickupItem
	
	// returns the item id
	public String getItemID() {
		return itemID;
	} // getItemID
	
	// returns the number of items in this pickup
	public int getNum() {
		return num;
	} // getNum
	
} // PickupItem
