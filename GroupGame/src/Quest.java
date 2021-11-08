import java.util.HashMap;

public class Quest {

	private String name;
	
	private boolean hasItemObjective;				// whether the objectives are items

	private HashMap<String, Integer> objectives;	// stores number of kills or number of items needed to complete quest
	private HashMap<String, Integer> rewards;		// stores item id and number that will be given to player when quest is complete
	
	private int status; // -1 = locked, 0 = unlocked, 1 = complete, 2 = pending rewards
	
	public Quest(String id, String name, HashMap<String, Integer> objectives, boolean itemObjective, HashMap<String, Integer> rewards, QuestLog log) {
		this.name = name;
		
		this.objectives = objectives;
		this.hasItemObjective = itemObjective;
		
		this.rewards = rewards;
		
		status = -1;
		
		log.add(id, this);
	} // Quest
	
	public HashMap<String, Integer> getObjectives() {
		return objectives;
	} // getObjectives
	
	public HashMap<String, Integer> getRewards() {
		return rewards;
	} // getObjectives
	
	// set the status of this quest to "complete"
	public void complete() {
		if (status == 0) {
			status = 1;
		} // if
	} // complete
	
	// set the status of this quest to "unlocked"
	public void unlock() {
		if (status == -1) {
			status = 0;
		} // if
	} // unlock
	
	// returns the quest name
	public String getName() {
		return name;
	} // getIsCompleted
	
	// returns the quest status
	public int getStatus() {
		return status;
	} // getStatus
	
	// sets the status of this quest
	public void setStatus(int n) {
		status = n;
	} // setStatus
	
	// removes a reward from this quest's rewards
	public void removeReward(String rewardID) {
		rewards.remove(rewardID);
	} // removeReward
	
	// returns whether this quest's objectives are items
	public boolean getHasItemObjective() {
		return hasItemObjective;
	} // getHasItemObjective
	
} // Quest
