import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class QuestLog { // rename to QuestManager?
	
	private HashMap<String, Quest> questStore = new HashMap<String, Quest>();	
	private ArrayList<String> currentQuests = new ArrayList<String>();
	
	private Inventory inv;
	private DialogueManager dialogue; // probably shouldn't have this here... but idk how to do it otherwise
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private Sprite questBox;
	
	public QuestLog(Inventory i) {
		inv = i;
		dialogue = null;
		
		// can change
		width = (int) (Camera.getWidth() * 0.24);
		height = (int) (Camera.getHeight() * 0.108);
		x = (int) (Camera.getWidth() * 0.025);
		y = (int) (Camera.getHeight() * 0.92) - height;
		
		questBox = SpriteStore.get().getSprite("ui/quest.png");
	} // QuestLog
	
	public void setDialogue(DialogueManager d) { // must be called in Game after initialising, else nullpointerexceptions...
		dialogue = d;
	}
	
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
	} // removeQuestItemsFromInventory
	
	private void storeRewardInInventory(String questID) {
		
		// check if quest is already completed or has no rewards
		if (!questStore.get(questID).getIsCompleted()) { return; }
		if (questStore.get(questID).getRewards() == null) { return; }
		
		HashMap<String, Integer> rewards = questStore.get(questID).getRewards();
		Set<String> rewardSet = questStore.get(questID).getRewards().keySet();
		
		int rewardsCollected = 0;
		
		// adds rewards to inventory
		for (String reward : rewardSet) {
			
			// add item to inventory and increment collected counter
			if (inv.addItem(reward, rewards.get(reward))) {
				rewardsCollected++;
				questStore.get(questID).removeReward(reward);
			} // if
			
		} // if
		
		if (rewardsCollected < rewardSet.size()) {
			System.out.println("inv full, not finished collecting rewards");
			// notify dialogue system that rewards are uncollected
			dialogue.rewardsUncollected();
		} // if
		
	} // storeRewardInInventory
	
	public void draw(Graphics g) {
		
		// draw text (current quests)
		for (int i = 0; i < currentQuests.size(); i++) {
			String displayText = questStore.get(currentQuests.get(i)).getName();
			
			// draw background
			questBox.draw(g, x, y, width, height);
			
			// draw current quest text
			g.setColor(Game.getTextColor());
			g.setFont(Game.getMedievalSharp().deriveFont(32f));
			g.drawString(displayText, x + (width - g.getFontMetrics().stringWidth(displayText)) / 2, y + 30 + (height - g.getFontMetrics().getHeight()) / 2);
			
		} // for
		
	} // draw

	
} // QuestLog
