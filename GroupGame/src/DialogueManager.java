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
	
	private int speakerWidth = 350;
	private int speakerHeight = 76;
	
	private int xPadding;
	private int yPadding;
	
	private int x;
	private int y;
	
	private Timer timer;
	private int textSpeed;
	private boolean textIsAnimating;
	private int index;
	
	private QuestLog questLog;
	private Game game;
	
	private Sprite dialogueBox;
	private Sprite speakerBox;
	private Sprite choiceBox;
	
	private Color textColor;
	
	public DialogueManager(QuestLog log, Game g) {
		currentDialogueID = null;
		currentChoice = -1;
		isDisplaying = false;
		textIsAnimating = false;
		waitingForChoice = false;
		displayText = "";
		
		questLog = log;
		game = g;
		
		textSpeed = 20; // can change, lower num is faster
		index = 0;
		
		timer = new Timer(textSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animateText();
            } // actionPerformed
        });
		timer.setInitialDelay(0);
		// can change
		xPadding = (int) (Camera.getWidth() * 0.1);
		yPadding = (int) (Camera.getHeight() * 0.01);
		
		dialogueWidth = (int) (Camera.getWidth() * 0.78);
		dialogueHeight = (int) (Camera.getHeight() * 0.27);
		
		x = 215;
		y = (int) (Camera.getHeight() * 0.9 - dialogueHeight);
		
		choiceWidth = (int) (Camera.getWidth() * 0.263);
		choiceHeight = (int) (Camera.getHeight() * 0.117);
		
		speakerWidth = 350;  
		speakerHeight = 76;  
		
		dialogueBox = SpriteStore.get().getSprite("ui/dialogueBox.png");
		speakerBox = SpriteStore.get().getSprite("ui/speakerBox.png");
		choiceBox = SpriteStore.get().getSprite("ui/choiceBox.png");
		
		textColor = new Color(183,183,183);
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
		displayText = "";
		
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
		
		System.out.println("displayText: " + displayText);
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
			game.stopTalking();
			System.out.println("hello?");
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
			} // else if
			
		} // for
		
		next = new String[temp.size()];
		
		for (int i = 0; i < next.length; i++) {
			next[i] = temp.get(i);
		}
		
		// check if current node is trying to complete a quest
		if (dialogueStore.get(next[0]) instanceof CompleteQuestNode) { // assumes DialogueQuestNode is not a choice
			boolean canComplete = questLog.canComplete(((CompleteQuestNode) dialogueStore.get(next[0])).getQuestToComplete());
					
			((CompleteQuestNode) dialogueStore.get(next[0])).setText(canComplete);
			((CompleteQuestNode) dialogueStore.get(next[0])).setNext(canComplete);
			
			
		} // if
		
		// if there are no choices, set dialogue
		if (next.length == 1) {
			
			// check if next node is not a choice
			if (!dialogueStore.get(next[0]).getIsChoice()) {

			currentDialogueID = next[0];
			handleQuest();
			
			textIsAnimating = true;
			displayText = "";
			index = 0;
			timer.restart();
			return;
			} // if
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
	public void handleClick(MouseEvent e) { // does not work!!!
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
		if (dialogueStore.get(currentDialogueID) instanceof CompleteQuestNode) {
			questLog.complete(((CompleteQuestNode) dialogueStore.get(currentDialogueID)).getQuestToComplete());
		} // if
	} // handleQuest
	
	// sets current dialogue node to uncollected rewards version
	public void rewardsUncollected() {
		((CompleteQuestNode) dialogueStore.get(currentDialogueID)).setToPendingRewardNextID();
	} // rewardsUncollected
	
	// draws the dialogue to the screen
	public void draw(Graphics g) {
		if (!dialogueStore.containsKey(currentDialogueID) || !isDisplaying) { return; }
		
		// set font and text color
		g.setFont(Game.getDidactGothic().deriveFont(28f)); // temp
		g.setColor(textColor);
		
		// check if there is a speaker
		if (!dialogueStore.get(currentDialogueID).getSpeaker().isEmpty()) {
			
			// draw speaker background
			speakerBox.draw(g, x + 20, y - speakerHeight + 20, speakerWidth, speakerHeight);
		
			// display speaker
			g.drawString(dialogueStore.get(currentDialogueID).getSpeaker(), (int) x + 50, (int) y - 10);
		} // if
		
		dialogueBox.draw(g, x, y, dialogueWidth, dialogueHeight);
		System.out.println("drawing dialogue");

		// draw dialogue text
		g.drawString(displayText, (int) x + 50, (int) y + 80); // can change
		
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
			choiceBox.draw(g, choiceX, choiceY, choiceWidth, choiceHeight);
			
			// draw text
			g.setColor(Color.LIGHT_GRAY);
			g.drawString(dialogueStore.get(currentChoiceNodes[i]).getChoiceText(), choiceX + 20, choiceY + 60);
		} // for
		
	} // drawChoices
	
} // DialogueManager
