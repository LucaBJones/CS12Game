
public class NPC extends Entity {

	String dialogueID;
	int range;
	
	public NPC(String dialogue, String sprite, int xPos, int yPos) {
		super(sprite, xPos, yPos);
		
		dialogueID = dialogue;
		range = 50;
	}
	
	public String getDialogue() {
		return dialogueID;
	}
	
	public boolean withinRange(int playerX, int playerY) {
		return playerX - x < range && playerY - y < range;
	}
}