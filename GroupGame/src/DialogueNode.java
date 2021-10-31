public class DialogueNode {
	private String id;
	private String text;
	private String speaker;
	private String[] nextIDs;
	
	private String choiceText;
	
	// normal dialogueNode
	public DialogueNode(String id, String speaker, String text, String[] nextIDs, DialogueManager d) {
		
		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;
		
		this.choiceText = "";
		
		d.add(id, this);
	} // DialogueNode
	
	// choice dialogueNode (should there only be one constructor?)
	public DialogueNode(String id, String speaker, String text, String choiceText, String[] nextIDs, DialogueManager d) {
		
		this.id = id;
		this.text = text;
		this.speaker = speaker;
		this.nextIDs = nextIDs;
		
		this.choiceText = choiceText;
		
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
} // DialogueNode
