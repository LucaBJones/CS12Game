import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.Timer;

public class DialogueManager {
	
	private HashMap<String, DialogueNode> dialogueStore = new HashMap<String, DialogueNode>();
	
	private boolean isDisplaying;
	private String displayText;
	
	private String currentDialogueID;
	private String[] currentChoiceNodes;
	
	private boolean waitingForChoice;
	private int currentChoice;
	
	private int dialogueWidth;
	private int dialogueHeight;
	
	private int choiceWidth;
	private int choiceHeight;
	
	private int xPadding;
	private int yPadding;
	
	private int x;
	private int y; // inherit from Entity instead? (then can use Sprite too)
	
	private Timer timer;
	private int textSpeed;
	private boolean textIsAnimating;
	private int index;
	
	public DialogueManager() {
		currentDialogueID = null;
		currentChoice = -1;
		isDisplaying = false;
		textIsAnimating = false;
		waitingForChoice = false;
		displayText = "";
		
		textSpeed = 30; // can change, lower num is faster
		index = 0;
		
		timer = new Timer(textSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateText();
            }
        });
		
		// can change
		xPadding = 10;
		yPadding = 10;
		
		dialogueWidth = Camera.getWidth() - 2 * xPadding;
		dialogueHeight = (int) (Camera.getHeight() * 0.2);
		
		x = 10;
		y = Camera.getHeight() - dialogueHeight - yPadding;
		
		choiceWidth = (int) (Camera.getWidth() * 0.3);
		choiceHeight = (int) (Camera.getHeight() * 0.08);
	} // DialogueManager
	
	public void add(String id, DialogueNode node) {
		dialogueStore.put(id, node);
	}
	
	public void start(String id) {
		isDisplaying = true;
		waitingForChoice = false;
		textIsAnimating = true;
		currentDialogueID = id;
		
		timer.start();
	}
	
	public void updateText() {
		if (!isDisplaying) { return; } // needed?
		if (index > dialogueStore.get(currentDialogueID).getText().length() - 1) { 
			timer.stop();
			textIsAnimating = false;
			return;
		}
		
		displayText += dialogueStore.get(currentDialogueID).getText().charAt(index);
		index++;
	}
	
	public void update() {
		if (!isDisplaying || waitingForChoice) { return; }
		
		// if text is animation, display full line of text
		if (textIsAnimating) {
			displayText = dialogueStore.get(currentDialogueID).getText();
			timer.stop();
			textIsAnimating = false;
			return;
		} // if
		
		// get the next dialogue nodes
		String[] next = dialogueStore.get(currentDialogueID).getNext();
		
		// check if there is any more dialogue
		if (next == null) { return; }
		
		// if there are no choices, set dialogue
		if (next.length == 1) {
			currentDialogueID = next[0];
			
			textIsAnimating = true;
			displayText = "";
			index = 0;
			timer.restart();
			return;
		} // if
		
		// check if there are multiple choices and that the player hasn't chosen yet
		if (currentChoice < 0) {
			waitingForChoice = true;
			currentChoiceNodes = next;
			return;
		} // if
		
		// update to dialogue that player chose
		currentDialogueID = next[currentChoice];
		displayText = "";
		index = 0;
		timer.restart();
		textIsAnimating = true;
	} // update
	
	public void choose(int n) {
		if (!waitingForChoice) { return; }
		currentChoice = n;
		waitingForChoice = false;
		update();
	} // choose
	
	public void handleClick(MouseEvent e) {
		if (!isDisplaying) { return; }
		
		if (currentChoiceNodes == null || currentChoiceNodes.length == 0) { return; }
		for (int i = 0; i < currentChoiceNodes.length; i++) {
			int choiceX = Camera.getWidth() - choiceWidth - xPadding;
			int choiceY = (int) y - (choiceHeight) * (i + 1) - ((i + 1) * yPadding);
			
			// if clicking on an option, choose it
			if (e.getX() > choiceX && e.getX() < choiceX + choiceWidth && e.getY() > choiceY && e.getY() < choiceY + choiceHeight) {
				choose(i);
			} // if
		
		} // for
	} // handleClick
	
	public void draw(Graphics g) {
		if (!dialogueStore.containsKey(currentDialogueID)) { return; }
		
		// draw background (use sprites?)
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) x, (int) y, dialogueWidth, dialogueHeight);

		g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 2f)); // temp
		g.setColor(Color.black);
		g.drawString(displayText, (int) x + 30, (int) y + 40); // can change
	
		// display speaker
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) x , (int) y - 50, 160, 40);
		
		g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 0.6f)); // temp
		g.setColor(Color.BLACK);
		g.drawString(dialogueStore.get(currentDialogueID).getSpeaker(), (int) x + 10, (int) y - 30);
		
		// draw choices if there are any
		if (waitingForChoice) {
			drawChoices(g);
		} // if
	} // draw
	
	public void drawChoices(Graphics g) {
		
		for (int i = 0; i < currentChoiceNodes.length; i++) {
			int choiceX = Camera.getWidth() - choiceWidth - xPadding;
			int choiceY = (int) y - (choiceHeight) * (i + 1) - ((i + 1) * yPadding);
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(choiceX, choiceY, choiceWidth, choiceHeight);
			
			g.setColor(Color.black);
			g.drawString(dialogueStore.get(currentChoiceNodes[i]).getChoiceText(), choiceX + 10, choiceY + 20); // idk why we need to add more to the y... but it works...
		} // for
		
	} // drawChoices
	
} // DialogueManager
