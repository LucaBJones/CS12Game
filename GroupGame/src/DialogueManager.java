import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
	private int y;
	
	private Timer timer;
	private int textSpeed;
	private boolean textIsAnimating;
	private int index;
	
	private QuestLog questLog;
	
	public DialogueManager(QuestLog log) {
		currentDialogueID = null;
		currentChoice = -1;
		isDisplaying = false;
		textIsAnimating = false;
		waitingForChoice = false;
		displayText = "";
		
		questLog = log;
		
		textSpeed = 30; // can change, lower num is faster
		index = 0;
		
		timer = new Timer(textSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animateText();
            } // actionPerformed
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
	
	// store dialogue node into dialogueStore
	public void add(String id, DialogueNode node) {
		dialogueStore.put(id, node);
	} // add
	
	// start a dialogue with the id of the first node of the dialogue
	public void start(String id) {
		isDisplaying = true;
		waitingForChoice = false;
		textIsAnimating = true;
		currentDialogueID = id;
		
		timer.start();
	} // start
	
	// animates the text (one letter at a time)
	private void animateText() {
		if (!isDisplaying) { return; } // needed?
		if (index > dialogueStore.get(currentDialogueID).getText().length() - 1) { 
			timer.stop();
			textIsAnimating = false;
			return;
		} // if
		
		displayText += dialogueStore.get(currentDialogueID).getText().charAt(index);
		index++;
	} // updateText
	
	// updates to next dialogue node if there is one
	public void update() {
		if (!isDisplaying || waitingForChoice) { return; }
		
		DialogueNode current = dialogueStore.get(currentDialogueID);
		String[] next;
		ArrayList<String> temp = new ArrayList<String>();
		
		// if text is animating, display full line of text
		if (textIsAnimating) {
			displayText = current.getText();
			timer.stop();
			textIsAnimating = false;
			return;
		} // if
		
		// check if there is any more dialogue
		if (current.getNext() == null) { 
			isDisplaying = false;
			return; 
		} // if
		
		// get the next dialogue nodes
		// (only get the ones that the player meets the prerequisite for)
		for (String str : current.getNext()) {
			String prerequisiteQuest = dialogueStore.get(str).getPrerequisiteQuest();
			if (prerequisiteQuest == null || prerequisiteQuest.isEmpty()) { // check if is null?
				temp.add(str);
			} else if (questLog.get(prerequisiteQuest).getStatus() == dialogueStore.get(str).getPrerequisiteStatus()) {
				temp.add(str);
			}
			
		} // for
		
		next = new String[temp.size()];
		
		for (int i = 0; i < next.length; i++) {
			next[i] = temp.get(i);
		}
		
		// check if current node is trying to complete a quest
		if (dialogueStore.get(next[0]) instanceof DialogueQuestNode) { // assumes DialogueQuestNode is not a choice
			boolean canComplete = questLog.canComplete(((DialogueQuestNode) dialogueStore.get(next[0])).getQuestToComplete());
			((DialogueQuestNode) dialogueStore.get(next[0])).setText(canComplete);
			((DialogueQuestNode) dialogueStore.get(next[0])).setNext(canComplete);
		}
		
		// if there are no choices, set dialogue
		if (next.length == 1) {
			currentDialogueID = next[0];
			handleQuest();
			
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
		
		// update to dialogue that player chose and reset
		currentDialogueID = next[currentChoice];
		handleQuest();
		
		currentChoice = -1;
		displayText = "";
		index = 0;
		timer.restart();
		textIsAnimating = true;
	} // update
	
	// sets currentChoice to what the player has chosen
	private void choose(int n) {
		if (!waitingForChoice) { return; }
		currentChoice = n;
		waitingForChoice = false;
		update();
	} // choose
	
	// handles clicking on a choice
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
	
	// handles unlocking and completing quests linked to the current dialogue
	private void handleQuest() {
		String questToUnlock = dialogueStore.get(currentDialogueID).getQuestToUnlock();

		System.out.println("handleQuest: " + questToUnlock);
		// if there is a quest to unlock, unlock it
		if (!questToUnlock.isEmpty()) {
			questLog.unlock(questToUnlock);
			System.out.println("quest: " + questToUnlock + " unlocked!");
		} // if
		
		// if there is a quest to complete, complete it
		if (dialogueStore.get(currentDialogueID) instanceof DialogueQuestNode) {
			questLog.complete(((DialogueQuestNode) dialogueStore.get(currentDialogueID)).getQuestToComplete());
		} // if
	} // handleQuest
	
	// draws the dialogue to the screen
	public void draw(Graphics g) {
		if (!dialogueStore.containsKey(currentDialogueID) || !isDisplaying) { return; }
		
		// draw background (use sprites?)
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) x, (int) y, dialogueWidth, dialogueHeight);

		// draw dialogue text
		g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 2f)); // temp
		g.setColor(Color.black);
		g.drawString(displayText, (int) x + 30, (int) y + 40); // can change
	
		// display speaker background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) x , (int) y - 50, 160, 40);
		
		// display speaker
		g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 0.6f)); // temp
		g.setColor(Color.BLACK);
		g.drawString(dialogueStore.get(currentDialogueID).getSpeaker(), (int) x + 10, (int) y - 30);
		
		// draw choices if there are any
		if (waitingForChoice) {
			drawChoices(g);
		} // if
	} // draw
	
	// draws the choices to the screen
	private void drawChoices(Graphics g) {
		for (int i = 0; i < currentChoiceNodes.length; i++) {
			int choiceX = Camera.getWidth() - choiceWidth - xPadding;
			int choiceY = (int) y - (choiceHeight) * (i + 1) - ((i + 1) * yPadding);
			
			// draw background
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(choiceX, choiceY, choiceWidth, choiceHeight);
			
			// draw text
			g.setColor(Color.black);
			g.drawString(dialogueStore.get(currentChoiceNodes[i]).getChoiceText(), choiceX + 10, choiceY + 20); // idk why we need to add more to the y... but it works...
		} // for
		
	} // drawChoices
	
} // DialogueManager
