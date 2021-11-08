import java.awt.Graphics;
import java.awt.Point;

public class NPC extends Entity {

	String dialogueID;
	int range; // how far from the npc the player can be to start a conversation
	
	Sprite character;
	
	public NPC(String dialogue, String sprite, int xPos, int yPos) {
		super(sprite, xPos, yPos);
		
		character = SpriteStore.get().getSprite(sprite);
		animation = new Animation(this, "animations/emotes/", ".png", 0, 3, 700, true);
		animation.start();
		
		dialogueID = dialogue;
		range = 30;
	} // NPC
	
	// returns the root dialogue node
	public String getDialogue() {
		return dialogueID;
	} // getDialogue
	
	// returns whether the player is within range of this npc
	public boolean withinRange(int playerX, int playerY) {
		return playerX - x < range && playerY - y < range;
	} // withinRange
	
	// draws the NPC and it's emotes
	@Override
	public void draw(Graphics g) {
		
		// draw emote above npc's head
		super.draw(g, (character.getWidth() - sprite.getWidth()) / 2, -character.getHeight() - 10);
		
		// calculate position of npc
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - character.getHeight() - Camera.getY();
		
		// draw npc
		character.draw(g, screenPosX, screenPosY);
	} // draw
	
} // NPC
