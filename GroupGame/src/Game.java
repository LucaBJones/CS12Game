import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
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
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<Attack> attacks = new ArrayList<Attack>();
	private static ArrayList<Character> characters = new ArrayList<Character>();
	private static ArrayList<NPC> npcs = new ArrayList<NPC>(); // temp?
	
	private Character player;
	private Inventory inv;
	private boolean inventoryVisible = false; 
	
	private static Tooltip tooltip;
	private DialogueManager dialogue;
	private boolean isTalking = false;
	
	private QuestLog questLog;
	
	// for character movement and attacks
	private boolean upPressed = false;
	private boolean leftPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	private boolean shotFired = false;
	private boolean melee = false;
	private boolean isRunning = false;
	
	private int walkSpeed = 200;
	private int runSpeed = 400;
	private int currentSpeed = walkSpeed;
	
	private int screen = 0; // 0 for menu, 1 for instructions, 2 for credits, 3 for win, 4 for game over, 5 for start game
	
	private Image menu;			// use sprites?
	private Image credits;
	private Image instructions;
	private Image gameOver;
	private Image win;
	private Image icon;
	
	private static Font medievalSharp32;
	private static Font didactGothic28;
	
	private int horizontalDirection = 0; 	// stores direction for projectiles // we could use the direction enum for this
	private int verticalDirection = 1;		// values: -1, 0, 1 
	private int projectileSpeed = 1000; 	// may want to change this later in the game as power up or something
	
	private long lastFire = 0; // time last shot fired
    private long firingInterval = 300; // interval between shots (ms)
    
    private long lastStaminaRegen = 0; // time stamina was last regenerated
    private long staminaRegenInterval = 2000; // interval between stamina regen
    private long lastManaRegen = 0;
    private long manaRegenInterval = 3000;
    
    private long lastStaminaConsumption = 0;
    private long staminaConsumeInterval = 400;
    
    private long lastDamage = 0;
    private long damageInterval = 500;
	
	public static void main(String[] args) {
		new Game();
	} // Game
	
	// set up the window and show the menu
	public Game() {
		
		// draw the window
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
//		setBounds(0, 0, Camera.getWidth(), Camera.getHeight());
		
		panel.setPreferredSize(new Dimension(Camera.getWidth(), Camera.getHeight()));
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		panel.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setResizable(false); // can change
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
	
		setBounds((frame.getWidth() - Camera.getWidth()) / 2, (frame.getHeight() - Camera.getHeight()) / 2, Camera.getWidth(), Camera.getHeight());
		
		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);
		
		// add key listener
		addKeyListener(new KeyInputHandler());
		requestFocus();
		
		// add mouse listeners
		MouseMotion mouseMotion = new MouseMotion();
		addMouseMotionListener(mouseMotion);
		addMouseListener(mouseMotion);
		
		// Create Image Object
        Toolkit tk = Toolkit.getDefaultToolkit();
		
		// init screen backgrounds
		menu = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("screens/menu.png"));
		credits = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("screens/credits.png"));
		instructions = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("screens/instructions.png"));
		gameOver = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("screens/gameover.png"));
		win = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("screens/win.png"));
		
		// init icon image
		icon = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("ui/icon.png"));
		
		// load didact Gothic font
        File f = new File(Game.class.getResource("fonts/didactGothic.ttf").getFile());
        FileInputStream in = null;
        
        try {
        	if (f.exists()) {
        		in = new FileInputStream(f);
        		Font didactGothic = Font.createFont(Font.TRUETYPE_FONT, in);
        		didactGothic28 = didactGothic.deriveFont(28f);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } // catch
        
        
        // load medieval sharp font
        f = new File("fonts/medievalSharp.ttf");
        
        try {
        	if (f.exists()) {
        		in = new FileInputStream(f);
                Font medievalSharp = Font.createFont(Font.TRUETYPE_FONT, in);
                medievalSharp32 = medievalSharp.deriveFont(32f);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } // catch

		// start the game
		initEntities();
		gameLoop();
		
	} // Game
	
	// initialize entities
	private void initEntities() {
		
		// initialize tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				boolean b = (map[i][j] > 1);
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", 60 * j, 60 *i, b);
				entities.add(tileMap[i][j]);
			} // for
		} // for
		
		// create characters
		player = new Character("animations/player/walk_w0.png", 0, 0, 0, 0, true);
		Camera.center(player);
		Character enemy = new Character("animations/player/walk_w0.png", 180, 180, 0, 0, false);
		entities.add(enemy);
		characters.add(enemy);
		entities.add(player);
		characters.add(player);
		System.out.println(enemy.getHitBox());
		
		
		// temp?
		npcs.add(new NPC("intro", "images/npc/npc_s.png", 400, 400));
