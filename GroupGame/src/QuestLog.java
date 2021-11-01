import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class QuestLog { // rename to QuestManager?
	
	private HashMap<String, Quest> questStore = new HashMap<String, Quest>();	
	private ArrayList<String> currentQuests = new ArrayList<String>();
	
	private Inventory inv;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	public QuestLog(Inventory i) {
		inv = i;
		
		// can change
		width = 100;
		height = 50;
		x = Camera.getWidth() - width - 50;
		y = 20;
	} // QuestLog
	
	// adds a quest to the questStore
	public void add(String id, Quest quest) {
		questStore.put(id, quest);
	} // add
	
	public Quest get(String id) {
		return questStore.get(id);
	}
	
	public void unlock(String questID) {
		questStore.get(questID).unlock();
		currentQuests.add(questID);
		System.out.println("added to current Quests");
	}
	
	// completes the quest if possible
	public void complete(String questID) {
		if (!canComplete(questID)) { return; }
		questStore.get(questID).complete();
		removeQuestItemsFromInventory(questID);
		storeRewardInInventory(questID);
		currentQuests.remove(questID);
		
		System.out.println("completed quest!: " + questStore.get(questID).getIsCompleted());
	} // complete
	
	// returns whether the objectives of the quest have been met
	public boolean canComplete(String questID) {
		HashMap<String, Integer> objectives = questStore.get(questID).getObjectives();
		Set<String> keySet = objectives.keySet();
		
		for (String key : keySet) {
			if (!inv.contains(key, objectives.get(key))) {
				return false;
			} // if
		} // if
		
		return true;
	} // canComplete
	
	private void removeQuestItemsFromInventory(String questID) {
		HashMap<String, Integer> objectives = questStore.get(questID).getObjectives();
		Set<String> objSet = objectives.keySet();
		
		for (String obj : objSet) {
			inv.removeItem(obj, objectives.get(obj));
		} // if
	}
	
	private void storeRewardInInventory(String questID) { // what if inventory is full??
		if (!questStore.get(questID).getIsCompleted()) { return; }
		
		HashMap<String, Integer> rewards = questStore.get(questID).getRewards();
		Set<String> rewardSet = questStore.get(questID).getRewards().keySet();
		
		// adds rewards to inventory
		for (String reward : rewardSet) {
			inv.addItem(reward, rewards.get(reward));
		} // if
		
		System.out.println("storing reward");
	}
	
	public void draw(Graphics g) {
		
		// draw title background
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		
		// draw title
		g.setColor(Color.BLACK);
		g.drawString("Quests", x + 20, y + 30); // can change
		
		// draw text (current quests)
		for (int i = 0; i < currentQuests.size(); i++) {
			
			// draw background
			g.setColor(Color.ORANGE);
			g.fillRect(x - 15, y + height * (i + 1), width + 30, height);
			g.setColor(Color.BLACK);
			g.drawRect(x - 15, y + height * (i + 1), width + 30, height);
			
			// draw current quest text
			g.setColor(Color.BLACK);
			g.drawString(questStore.get(currentQuests.get(i)).getName(), x + 20, y + (height + 30) * (i + 1));
		} // for
		
	} // draw
	
} // QuestLog
