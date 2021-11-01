import java.util.HashMap;

// Items that can be stored in inventory
public class InventoryItem {

	// stores all items in game (like spriteStore)
	private static HashMap<String, InventoryItem> itemLookUp = new HashMap<String, InventoryItem>();
	
	// stores sprite and id of item (too obvious?)
	private Sprite inventoryIcon;
	
	private String itemID;
	private String name;
	private String description;
	
	// constructor
	public InventoryItem(String id, String r, String n, String d) {
		itemID = id;
		
		// if item is already in itemLookUp, use it
		if (itemLookUp.containsKey(id)) {
			inventoryIcon = getItem(id).getSprite();
			return;
		} // if
		
		// set sprite of item, name, and description of item
		inventoryIcon = (SpriteStore.get()).getSprite(r);
		name = n;
		description = d;
		
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
		return inventoryIcon;
	} // getSprite
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
} // InventoryItem