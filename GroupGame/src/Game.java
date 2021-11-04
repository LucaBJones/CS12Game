import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Game extends Canvas {
	
	private BufferStrategy strategy;

	private boolean gameIsRunning = false;
	
	private static int[][] map = {
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
		};
	
	private static Tile[][] tileMap = new Tile[map.length][map[0].length]; // array or arrayList?
	private static ArrayList<Entity> entityArray = new ArrayList<Entity>();
	private static ArrayList<Attack> attacks = new ArrayList<Attack>();
	private static ArrayList<Character> characters = new ArrayList<Character>();
	
	private Character player;
	private Inventory inv;
	private boolean inventoryVisible = false; 
	
	private static Tooltip tooltip;
	private DialogueManager dialogue;
	
	private QuestLog questLog;
	
	// for character movement and attacks
	private boolean upPressed = false;
	private boolean leftPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	private boolean shotFired = false;
	private boolean melee = false;
	private static int speed = 300;
	
	private int horizontalDirection = 0; 	// stores direction for projectiles // we could use the direction enum for this
	private int verticalDirection = 1;		// values: -1, 0, 1 
	private int projectileSpeed = 1000; 	// may want to change this later in the game as power up or something
	
	private long lastFire = 0; // time last shot fired
    private long firingInterval = 300; // interval between shots (ms)
    
    private long lastRegen = 0; // time stamina was last regenerated
    private long regenInterval = 500; // interval between stamina regen
    
    private long lastDamage = 0;
    private long damageInterval = 500;
	
	public static void main(String[] args) {
		new Game();
	} // Game
	
	// start the game
	public Game() {
		
		// draw the window
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
		setBounds(0, 0, Camera.getWidth(), Camera.getHeight());
		
		panel.setPreferredSize(new Dimension(Camera.getWidth(), Camera.getHeight()));
		panel.setLayout(null);
		panel.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setResizable(false); // can change
		frame.setVisible(true);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
		
		
		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);
		
		// initialize entities
		initGame();
		
		// add key listener
		addKeyListener(new KeyInputHandler());
		requestFocus();
		
		// add mouse listeners
		MouseMotion mouseMotion = new MouseMotion();
		addMouseMotionListener(mouseMotion);
		addMouseListener(mouseMotion);
		
		// start the game
		gameIsRunning = true;
		gameLoop();
		
	} // Game
	
	// initialize entities
	private void initGame() {
		
		// initialize tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				boolean b = (map[i][j] > 1);
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", 60 * j, 60 *i, b);
				entityArray.add(tileMap[i][j]);
			} // for
		} // for
		
		// create characters
		player = new Character("animations/player/walk_w0.png", 0, 0, 0, 0, true);
		Camera.center(player);
		Character enemy = new Character("images/char_sw.png", 180, 180, 0, 0, false);
		entityArray.add(enemy);
		characters.add(enemy);
		entityArray.add(player);
		characters.add(player);
		System.out.println(enemy.getHitBox());
		
		// create inventory and tooltip
		inv = new Inventory(3); // size of inventory
		tooltip = new Tooltip();

		questLog = new QuestLog(inv);
		initItems();
		initQuests();
		initDialogue();
		questLog.setDialogue(dialogue); // important... and probably not good
		
		dialogue.start("q1Start"); // uncomment to display example dialogue
		
	} // initEntities

	private void initItems() {

		// items
		new InventoryItem("apple", "images/sprite1.png", "Apple", "A juicy red apple. Looks tasty. Eat to replenish health.");
		new InventoryItem("key", "images/sprite2.png", "Key", "Key to the demon’s lair.");
		
	} // initItems
	
	private void initDialogue() {
		// temp, example dialogue
		dialogue = new DialogueManager(questLog);
		
//		new DialogueNode("1", "NPC1", "Hello, my name is...", new String[] {"2"}, dialogue);
//		
//		new DialogueNode("2", "NPC1", "Wait! Don't leave, let me finish first...", new String[] {"3"}, dialogue);
//		
//		new DialogueNode("3", "NPC1", "Hey!!!", new String[] {"4", "5"}, dialogue);
//		
//		DialogueNode d4 = new DialogueNode("4", "NPC1", "How dare you!", null, dialogue);
//		d4.setChoiceText("Leave me alone...");
//		
//		DialogueNode d5 = new DialogueNode("5", "NPC1", "Yes!!! I am!!!!", new String[] {"6"}, dialogue);
//		d5.setChoiceText("What? Are you talking to me?");
//		
//		new DialogueNode("6", "NPC1", "I just wanted to ask... Do you have any apples?", new String[] {"7", "8", "9"}, dialogue);
//		
//		DialogueNode d7 = new DialogueNode("7", "NPC1", "Really? That'd be great!", null, dialogue);
//		d7.setChoiceText("I can go find some for you.");
//		d7.setQuestToUnlock("q1");
//		
//		DialogueNode d8 = new DialogueNode("8", "NPC1", "Oh.. Sorry to bother you...", null, dialogue);
//		d8.setChoiceText("No.");
//		
//		DialogueNode d9 = new DialogueNode("9", "NPC1", "What? Really? You have apples?", new String[] {"10"}, dialogue);
//		d9.setChoiceText("Here are some apples");
//		d9.setChoicePrerequisite("q1", 0); // -1 = locked, 0 = unlocked, 1 = completed
//		
//		new CompleteQuestNode("10", "NPC1", // id and speaker
//				"q1", // questID
//				"Thank you so much! Here. I'll give you a coin in return.", "11",  // text and next node if quest is complete
//				"You said you had apples... Where are they?", "12", // text and next node if quest is incomplete
//				"13", // next node if inv is full
//				dialogue);
//		
//		new DialogueNode("13", "NPC1", "Is your bag full? That's fine, just come back whenever you're ready. I'll give you the coin then.", null, dialogue);
//		
//		new DialogueNode("11", "NPC1", "Thanks again.", null, dialogue);
//		new DialogueNode("12", "NPC1", "Come back when you actually get some.", null, dialogue);
//	
	
		// Opening/intro dialogue
		new DialogueNode("intro", "", "Hey, you. You’re finally awake.", new String[] {"introChoice1", "introChoice2"}, dialogue);
		
		DialogueNode introChoice1 = new DialogueNode("introChoice1", "", "You don’t remember? Hmph. Some hunter you are.", new String[] {"intro2"}, dialogue);
		introChoice1.setChoiceText("... What? Where am I?");
		
		DialogueNode introChoice2 = new DialogueNode("introChoice2", "", "I am the chief of this town. Don’t you remember me?", new String[] {"intro2"}, dialogue);
		introChoice2.setChoiceText("Who are you?");
		
		new DialogueNode("intro2", "", "You’re the one who’s meant to take down the demon lord up north.", new String[] {"introChoice3"}, dialogue);
		
		DialogueNode introChoice3 = new DialogueNode("introChoice3", "", "No memory, huh. Interesting. ", new String[] {"intro3"}, dialogue);
		introChoice3.setChoiceText("The what now?");
		
		new DialogueNode("intro3", "", "You are a hunter, but since you’ve lost your memory, I shall assign you a task to determine if you are worthy.", new String[] {"q1Start"}, dialogue);
		
		// Starting Village Quests
		
		//----------------------------
		// First Quest: Collect Apples
		//----------------------------
		new DialogueNode("q1Start", "", "Anyways, you look like you haven’t eaten in days.", new String[] {"q1Start2"}, dialogue);
		new DialogueNode("q1Start2", "", "There are some apples in the orchard east of here.", new String[] {"q1Start3"}, dialogue);
		new DialogueNode("q1Start3", "", "Go collect some - you’ll need the energy.", new String[] {"q1Start4"}, dialogue);
		DialogueNode q1Start = new DialogueNode("q1Start4", "", "When you’ve done that, we can talk about your quest.", null, dialogue);
		q1Start.setQuestToUnlock("q1");
		
		// dialogue when quest 1 is in progress
		new DialogueNode("chief", "", "Hello there.", new String[] {"q1InProgress", "q1Complete", "chief2"}, dialogue);
		DialogueNode q1InProgress = new DialogueNode("q1InProgress", "", "Come back when you’ve gotten some apples. They’re in the orchard to the east.", null, dialogue);
		q1InProgress.setChoicePrerequisite("q1", 0);
		
		// dialogue when quest 1 is complete
		new DialogueNode("q1Complete", "", "Eating food will replenish your health.", new String[] {"q1Complete2"}, dialogue);
		new DialogueNode("q1Complete2", "", "That’ll be useful on your journey.", null, dialogue);
		
		// random smalltalk
		new DialogueNode("chief2", "", "What are you still doing here?", null, dialogue);
		new DialogueNode("chief2", "", "Have you defeated the demon lord yet?", null, dialogue);
		
		
		//---------------------------------
		// Second quest: Kill a few enemies
		//---------------------------------
		DialogueNode q2Start = new DialogueNode("q2Start", "", "Now that you’ve proven to be at least competent, I believe that I can trust you to protect the village from the enemies in the west.", null, dialogue);
		q2Start.setQuestToUnlock("q2");
		
		// during quest 2
		new DialogueNode("q2InProgress", "", "There are a few stray enemies roaming on the field west of here. Take care of them.", null, dialogue);
		
		
		// quest 2 complete
		new DialogueNode("q2Complete", "", "So you can fight.", new String[] {"q2Complete2"}, dialogue);
		new DialogueNode("q2Complete2", "", "Good. I suppose you’ve truly proven yourself.", new String[] {"q2Complete3"}, dialogue);
		new DialogueNode("q2Complete3", "", "Take the road north until you reach the next village.", new String[] {"q2Complete4"}, dialogue);
		new DialogueNode("q2Complete4", "", "The chief there will help you—", new String[] {"q2Complete5"}, dialogue);
		new DialogueNode("q2Complete5", "", "if he’s alive, that is.", null, dialogue);
		
		// quest 2 after completion
		new DialogueNode("q2AfterComplete", "", "Take care, hunter.", null, dialogue);
		
		
		// Second Village (North)
		// Intro Dialogue
		new DialogueNode("northChief", "", "Hello.", new String[] {"northIntro"}, dialogue);
		new DialogueNode("northIntro", "", "Are you the hunter? It’s about time you got here.", new String[] {"northIntro2"}, dialogue);
		new DialogueNode("northIntro2", "", "Better late than never, I suppose.", null, dialogue);
		
		//-------------------------------
		// Third Quest: Kill More Enemies
		//-------------------------------
		new DialogueNode("q3Start", "", "There are enemies surrounding this village.", new String[] {"q3Start2"}, dialogue);
		DialogueNode q3Start = new DialogueNode("q3Start2", "", "You’ll need to get rid of them before I grant you passage to the demon’s lair.", null, dialogue);
		q3Start.setQuestToUnlock("q3");
		
		// quest 3 in progress
		new DialogueNode("q2InProgress", "", "The safety of this village is in your hands, brave hunter.", new String[] {"q2InProgress2"}, dialogue);
		new DialogueNode("q2InProgress2", "", "Let me remind you that I will reward you with passage to the demon’s lair if you are to succeed.", null, dialogue);
		
		// quest 3 complete
		new DialogueNode("q3Complete", "", "Impressive.", new String[] {"q3Complete2"}, dialogue);
		new DialogueNode("q3Complete2", "", "Perhaps you’ll be the first to come back alive from the demon lord’s home.", null, dialogue);
		
		//----------------------------
		// Fourth Quest: Find the key
		//----------------------------
		
		// quest 4 start
		new DialogueNode("q4Start", "", "Now, you’ll need a key to unlock the entrance to the demon lord’s castle.", new String[] {"q4Start2"}, dialogue);
		DialogueNode q4Start = new DialogueNode("q4Start2", "", "Try looking around for it.", null, dialogue);
		q4Start.setQuestToUnlock("q4");
		
		// quest 4 in progress
		new DialogueNode("q4InProgress", "", "I wonder where the key is...", new String[] {"q4InProgress2"}, dialogue);
		new DialogueNode("q4InProgress2", "", "Maybe try looking where there are more enemies.", null, dialogue);
		
		// quest 4 complete
		new DialogueNode("q4Complete", "", "You found it!", new String[] {"q4Complete2"}, dialogue);
		new DialogueNode("q4Complete2", "", "Now you can pass through to the demon’s home.", new String[] {"q4AfterComplete"}, dialogue);
		
		// quest 4 after completion
		new DialogueNode("q4AfterComplete", "", "Best of luck, hunter.", null, dialogue);
		
		//----------------------------
		// Final: Slay the Demon Lord
		//----------------------------
		
		new DialogueNode("final",  "Demon Lord", "Who dares enter my lair?", new String[] {"final2"}, dialogue);
		new DialogueNode("final2", "Demon Lord", "Ah. Could you be the hunter?", new String[] {"final3"}, dialogue);
		new DialogueNode("final3", "Demon Lord", "Do you know how many of your kind I’ve slain?", new String[] {"final4"}, dialogue);
		new DialogueNode("final4", "Demon Lord", "Look around you.", new String[] {"final5"}, dialogue);
		new DialogueNode("final5", "Demon Lord", "You walk on their bones—", new String[] {"final6"}, dialogue);
		new DialogueNode("final6", "Demon Lord", "hundreds of skeletons, all killed by me.", new String[] {"final7"}, dialogue);
		new DialogueNode("final7", "Demon Lord", "What would you think if I were to use those very hero’s remains to conjure an army and decimate the villages of this world?", new String[] {"final8"}, dialogue);
		new DialogueNode("final8", "Demon Lord", "HAHAHA!", null, dialogue);
	}

	private void initQuests() {
		
		
		
		// Quest 1: Collect Food
		HashMap<String, Integer> obj1 = new HashMap<String, Integer>();
		obj1.put("apple", 10); 
			
		new Quest(	"q1",				// quest id
					"Collect Food", 	// name
					obj1, 				// objectives
					null, 				// rewards
					questLog);
		
		// Quest 2: Kill the Stray Enemies
		
		
		// Quest 3: Kill the Surrounding Enemies
		
		
		// Quest 4: Find the Key
		
		new Quest(	"q1",				
					"Find the Key", 	
					obj1, 				
					null, 				
					questLog);
		
		
	}
	
	private void gameLoop() {
		
		long lastLoopTime = System.currentTimeMillis();
		
		while (gameIsRunning) {
			
			// update the last loop time
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// draw graphics and center camera
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, Camera.getWidth(), Camera.getHeight());
			Camera.center(player);
			
			// not necessary right now
			//ArrayList<Entity> tempEntities = (ArrayList<Entity>) entityArray.clone();
			//ArrayList<Character> tempChars = (ArrayList<Character>) characters.clone();
			
			// for removing attacks
			ArrayList<Attack> tempAttacks = (ArrayList<Attack>) attacks.clone();
			
			// draw the entities
			for (Entity e : entityArray) {
				e.draw(g);
			} // for
			
			for (Character c : characters) {
				if (c.isPlayer()) {
					c.drawHitbox(g);
				} else {
					handleEnemyMovement(delta, c, player);
				}
				c.drawHitbox(g);
			} // for
			
//			for (Attack a : attacks) {
//				a.drawHitbox(g);
//			} // for
			
			// draw the inventory
			if (inventoryVisible) {
				inv.draw(g);
			} // if
			
			// display dialogue
			dialogue.draw(g);
			
			// draw the quest log
			questLog.draw(g);
			
			
			// temp
			if (player.animation != null) {
				player.animation.update(delta);
			}
			
			// updates projectile sprites
			for (int i = 0; i < attacks.size(); i++) {
				if (attacks.get(i).animation != null) {
					attacks.get(i).animation.update(delta);
				}
			} // for
			
			// moves projectiles 
			for (Attack a : tempAttacks) {
				a.move(delta);
			} // for
			
			// move the player
			handlePlayerMovement(delta);
			
			if ((System.currentTimeMillis() - lastDamage) > damageInterval) {
				player.playerCollision(characters);
				lastDamage = System.currentTimeMillis();
			} // if
			
			// long range attack
			if (shotFired && !melee && player.getMana().getValue() > 0) {
				Attack p = startAttack(1000, player);
				if (p != null) {
					attacks.add(p);
					entityArray.add(p);
					player.getMana().decrement(10);
					p.setAnimation();
					p.animation.start();
				} // if
				//System.out.println("shot");
			} // if
			
			// short range attack
			if (melee && !shotFired && player.getStamina().getValue() > 10) {
				Attack m = startAttack(100, player);
				if (m != null) {
					attacks.add(m);
					entityArray.add(m);
					player.getStamina().decrement(10);
					m.setAnimation();
					m.animation.start();
				} // if
			} // if
			
			// check for attacks hitting characters
			for (Attack a : tempAttacks) {
				if(a.collidesWith(characters, g)) {
					removeEntity(a);
				} // if
			} // for
			
			// regenerate stamina
			if ((System.currentTimeMillis() - lastRegen) > regenInterval) {
				player.getStamina().increment(1);
				lastRegen = System.currentTimeMillis();
			} // if
			
			if (player.getHp().getValue() <= 0) {
				
				gameOver(g);
				
				entityArray.clear();
				
			}
			
			if (noEnemies(characters)) {
				win(g);
			}
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
		} // while
		
	} // gameLoop

	private void handlePlayerMovement(long delta) {
		
		// reset speed
		player.setXVelocity(0);
		player.setYVelocity(0);
		
		// check for user input
		if (upPressed && !downPressed) {
			
			if (leftPressed && !rightPressed) {
				player.setXVelocity( -1 * speed);
//				player.setSprite("images/char_nw.png");
				horizontalDirection = -1;
				verticalDirection = 0;
			
				player.setDirection(Direction.NW);
			} else if (rightPressed && !leftPressed) {
				player.setYVelocity(-1 * speed);
//				player.setSprite("images/char_ne.png");
				horizontalDirection = 0;
				verticalDirection = -1;
				
				player.setDirection(Direction.NE);
			} else {
				player.setXVelocity(-1 * speed);
				player.setYVelocity(-1 * speed);
//				player.setSprite("images/char_n.png");
				horizontalDirection = -1;
				verticalDirection = -1;
				
				player.setDirection(Direction.N);
			} // else
			
		} else if (leftPressed && !rightPressed) {
			
			if (downPressed && !upPressed) {
				player.setYVelocity(speed);
//				player.setSprite("images/char_sw.png");
				horizontalDirection = 0;
				verticalDirection = 1;
				
				player.setDirection(Direction.SW);
			} else {
				player.setXVelocity((int)(-1 * speed * 0.7));
				player.setYVelocity((int)(speed * 0.7));
//				player.setSprite("images/char_w.png");
				horizontalDirection = -1;
				verticalDirection = 1;
				
				player.setDirection(Direction.W);
			} // else
			
		} else if (downPressed && !upPressed) {
			if (rightPressed && !leftPressed) {
				player.setXVelocity(speed);
//				player.setSprite("images/char_se.png");
				horizontalDirection = 1;
				verticalDirection = 0;
				
				player.setDirection(Direction.SE);
			} else {
				player.setXVelocity(speed);
				player.setYVelocity(speed);
//				player.setSprite("images/char_s.png");
				horizontalDirection = 1;
				verticalDirection = 1;
				
				player.setDirection(Direction.S);
			} // else
			
		} else if (rightPressed && !leftPressed) {
			player.setXVelocity((int)(speed * 0.7));
			player.setYVelocity((int)(-1 * speed * 0.7));
//			player.setSprite("images/char_e.png");
			horizontalDirection = 1;
			verticalDirection = -1;
			
			player.setDirection(Direction.E);
		} // else if
		
		// animation
		player.setWalkAnimation();
		player.animation.start();
		player.move(delta);
		
	} // handlePlayerMovement
	
	private void handleEnemyMovement(long delta, Character enemy, Character player) {
		
		// reset speed
		enemy.setXVelocity(0);
		enemy.setYVelocity(0);
		
		// if on screen 
		// do we want off screen characters to move?
		if(enemy.getScreenPosX() > 0 - 100 && enemy.getScreenPosX() < Camera.getWidth() + 100 && enemy.getScreenPosY() > 0 - 100 && enemy.getScreenPosY() < Camera.getHeight() + 100) {
			/*
			// path finding
			// the algorith should find the tile the player is closest to being on
			// it doesn't matter if a player is on multiple tiles 
			ArrayList<Node> open = new ArrayList<Node>();
			ArrayList<Node> closed = new ArrayList<Node>();
			
			//this doesnt work yet if != 0
			
			// end Node (player location tile)
			int playerTileX = 1;// (int) player.getScreenPosX() / 60;
			int playerTileY = 1; // (int) player.getScreenPosY() / 60;
			Node end = new Node(playerTileX, playerTileY);
			
			// start node
			System.out.println((int) enemy.screenPosX / 60 + " : " + (int) enemy.screenPosY / 60);
			Node start = new Node((int) enemy.screenPosX / 60, (int) enemy.screenPosY / 60, end);
			
			// add start to open
			open.add(start);
			
			
			 * do {
				
				// current = node in open with min f cost
				Node current = getMinFCost(open);
				
				// set the neighbours of this node
				for(int i = -1; i < 2; i ++) {
					for(int j = -1; j < 2; j ++) {
						if(i != 0 && j !=0){
							current.addNeighbour(new Node(current.getY() + i, current.getX() + j, end, current));
						}
					} // for
				} // for
				
				// remove current from open
				open.remove(current);
				
				// add current to closed
				closed.add(current);
				
				//if current == target node: return
				if(current.getX() == end.getX() && current.getY() == end.getY()) {
					System.out.println("break");
					break;
				} // if
				
				// for each neighbour of current:
				for(Node neighbour : current.getNeighbours()) {
					
					//if neigh is not traversable || neighbor is in closed: continue
					// 60 = TILE_LENGTH
					try {
						if (!tileMap[neighbour.getY()][neighbour.getX()].getIsPassable() || closed.contains(neighbour)) {
							continue;
						} // if
					} catch (ArrayIndexOutOfBoundsException e) {
						
						continue;
						
					} // catch
					
					//if new gCost of neighbor < old gCost || neigh !in closed
					if (current.getGCost() + (int) Math.pow((Math.pow(neighbour.getX() - current.getX(), 2) + Math.pow(neighbour.getX() - current.getY(), 2)), 0.5) < neighbour.getGCost() || !closed.contains(neighbour)) {
						
						// set fCost of neigh
						neighbour.setFCost(neighbour.getGCost() + neighbour.getHCost());
						neighbour.setGCost(current.getGCost() + (int) Math.pow((Math.pow(neighbour.getX() - current.getX(), 2) + Math.pow(neighbour.getX() - current.getY(), 2)), 0.5));
						neighbour.setHCost((int) Math.pow((Math.pow(neighbour.getX() - end.getX(), 2) + Math.pow(neighbour.getY() - end.getY(), 2)), 0.5));
						
						// set parent of neighbor to current
						neighbour.setParent(current);
						
						// if neigh !in open
						if (!open.contains(neighbour)) {
							
							// add neigh to open
							open.add(neighbour);
							
						} // if
							
					} // if
					
				} // for
				System.out.println("finding path...");
			} while(true);
			 
			
			//Node goal = getMinFCost(start.getNeighbours());
			//System.out.println(getMinFCost(start.getNeighbours()).getX() + " | " + getMinFCost(start.getNeighbours()).getX());
			*/
			
			int XDirection = (int) (player.getX() - enemy.getX());
			int YDirection = (int) (player.getY() - enemy.getY());
			
//			System.out.println(XDirection);
//			System.out.println(YDirection);
			
			// probably some enemy direction
			// decide the direction
			enemy.setXVelocity( XDirection);
			enemy.setYVelocity( YDirection);
//			enemy.setSprite("images/char_s.png");
			
		} // if
		
		// animation
		enemy.setWalkAnimation();
		enemy.animation.start();
			
		// move
		enemy.move(delta);
	} // handleEnemyMovement
	
	// move?
	// create an attack
	private Attack startAttack(int range, Character shooter) { // do we still need shooter?
		int xSpawn = 0;				// just to make things
		int ySpawn = 0;				// nice and not messy
		double diagonal = 0;		// used to make diagonal shots come from the same place
		final int INIT_DIST = 30;	// distance from player at spawn
		
		// check that we've waited long enough to fire
        if ((System.currentTimeMillis() - lastFire) < firingInterval){
          return null;
        } // if
        lastFire = System.currentTimeMillis();
		
		diagonal = Math.pow(Math.pow(horizontalDirection, 2) + Math.pow(verticalDirection, 2), -0.5);
		xSpawn = (int) (player.getX() + INIT_DIST * horizontalDirection *  diagonal);
		ySpawn = (int) (player.getY() - Entity.TILE_LENGTH + INIT_DIST * verticalDirection * diagonal);		
		
		return new Attack("animations/projectile/shot_s1.png", xSpawn * 60, ySpawn * 60, horizontalDirection * projectileSpeed, verticalDirection * projectileSpeed, range, shooter, shooter.getDirection());

	} // spawnProjectile
	
	// remove an entity from the game
	public static void removeEntity(Entity e) {
		entityArray.remove(e);
		if (e instanceof Attack) {
			attacks.remove(e);
		} else if (e instanceof Character) {
			characters.remove(e);
		} // else if
	} // removeEntity
	
	// handles keyboard input from the user
	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				//System.out.println("Pressed: w or up");
				upPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				//System.out.println("Pressed: a or left");
				leftPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				//System.out.println("Pressed: s or down");
				downPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				//System.out.println("Pressed: d or right");
				rightPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_X) {
				shotFired = true;
				//System.out.println("Pressed: space");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // should we have this?
				System.exit(0);
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				melee = true;
			} // if
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.getHp().decrement(10);
				player.getMana().increment(100);
			}

		} // keyPressed

		public void keyReleased(KeyEvent e) {

			// temp, for testing-------------------------------
			if (e.getKeyCode() == KeyEvent.VK_1) {
				inv.addItem("apple", 1);
				System.out.println("added 1 apple to inv");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_2) {
				questLog.unlock("q1");
				System.out.println("unlocked quest 1");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_3) {
				System.out.println("completing quest 1");
			} // if
			// --------------------------------------------------------
			
			// temp
			if (e.getKeyCode() == KeyEvent.VK_T) {
				dialogue.update();
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				//System.out.println("Released: w or up");
				upPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				//System.out.println("Released: a or left");
				leftPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				//System.out.println("Released: s or down");
				downPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				//System.out.println("Released: d or right");
				rightPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_X) {
				shotFired = false;
				//System.out.println("Released: space");
			}
			
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				melee = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_I) {
				inventoryVisible = !inventoryVisible;
				
				if (!inventoryVisible) {
					inv.stopDrag();
				}
				
				System.out.println("inventoryVisible: " + inventoryVisible);
			} // if
			
			
		} // keyReleased

	} // class KeyInputHandler
	
	private class MouseMotion implements MouseMotionListener, MouseListener {

		// may not need all of these...

		@Override
		public void mouseDragged(MouseEvent e) {
			//System.out.println("MOUSE_DRAGGED: " + " (" + e.getX() + "," + e.getY() + ")" + " detected on " + e.getComponent().getClass().getName());

			if (!inventoryVisible) { return; }
			if (SwingUtilities.isLeftMouseButton(e)) {
				//System.out.println("left button");
				inv.handleDrag(e);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//System.out.println("MOUSE_MOVED: " + " (" + e.getX() + "," + e.getY() + ")" + " detected on "
					//+ e.getComponent().getClass().getName());
			
			if (!inventoryVisible) { return; }
			inv.handleHover(e);
		
		} // mouseMoved

		@Override
		public void mouseClicked(MouseEvent e) {
			dialogue.handleClick(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//System.out.println("enter");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//System.out.println("exit");
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("pressed");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//System.out.println("released");
			if (!inventoryVisible) { return; }
			inv.stopDrag(e);
		}
	} // MouseMotion
	
	public static Tile[][] getTiles() {
		return tileMap;
	}
	
	public static Tooltip getTooltip() {
		return tooltip;
	}
	
	public static ArrayList<Character> getCharacters() {
		return characters;
	}
	
	public boolean noEnemies(ArrayList<Character> chars) {
		for (Character c : chars) {
			if (!c.isPlayer()) {
				return false;
			}
		}
		return true;
	}
	
	public static void gameOver(Graphics2D g) {
		String message = "GAME OVER";
		g.setColor(Color.black);
		g.fillRect(0, 0, Camera.getWidth(), Camera.getHeight());
		g.setColor(Color.white);
        g.setFont(new Font("Purisa" , Font.BOLD, 35));
		g.drawString(message, (Camera.getWidth() - g.getFontMetrics().stringWidth(message)) / 2, Camera.getHeight() / 2);
	} // gameOver
	
	public void newGame() {
		initGame();
		gameLoop();
	}
	
	public static void win(Graphics2D g) {
		String message = "YOU WIN!";
		g.setColor(Color.black);
		g.fillRect(0, 0, Camera.getWidth(), Camera.getHeight());
		g.setColor(Color.white);
        g.setFont(new Font("Purisa" , Font.BOLD, 35));
		g.drawString(message, (Camera.getWidth() - g.getFontMetrics().stringWidth(message))/ 2, Camera.getHeight() / 2);
	} // win
} // Game
