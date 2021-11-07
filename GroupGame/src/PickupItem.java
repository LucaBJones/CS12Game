import java.awt.Graphics;
import java.awt.Rectangle;

public class PickupItem extends Entity {

	private String itemID;
	private int num;
	
	public PickupItem(String ref, String item, int numOfItem, int xPos, int yPos) {
		super(ref, xPos, yPos);
		itemID = item;
		num = numOfItem;
	} // PickupItem
	
	public String getItemID() {
		return itemID;
	} // getItemID
	
	public int getNum() {
		return num;
	}

	
} // PickupItem
