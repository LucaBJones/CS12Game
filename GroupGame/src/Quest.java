import java.util.HashMap;

public class Quest {

	
	private String id;
	private String name;
	
	private boolean hasItemObjective;

	private HashMap<String, Integer> objectives;	// stores item id and num for each objective
	private HashMap<String, Integer> rewards;		// stores item id and num of that item of rewards
	
	private int status; // -1 = locked, 0 = unlocked, 1 = complete, 2 = pending rewards
	
	public Quest(String id, String name, HashMap<String, Integer> objectives, boolean itemObjective, HashMap<String, Integer> rewards, QuestLog log) {
		this.id = id;
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
	
	public void complete() {
		if (status == 0) {
			status = 1;
		} // if
	} // complete
	
	public void unlock() {
		if (status == -1) {
			status = 0;
		} // if
	} // unlock
	
	public boolean getIsCompleted() {
		return status == 1;
	} // getIsCompleted
	
	public String getName() {
		return name;
	} // getIsCompleted
	
	public int getStatus() {
		return status;
	} // getStatus
	
	public void setStatus(int n) {
		status = n;
	}
	
	public void removeReward(String rewardID) {
		rewards.remove(rewardID);
	}
	
	public boolean getHasItemObjective() {
		return hasItemObjective;
	}
	
} // Quest
