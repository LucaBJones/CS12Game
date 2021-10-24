import java.util.HashMap;

// Items that can be stored in inventory
public class InventoryItem {

	// stores all items in game (like spriteStore)
	private static HashMap<String, InventoryItem> itemLookUp = new HashMap<String, InventoryItem>();
	
	// stores sprite and id of item
	private Sprite sprite;
	private String itemID;
	
	// constructor
	public InventoryItem(String r, String id) {
		itemID = id;
		
		// if item is already in itemLookUp, use it
		if (itemLookUp.containsKey(id)) {
			sprite = getItem(id).getSprite();
			return;
		} // if
		
		// set sprite of item
		sprite = (SpriteStore.get()).getSprite(r);
		
		// add item to itemLookUp
		itemLookUp.put(id, this);
	} // InventoryItem
	
	// returns id of item
	public String getID() {
		return itemID;
	} // getID
	
	// returns the item with the id passed in
	public static InventoryItem getItem(String id) {
		return itemLookUp.get(id);
	} // InventoryItem

	// returns the sprite of this item
	public Sprite getSprite() {
		return sprite;
	} // getSprite
	
} // InventoryItem