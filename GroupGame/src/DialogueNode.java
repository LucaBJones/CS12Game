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

	// choice dialogueNode 
	public DialogueNode(String id, String speaker, String text, String choiceText, String[] nextIDs,
			DialogueManager d) {

		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;

		this.choiceText = choiceText;

		this.questToUnlock = "";

		this.prerequisiteQuest = "";
		this.prerequisiteStatus = -1;

		d.add(id, this);
	} // DialogueNode

	// choice dialogueNode with prerequisites
	public DialogueNode(String id, String speaker, String text, String choiceText, String prerequisiteQuest,
			int prerequisiteStatus, String[] nextIDs, DialogueManager d) {

		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;

		this.choiceText = choiceText;

		this.questToUnlock = "";

		this.prerequisiteQuest = prerequisiteQuest;
		this.prerequisiteStatus = prerequisiteStatus;

		d.add(id, this);
	} // DialogueNode

	// choice + questToUnlock dialogueNode
	public DialogueNode(String id, String speaker, String text, String choiceText, String[] nextIDs, DialogueManager d,
			String questToUnlock) {

		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;

		this.choiceText = choiceText;

		this.questToUnlock = questToUnlock;
		System.out.println("init, " + id + ": " + questToUnlock);

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

} // DialogueNode
