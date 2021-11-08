import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class QuestLog {
	
	private HashMap<String, Quest> questStore = new HashMap<String, Quest>();	
	private ArrayList<String> currentQuests = new ArrayList<String>();
	
	private Inventory inv;
	private DialogueManager dialogue;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private Sprite questBox;
	
	public QuestLog(Inventory i) {
		inv = i;
		dialogue = null;
		
		// set positioning
		width = (int) (Camera.getWidth() * 0.24);
		height = (int) (Camera.getHeight() * 0.108);
		x = (int) (Camera.getWidth() * 0.025);
		y = (int) (Camera.getHeight() * 0.92) - height;
		
		questBox = SpriteStore.get().getSprite("ui/quest.png");
	} // QuestLog
	
	public void setDialogue(DialogueManager d) { 
		dialogue = d;
	} // setDialogue
	
	// adds a quest to the questStore
	public void add(String id, Quest quest) {
		questStore.put(id, quest);
	} // add
	
	// returns questStore
	public Quest get(String id) {
		return questStore.get(id);
	} // get

	// unlocks the quest passed in
	public void unlock(String questID) {
		questStore.get(questID).unlock();
		currentQuests.add(questID);
	} // unlock
	
	// completes the quest if possible
	public void complete(String questID) {
		if (!canComplete(questID)) { return; }
		
		// mark quest as complete
		questStore.get(questID).complete();
		
		// take objectives from and store rewards in inventory
		removeQuestItemsFromInventory(questID);
		storeRewardInInventory(questID);
		
		currentQuests.remove(questID);
	} // complete
	
	// returns whether the objectives of the quest have been met
	public boolean canComplete(String questID) {
		HashMap<String, Integer> objectives = questStore.get(questID).getObjectives();
		Set<String> keySet = objectives.keySet();
		
		// check each objective
		for (String key : keySet) {

			// if quest objectives are items, return whether items are not in inventory
			if (questStore.get(questID).getHasItemObjective() && !inv.contains(key, objectives.get(key))) {
				System.out.println("canComplete: " + questID + ", false, : " + objectives.get(key) + " " + key);
				return false;
			} // if
			
			// if quest objective is number of kills, return false if objective not reached
			if (!questStore.get(questID).getHasItemObjective() && Character.getKills(key) < objectives.get(key)) {
				System.out.println("canComplete: " + questID + ", false, : " + objectives.get(key) + " " + key);
				return false;
			} // if
		} // if
		
		return true;
	} // canComplete
	
	// remove quest objective items from inventory
	private void removeQuestItemsFromInventory(String questID) {
		HashMap<String, Integer> objectives = questStore.get(questID).getObjectives();
		Set<String> objSet = objectives.keySet();
		
		// remove each item in objectives from inventory
		for (String obj : objSet) {
			inv.removeItem(obj, objectives.get(obj));
		} // if
	} // removeQuestItemsFromInventory
	
	// add rewards to inventory
	// if inventory is full, set quest status to pending
	private void storeRewardInInventory(String questID) {
		
		// check if quest is already completed or has no rewards
		if (!questStore.get(questID).getIsCompleted()) { return; }
		if (questStore.get(questID).getRewards() == null) { return; }
		
		// get quest rewards
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
		
		// check if all rewards were collected
		if (rewardsCollected < rewardSet.size()) {
			
			// notify dialogue system that some rewards are uncollected
			dialogue.rewardsUncollected();
		} // if
		
	} // storeRewardInInventory
	
	// draw quest log to screen
	public void draw(Graphics g) {
		
		// draw text (current quest)
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
