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
	
	private String use;			// whether the item affects hp, mana, or stamina
	private int useAmount;		// amount that hp/mana/stamina is decremented/incremented by
	
	// constructor
	public InventoryItem(String id, String r, String n, String d, String use, int useAmount) {
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
		
		this.use = use;
		this.useAmount = useAmount;
		
		// add item to itemLookUp
		itemLookUp.put(id, this);
	} // InventoryItem
	
	public void use(Character player) {
		if (use.isEmpty()) { return; }
		
		if (use.contains("hp")) {
			if (use.contains("increment")) {
				player.getHp().increment(useAmount);
			} else if (use.contains("decrement")) {
				player.getHp().decrement(useAmount);
			}
		} else if (use.contains("mana")) {
			if (use.contains("increment")) {
				player.getMana().increment(useAmount);
			} else if (use.contains("decrement")) {
				player.getMana().decrement(useAmount);
			}
		} else if (use.contains("stamina")) {
			if (use.contains("increment")) {
				player.getStamina().increment(useAmount);
			} else if (use.contains("decrement")) {
				player.getStamina().decrement(useAmount);
			} // else if
		} // else if
	} // use
	
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