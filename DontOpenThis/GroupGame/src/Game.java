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
	private boolean inventoryVisible = false; 	// false bc otherwise hovering over inv creates nullPointer exceptions... 
												// (a fix would be to add the mouselistener after everything is initialized)
	private static Tooltip tooltip;
	
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
	private int projectileSpeed = 300; 	// may want to change this later in the game as power up or something
	
	private long lastFire = 0; // time last shot fired
    private long firingInterval = 300; // interval between shots (ms)
    
    private long lastRegen = 0; // time stamina was last regenerated
    private long regenInterval = 500; // interval between stamina regen
	
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
		
		// add key listener
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
		
		// initialize entities
		initEntities();
		
		// start the game
		gameIsRunning = true;
		gameLoop();
		
	} // Game
	
	// initialize entities
	private void initEntities() {
		
		// initialize tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				boolean b = (map[i][j] > 1);
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", 60 * j, 60 *i, b);
				entityArray.add(tileMap[i][j]);
			} // for
		} // for
		
		// create characters
		player = new Character("images/char_sw.png", 0, 0, 0, 0, true);
		Camera.center(player);
		Character enemy = new Character("images/char_sw.png", 180, 180, 0, 0, false);
		entityArray.add(enemy);
		characters.add(enemy);
		entityArray.add(player);
		characters.add(player);
		
		
		// create inventory and tooltip
		inv = new Inventory(3); // size of inventory
		tooltip = new Tooltip();
		
		// temp
//		anim = new Animation("walk_s");
//		anim.start();
		
	} // initEntities
	
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
			
			for (Attack a : attacks) {
				a.drawHitbox(g);
			} // for
			
			// draw the inventory
			if (inventoryVisible) {
				inv.draw(g);
			} // if
			
			// temp
			if (player.animation != null) {
				player.animation.update(delta);
			}
			
			// moves projectiles 
			for (Attack a : tempAttacks) {
				a.move(delta);
			} // for
			
			// move the player
			handlePlayerMovement(delta);
			
			// long range attack
			if (shotFired && !melee && player.getMana().getValue() > 0) {
				Attack p = startAttack(1000, player);
				if (p != null) {
					attacks.add(p);
					entityArray.add(p);
					player.getMana().decrement(10);
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
				} // if
			} // if
			
			//System.out.println(player.getHitBox());
			
			// check for attacks hitting characters
			for (Attack a : tempAttacks) {
				if(a.collidesWith(characters, g)) {
					removeEntity(a);
					System.out.println("remove attack");
				} // if
			} // for
			
			// regenerate stamina
			if ((System.currentTimeMillis() - lastRegen) > regenInterval) {
				player.getStamina().increment(1);
				lastRegen = System.currentTimeMillis();
			} // if
			
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
		if(enemy.getScreenPosX() > 0 && enemy.getScreenPosX() < Camera.getWidth() && enemy.getScreenPosY() > 0 && enemy.getScreenPosY() < Camera.getHeight()) {
			
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
			
			do {
				
				// current = node in open with min f cost
				Node current = getMinFCost(open);
				
				// set the neighbours of this node
				for(int i = -1; i < 2; i ++) {
					for(int j = -1; j < 2; j ++) {
						current.addNeighbour(new Node(current.getY() + i, current.getX() + j, end, current));
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
			
			Node goal = getMinFCost(start.getNeighbours());
			System.out.println(getMinFCost(start.getNeighbours()).getX() + " | " + getMinFCost(start.getNeighbours()).getX());
			int XDirection = goal.getX() - start.getX();
			int YDirection = goal.getY() - start.getY();
			
			System.out.println(100 * XDirection);
			System.out.println(100 * YDirection);
			
			// probably some enemy direction
			// decide the direction
			enemy.setXVelocity(100 * XDirection);
			enemy.setYVelocity(100 * YDirection);
			enemy.setSprite("images/char_s.png");
			
		} // if
		
		// animation
		enemy.setWalkAnimation();
		enemy.animation.start();
			
		// move
		enemy.move(delta);
	} // handleEnemyMovement
	
	private Node getMinFCost(ArrayList<Node> open) {
		Node min = open.get(0);
		int minValue = min.getFCost();
		
		for(int i = 1; i < open.size(); i ++) {
			if(open.get(i).getFCost() < minValue) {
				min = open.get(i);
				minValue = min.getFCost();
			} // if
		} // for
		
		return min;
	}
	
	// Node class for path finding, though these nodes are points not squares
	private class Node {
		private int x;		// tile x
		private int y;		// tile y
		private int gCost;
		private int hCost;
		private int fCost;
		private ArrayList<Node> neighbours = new ArrayList<Node>();
		private Node parent;

		public Node(){
			gCost = 0;
			hCost = 0;
			fCost = 0;
			x = 0;
			y = 0;
		} 
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
			gCost = 0;
			hCost = 0;
			fCost = 0;
		}
		
		public Node(int x, int y, Node end) {
			this.x = x;
			this.y = y;
			gCost = 0;
			hCost = (int) Math.pow((Math.pow(x - end.getX(), 2) + Math.pow(y - end.getY(), 2)), 0.5);
			fCost = gCost + hCost;
		}
		
		public Node(int x, int y, Node end, Node parent) {
			this.x = x;
			this.y = y;
			this.parent = parent;
			gCost = parent.getGCost() + (int) Math.pow((Math.pow(x - parent.getX(), 2) + Math.pow(y - parent.getY(), 2)), 0.5);
			hCost = parent.getHCost() + (int) Math.pow((Math.pow(x - end.getX(), 2) + Math.pow(y - end.getY(), 2)), 0.5);
			fCost = gCost + hCost;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public void setGCost(int g) {
			gCost = g;
		}
		
		public int getGCost() {
			return gCost;
		}
		
		public void setHCost(int h) {
			hCost = h;
		}
		
		public int getHCost() {
			return hCost;
		}
		
		public void setFCost(int f) {
			fCost = f;
		}
		
		public int getFCost() {
			return fCost;
		}
		
		public void addNeighbour(Node n) {
			neighbours.add(n);
		}
		
		public ArrayList<Node> getNeighbours() {
			return neighbours;
		}
		
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		
	} // Node
	
	// move?
	// create an attack
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
		//System.out.println("spawn projectile");
		
		diagonal = Math.pow(Math.pow(horizontalDirection, 2) + Math.pow(verticalDirection, 2), -0.5);
		xSpawn = (int) (player.getX() + INIT_DIST * horizontalDirection *  diagonal);
		ySpawn = (int) (player.getY() - Entity.TILE_LENGTH + INIT_DIST * verticalDirection * diagonal);		
		
		//System.out.println(xSpawn);
		//System.out.println( ySpawn);
		return new Attack("images/sprite1.png", xSpawn * 60, ySpawn * 60, horizontalDirection * projectileSpeed, verticalDirection * projectileSpeed, range, shooter);

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