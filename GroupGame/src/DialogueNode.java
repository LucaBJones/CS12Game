public class DialogueNode {
	protected String id;
	protected String text;
	protected String speaker;
	protected String[] nextIDs;

	private String choiceText;

	private String questToUnlock;

	private String prerequisiteQuest;
	private int prerequisiteStatus; // -1 = locked, 0 = unlocked, 1 = completed

	public DialogueNode() {
		id = "";
		text = "";
		speaker = "";
		nextIDs = null;
		choiceText = "";
		questToUnlock = "";
	}

	// normal, text-only dialogueNode
	public DialogueNode(String id, String speaker, String text, String[] nextIDs, DialogueManager d) {

		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;

		this.choiceText = "";

		this.questToUnlock = "";

		d.add(id, this);
	} // DialogueNode

	// returns the dialogue text
	public String getText() {
		return text;
	} // getText

	// returns the name of the speaker
	public String getSpeaker() {
		return speaker;
	} // getSpeaker

	// returns the id of the next dialogue node
	public String[] getNext() {
		return nextIDs;
	} // getNext

	// returns the choice text
	public String getChoiceText() {
		return choiceText;
	} // getChoiceText

	public String getQuestToUnlock() {
		return questToUnlock;
	}

	public String getPrerequisiteQuest() {
		return prerequisiteQuest;
	}
	
	public int getPrerequisiteStatus() {
		return prerequisiteStatus;
	}

	public void setText(String text) {
		this.text = text;
	} // setText
	
	public void setChoiceText(String choiceText) {
		this.choiceText = choiceText;
	} // setChoiceText
	
	public void setChoicePrerequisite(String questID, int questStatus) {
		prerequisiteQuest = questID;
		prerequisiteStatus = questStatus;
	} // setChoicePrerequisite

	public void setQuestToUnlock(String questID) {
		questToUnlock = questID;
	} // setQuestToUnlock
	
} // DialogueNode
