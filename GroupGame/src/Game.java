import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

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
	private Inventory inv; // should this be in player?
	private static Tooltip tooltip;
	
	private boolean upPressed = false;
	private boolean leftPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	private boolean shotFired = false;
	private boolean inventoryVisible = false; 	// false bc otherwise hovering over inv creates nullPointer exceptions... 
												// (a fix would be to add the mouselistener after everything is initialized)
	private boolean melee = false;
	
	private int horizontalDirection = 0; 	// stores direction for projectiles // we could use the direction enum for this
	private int verticalDirection = 1;		// values: -1, 0, 1 
	private int projectileSpeed = 1000; 	// may want to change this later in the game as power up or something
	
	private long lastFire = 0; // time last shot fired
    private long firingInterval = 300; // interval between shots (ms)
    
    private long lastRegen = 0; // time stamina was last regenerated
    private long regenInterval = 500; // interval between stamina regen
	
	private static int speed = 300;
	
	public static void main(String[] args) {
		new Game();
		
	} // Game
	
	public Game() {
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
		
		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());
		requestFocus();
		
		// add mouse listeners
		MouseMotion mouseMotion = new MouseMotion();
		addMouseMotionListener(mouseMotion);
		addMouseListener(mouseMotion);
		
		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);
		
		// initializes Entities
		initEntities();
		
		gameIsRunning = true; // move?
		gameLoop();
		
	} // Game
	
	private void initEntities() {
		// initiate tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				boolean b = (map[i][j] > 1);
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", 60 * j, 60 *i, b);
				entityArray.add(tileMap[i][j]);
			} // for
		} // for
		
		player = new Character("images/char_sw.png", 0, 0, 0, 0, true);
		Character enemy = new Character("images/char_sw.png", 120, 120, 0, 0, false);
		entityArray.add(enemy);
		characters.add(enemy);
		
		inv = new Inventory(3); // size of inventory
		tooltip = new Tooltip();
		
		entityArray.add(player);
		characters.add(player);
		for (Point p : player.getCorners()) {
			System.out.println(p);
		} // for
		
		
		// temp
//		anim = new Animation("walk_s");
//		anim.start();
		
		
	} // initEntities
	
	private void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		while (gameIsRunning) {
			
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// draw graphics
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, Camera.getWidth(), Camera.getHeight());
			
			Camera.center(player);
			
			ArrayList<Entity> tempEntities = (ArrayList<Entity>) entityArray.clone();
			ArrayList<Character> tempChars = (ArrayList<Character>) characters.clone();

			ArrayList<Attack> tempAttacks = (ArrayList<Attack>) attacks.clone();
			
			for (Entity e : tempEntities) {
				e.draw(g);
				
			} // for
			
			if (inventoryVisible) {
				inv.draw(g);
			} // if
			
			
			// temp
			if (player.animation != null) {
				player.animation.update(delta);
			}
			
			
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
			// moves projectiles 
			for (Attack a : tempAttacks) {
				a.move(delta);
			} // for
			
			// move the player
			handlePlayerMovement(delta);
			
			// range attack
			if (shotFired && !melee && player.getMana().getValue() > 0) {
				Attack p = startAttack(1000, player);
				if (p != null) {
					attacks.add(p);
					entityArray.add(p);
					player.getMana().decrement(10);
				} // if
			} // if
			
			// melee attack
			if (melee && !shotFired && player.getStamina().getValue() > 10) {
				Attack m = startAttack(100, player);
				if (m != null) {
					attacks.add(m);
					entityArray.add(m);
					player.getStamina().decrement(10);
				} // if
			} // if
			
			// check for collision with attacks
			for (Attack a : tempAttacks) {
				if(a.collidesWith(characters, g)) {
					removeEntity(a);
					System.out.println("remove attack");
				} // if
			} // for
			
			// delete characters if their hp is empty
