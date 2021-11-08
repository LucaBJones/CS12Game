
public class CompleteQuestNode extends DialogueNode {

	private String questToComplete;
	
	private String completedText;
	private String completedNextID;
	
	private String incompleteText;
	private String incompleteNextID;
	
	private String pendingRewardNextID;
	
	public CompleteQuestNode(String id, String speaker, String questToComplete, String completedText, String completedNextID, String incompleteText, String incompleteNextID, String pendingRewardNextID, DialogueManager dialogue) {
		super();
		
		this.id = id;
		this.speaker = speaker;
		
		this.questToComplete = questToComplete;
		
		this.completedText = completedText;
		this.completedNextID = completedNextID;
		
		this.incompleteText = incompleteText;
		this.incompleteNextID = incompleteNextID;
		
		this.pendingRewardNextID = pendingRewardNextID;
		
		dialogue.add(id, this);
	} // DialogueQuestNode
	
	// sets text based on whether quest can be completed
	public void setText(boolean canComplete) {
		text = (canComplete) ? completedText : incompleteText;
	} // setText
	
	// sets next node based on whether quest can be completed
	public void setNext(boolean canComplete) {
		nextIDs = new String[] { ((canComplete) ? completedNextID : incompleteNextID) };
	} // setNext
	
	// sets next node to nextID for pending rewards
	public void setToPendingRewardNextID() {
		nextIDs = new String[] { pendingRewardNextID };
	} // setToNextIDToPendingReward
	
	// returns the quest to complete
	public String getQuestToComplete() {
		return questToComplete;
	} // getQuestToComplete
	
} // DialogueQuestNode