//		npcs.add(new NPC("chief2", "images/npc/npc_s.png", 800, 800));
		entities.addAll(npcs);
		
		
		// create inventory and tooltip
		inv = new Inventory(4); // size of inventory
		tooltip = new Tooltip();

		questLog = new QuestLog(inv);
		initItems();
		initQuests();
		initDialogue();
		questLog.setDialogue(dialogue); // important... and probably not good
		
	} // initEntities

	private void initItems() {

		// items
		new InventoryItem("apple", "images/tile1.png", "Apple", "A juicy red apple. Looks tasty. Eat to replenish health.");
		new InventoryItem("key", "images/tile2.png", "Key", "Key to the demon’s lair.");
		
	} // initItems
	
	private void initDialogue() {
		// temp, example dialogue
		dialogue = new DialogueManager(questLog, this);
	
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
		new DialogueNode("chief2", "", "What are you still doing here?", new String[] {"chief2_1"}, dialogue);
		new DialogueNode("chief2_1", "", "Have you defeated the demon lord yet?", null, dialogue);
		
		
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
		
		while (true) {
			
			// update the last loop time
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// draw graphics and center camera
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, Camera.getWidth(), Camera.getHeight());
			
			if (screen != 5) {
				
				// show the menu
				if (screen == 0) {
					g.drawImage(menu, 0, 0, null);
//					g.drawImage(menu, 0, 0, Camera.getWidth(), Camera.getHeight(), 0, 0, menu.getWidth(null), menu.getHeight(null), null);
				} // if
				
				// show instructions
				if (screen == 1) {
					g.drawImage(instructions, 0, 0, null);
//					g.drawImage(instructions, 0, 0, Camera.getWidth(), Camera.getHeight(), 0, 0, instructions.getWidth(null), instructions.getHeight(null), null);
				} // if
				
				// show credits
				else if (screen == 2) {
					g.drawImage(credits, 0, 0, null);
//					g.drawImage(credits, 0, 0, Camera.getWidth(), Camera.getHeight(), 0, 0, credits.getWidth(null), credits.getHeight(null), null);
				} // else if
				
				// show win screen
				else if (screen == 3) {
					g.drawImage(win, 0, 0, null);
//					g.drawImage(win, 0, 0, Camera.getWidth(), Camera.getHeight(), 0, 0, win.getWidth(null), win.getHeight(null), null);
				} // else if
				
				// show game over screen
				else if (screen == 4) {
					g.drawImage(gameOver, 0, 0, null);
//					g.drawImage(gameOver, 0, 0, Camera.getWidth(), Camera.getHeight(), 0, 0, gameOver.getWidth(null), gameOver.getHeight(null), null);
				} // else if
				
				g.dispose();
				strategy.show();
				continue;
			} // if
			
			Camera.center(player);
			
			// run the game
			
			// not necessary right now
			//ArrayList<Entity> tempEntities = (ArrayList<Entity>) entityArray.clone();
			//ArrayList<Character> tempChars = (ArrayList<Character>) characters.clone();
			
			// for removing attacks
			ArrayList<Attack> tempAttacks = (ArrayList<Attack>) attacks.clone();
			
			// draw the entities
			for (Entity e : entities) {
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
			
			
			// draw player icon
			g.drawImage(icon, 0, 0, null);
			
			// temp
			for (Character c : characters) {
				if (c.animation != null) {
					c.animation.update(delta);
				} // if
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
					entities.add(p);
					player.getMana().decrement(10);
				} // if
				//System.out.println("shot");
			} // if
			
			// check for attacks hitting characters
			for (Attack a : tempAttacks) {
				if(a.collidesWith(characters, g)) {
					removeEntity(a);
				} // if
			} // for
			
			// regenerate stamina
			if ((System.currentTimeMillis() - lastStaminaRegen) > staminaRegenInterval) {
				player.getStamina().increment(10);
				lastStaminaRegen = System.currentTimeMillis();
			} // if
			
			// regenerate mana
			if ((System.currentTimeMillis() - lastManaRegen) > manaRegenInterval) {
				player.getMana().increment(10);
				lastManaRegen = System.currentTimeMillis();
			} // if
			
			// consume stamina if running
			if (isRunning && (leftPressed || upPressed || downPressed || rightPressed)) {
				if (player.getStamina().getValue() <= 0) {
					isRunning = false;
					currentSpeed = walkSpeed;
				} else if ((System.currentTimeMillis() - lastStaminaConsumption) > staminaConsumeInterval) {
					player.getStamina().decrement(10);
					lastStaminaConsumption = System.currentTimeMillis();
				} // if
				
			} // if
			
			
			if (player.getHp().getValue() <= 0) {
				entities.clear();
				gameIsRunning = false;
				screen = 4;
			} // if
			
			if (noEnemies()) {
				entities.clear();
				gameIsRunning = false;
				screen = 3;
			} // if
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
		} // while
		
	} // gameLoop

	private void handlePlayerMovement(long delta) {
		
		if (isTalking) { return; } // what if enemy followed over?
		
		// reset speed
		player.setXVelocity(0);
		player.setYVelocity(0);
		
		// check for user input
		if (upPressed && !downPressed) {
			
			if (leftPressed && !rightPressed) {
				player.setXVelocity( -1 * currentSpeed);
				horizontalDirection = -1;
				verticalDirection = 0;
			
				player.setDirection(Direction.NW);
			} else if (rightPressed && !leftPressed) {
				player.setYVelocity(-1 * currentSpeed);
				horizontalDirection = 0;
				verticalDirection = -1;
				
				player.setDirection(Direction.NE);
			} else {
				player.setXVelocity(-1 * currentSpeed);
				player.setYVelocity(-1 * currentSpeed);
				horizontalDirection = -1;
				verticalDirection = -1;
				
				player.setDirection(Direction.N);
			} // else
			
		} else if (leftPressed && !rightPressed) {
			
			if (downPressed && !upPressed) {
				player.setYVelocity(currentSpeed);
				horizontalDirection = 0;
				verticalDirection = 1;
				
				player.setDirection(Direction.SW);
			} else {
				player.setXVelocity((int)(-1 * currentSpeed * 0.7));
				player.setYVelocity((int)(currentSpeed * 0.7));
				horizontalDirection = -1;
				verticalDirection = 1;
				
				player.setDirection(Direction.W);
			} // else
			
		} else if (downPressed && !upPressed) {
			if (rightPressed && !leftPressed) {
				player.setXVelocity(currentSpeed);
				horizontalDirection = 1;
				verticalDirection = 0;
				
				player.setDirection(Direction.SE);
			} else {
				player.setXVelocity(currentSpeed);
				player.setYVelocity(currentSpeed);
				horizontalDirection = 1;
				verticalDirection = 1;
				
				player.setDirection(Direction.S);
			} // else
			
		} else if (rightPressed && !leftPressed) {
			player.setXVelocity((int)(currentSpeed * 0.7));
			player.setYVelocity((int)(-1 * currentSpeed * 0.7));
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
			
			
			int XDirection = (int) (player.getX() - enemy.getX());
			int YDirection = (int) (player.getY() - enemy.getY());
			
			// probably some enemy direction
			// decide the direction
			enemy.setXVelocity( XDirection);
			enemy.setYVelocity( YDirection);
			
		} // if
		
		// animation
		enemy.updateDirection();
		enemy.setWalkAnimation();
		enemy.animation.start();
			
		// move
		enemy.move(delta);
	} // handleEnemyMovement
	
	private boolean tryToStartDialogue() {
		if (isTalking) { return false; }
		
		for (NPC n : npcs) {
			if (n.withinRange((int) player.x, (int) player.y)) {
				System.out.println("within range");
				dialogue.start(n.getDialogue());
				isTalking = true;
				return true;
			} // if
		} // for
		
		return false;
	} // tryToStartDialogue
	
	public void stopTalking() {
		isTalking = false;
	}
	
	private void handleMenuClick(MouseEvent e) {
		int x = 700;
		int y = 150;
		int buttonWidth = 520;
		int buttonHeight = 210;
		int padding = 50;
		
		// if currently on menu screen
		if (screen == 0) {
			y = 150;
			
			// check if within horizontal bounds of button
			if (!(e.getX() > x && e.getX() < x + buttonWidth)) { return; }
			
			// start game
			if (e.getY() > y && e.getY() < y + buttonHeight) {
				resetGame();
				screen = 5;
			} // if
			
			// instructions screen
			if (e.getY() > y + buttonHeight + padding && e.getY() < y + 2 * buttonHeight + padding) {
				screen = 1;
			} // if
			
			// credits screen
			if (e.getY() > y + 2 * buttonHeight + 2 * padding && e.getY() < y + 3 * buttonHeight + 2 * padding) {
				screen = 2;
			} // if
		} // if
		
		// if currently on instructions or credits
		if (screen == 1 || screen == 2) {
			y = 870;
			x = 750;
			buttonWidth = 420;
			buttonHeight = 180;
			
			// check if within horizontal bounds of button
			if (!(e.getX() > x && e.getX() < x + buttonWidth)) { return; }
			
			// return to menu
			if (e.getY() > y && e.getY() < y + buttonHeight) {
				screen = 0;
			} // if
			
		} // if
		
		// if currently on game lost or won screen
		if (screen == 3 || screen == 4) {
			y = 620;
			
			// check if within horizontal bounds of button
			if (!(e.getX() > x && e.getX() < x + buttonWidth)) { return; }
			
			// return to menu
			if (e.getY() > y && e.getY() < y + buttonHeight) {
				screen = 0;
			} // if
			
		} // if
		
	} // handleMenuClick
	
	private void resetGame() { //?
		
		// reset variables
		upPressed = false;    
		leftPressed = false;  
		downPressed = false;  
		rightPressed = false; 
		shotFired = false;    
		melee = false;       
		
		currentSpeed = walkSpeed;
		isRunning = false;    
		isTalking = false;
		
		lastFire = 0; 
		lastStaminaRegen = 0;
		lastManaRegen = 0;
		lastStaminaConsumption = 0;
		lastDamage = 0;
		
		inventoryVisible = false; 
		
		// clear quests?
		// clear inv?
		
		entities.clear();
		attacks.clear();
		characters.clear();
		npcs.clear(); //?
		
		initEntities();
	}
	
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
		
		return new Attack("images/projectiles/shot_" + shooter.getDirection().getDirection() + ".png", xSpawn * 60, ySpawn * 60, horizontalDirection * projectileSpeed, verticalDirection * projectileSpeed, range, shooter);

	} // spawnProjectile
	
	// remove an entity from the game
	public static void removeEntity(Entity e) {
		entities.remove(e);
		if (e instanceof Attack) {
			attacks.remove(e);
		} else if (e instanceof Character) {
			characters.remove(e);
		} // else if
	} // removeEntity
	
	// handles keyboard input from the user
	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // should we have this?
				System.exit(0);
			} // if
			
			if (screen == 5) {
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
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) { // should we have this?
					if (player.getStamina().getValue() <= 0) { return; }
					isRunning = true;
					currentSpeed = runSpeed;
				} // if
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
			
			if (screen == 5) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					
					// check if npc is in range and start dialogue
					// if just started dialogue, don't update dialogue
					if (tryToStartDialogue()) { return; }
					
					// update dialogue
					if (isTalking) {
						dialogue.update();
					} // if
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
					upPressed = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftPressed = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
					downPressed = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightPressed = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_X) {
					shotFired = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					melee = false;
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_I) {
					inventoryVisible = !inventoryVisible;
					
					if (!inventoryVisible) {
						inv.stopDrag();
					} // if
					
					System.out.println("inventoryVisible: " + inventoryVisible);
				} // if
				
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) { // should we have this?
					isRunning = false;
					currentSpeed = walkSpeed;
				} // if
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
			if (screen <= 4) {
				handleMenuClick(e);
			}
			if (isTalking) {
				dialogue.handleClick(e);
			}
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
	
	
	private boolean noEnemies() {
		for (Character c : characters) {
			if (!c.isPlayer()) {
				return false;
			}
		}
		return true;
	}
	
	public static Font getDidactGothic() {
		return didactGothic28;
	}
} // Game