//			for (Character c : characters) {
//				if (c.getHp().getValue() <= 0) {
//					removeEntity(c);
//				} // if
//			} // for
			
			// regenerate stamina
			if ((System.currentTimeMillis() - lastRegen) > regenInterval) {
				player.getStamina().increment(1);
				lastRegen = System.currentTimeMillis();
			} // if
			
			g.setColor(Color.BLACK);
			g.fillRect(50, 50, 10, 10);
			
			
		} // while
		
		
	} // gameLoop

	private void handlePlayerMovement(long delta) {
		player.setXVelocity(0);
		player.setYVelocity(0);
		
		if (upPressed && !downPressed) {
			
			if (leftPressed && !rightPressed) {
				player.setXVelocity( -1 * speed);
				player.setSprite("images/char_nw.png");
				horizontalDirection = -1;
				verticalDirection = 0;
			
			} else if (rightPressed && !leftPressed) {
				player.setYVelocity(-1 * speed);
				player.setSprite("images/char_ne.png");
				horizontalDirection = 0;
				verticalDirection = -1;
			} else {
				player.setXVelocity(-1 * speed);
				player.setYVelocity(-1 * speed);
				player.setSprite("images/char_n.png");
				horizontalDirection = -1;
				verticalDirection = -1;
			} // else
			
		} else if (leftPressed && !rightPressed) {
			
			if (downPressed && !upPressed) {
				player.setYVelocity(speed);
				player.setSprite("images/char_sw.png");
				horizontalDirection = 0;
				verticalDirection = 1;
			} else {
				player.setXVelocity((int)(-1 * speed * 0.7));
				player.setYVelocity((int)(speed * 0.7));
				player.setSprite("images/char_w.png");
				horizontalDirection = -1;
				verticalDirection = 1;
			} // else
			
		} else if (downPressed && !upPressed) {
			if (rightPressed && !leftPressed) {
				player.setXVelocity(speed);
				player.setSprite("images/char_se.png");
				horizontalDirection = 1;
				verticalDirection = 0;
				
				player.setDirection(Direction.SE);
			} else {
				player.setXVelocity(speed);
				player.setYVelocity(speed);
				player.setSprite("images/char_s.png");
				horizontalDirection = 1;
				verticalDirection = 1;
				
				player.setDirection(Direction.S);
			} // else
			
		} else if (rightPressed && !leftPressed) {
			player.setXVelocity((int)(speed * 0.7));
			player.setYVelocity((int)(-1 * speed * 0.7));
			player.setSprite("images/char_e.png");
			horizontalDirection = 1;
			verticalDirection = -1;
		} // else if
		

		player.setWalkAnimation();
		player.animation.start();
		player.move(delta);
	} // handlePlayerMovement
	
	// move?
	private Attack startAttack(int range, Character shooter) { 
		int xSpawn = 0;				// just to make things
		int ySpawn = 0;				// nice and not messy
		double diagonal = 0;		// used to make diagonal shots come from the same place
		final int INIT_DIST = 30;	// distance from player at spawn
		
		// check that we've waited long enough to fire
        if ((System.currentTimeMillis() - lastFire) < firingInterval){
          return null;
        } // if
        lastFire = System.currentTimeMillis();
		System.out.println("spawn projectile");//
		
		diagonal = Math.pow(Math.pow(horizontalDirection, 2) + Math.pow(verticalDirection, 2), -0.5);
		xSpawn = (int) (player.getX() + INIT_DIST * horizontalDirection *  diagonal);
		ySpawn = (int) (player.getY() - 30 + INIT_DIST * verticalDirection * diagonal);		
		
		return new Attack("images/sprite1.png", xSpawn * 60, ySpawn * 60, horizontalDirection * projectileSpeed, verticalDirection * projectileSpeed, range, shooter);

	} // spawnProjectile
	
	
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
				System.out.println("inventoryVisible: " + inventoryVisible);
			} // if
			
		} // keyReleased

		// do we need this?
		public void keyTyped(KeyEvent e) {
	
		} // keyTyped

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
			//System.out.println("clicked");
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
	
} // Game

