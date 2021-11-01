
public class DialogueQuestNode extends DialogueNode {

	private String questToComplete;
	
	private String completedText;
	private String completedNextID;
	
	private String incompleteText;
	private String incompleteNextID;
	
	public DialogueQuestNode(String id, String speaker, String questToComplete, String completedText, String completedNextID, String incompleteText, String incompleteNextID, DialogueManager dialogue) {
		super();
		
		this.id = id;
		this.speaker = speaker;
		
		this.questToComplete = questToComplete;
		
		this.completedText = completedText;
		this.completedNextID = completedNextID;
		
		this.incompleteText = incompleteText;
		this.incompleteNextID = incompleteNextID;
		
		
		dialogue.add(id, this);
	} // DialogueQuestNode
	
	public void setText(boolean canComplete) {
		text = (canComplete) ? completedText : incompleteText; // sets text, not sure if this will cause problems later
	}
	
	public void setNext(boolean canComplete) {
		nextIDs = new String[] { ((canComplete) ? completedNextID : incompleteNextID) };
	}
	
	public String getQuestToComplete() {
		return questToComplete;
	}
	
	
} // DialogueQuestNode
