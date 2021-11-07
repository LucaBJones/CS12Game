import java.awt.Graphics;
import java.awt.Point;

public class NPC extends Entity {

	String dialogueID;
	int range;
	
	Sprite character;
	
	public NPC(String dialogue, String sprite, int xPos, int yPos) {
		super(sprite, xPos, yPos);
		
		character = SpriteStore.get().getSprite(sprite);
		animation = new Animation(this, "animations/emotes/", ".png", 0, 3, 700);
		animation.start();
		
		dialogueID = dialogue;
		range = 50;
	}
	
	public String getDialogue() {
		return dialogueID;
	}
	
	public boolean withinRange(int playerX, int playerY) {
		return playerX - x < range && playerY - y < range;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g, (character.getWidth() - sprite.getWidth()) / 2, -character.getHeight() - 10);
		
		// calculate position of npc
		Point isoPoint = toIso((int) x, (int) y);
		screenPosX = isoPoint.x - Camera.getX();
		screenPosY = isoPoint.y + TILE_LENGTH - character.getHeight() - Camera.getY();
		
		// draw npc
		character.draw(g, screenPosX, screenPosY);
	}
}