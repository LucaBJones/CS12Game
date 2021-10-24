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
	
	private Camera camera = new Camera();
	
	private BufferStrategy strategy;

	private boolean gameIsRunning = false;
	
	private static int[][] map = {
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 1, 1, 2, 2, 2, 2, 2, 2},
        	{2, 2, 1, 1, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
		};
	
	private static Tile[][] tileMap = new Tile[map.length][map[0].length]; // array or arrayList?
	private static ArrayList<Entity> entityArray = new ArrayList<Entity>();
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	private Player player;
	private Inventory inv; // should this be in player?
	
	private Projectile p;
	
	private boolean upPressed = false;
	private boolean leftPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	private boolean shotFired = false;
	private boolean inventoryVisible = true;
	
	private int horizontalDirection = 0; 	// stores direction for projectiles
	private int verticalDirection = 1;		// values: -1, 0, 1 
	private int projectileSpeed = 200; 		// may want to change this later in the game as power up or something
	
	private long lastFire = 0; // time last shot fired
    private long firingInterval = 300; // interval between shots (ms)
	
	private static int speed = 100;
	
	public static void main(String[] args) {
		new Game();
		
	} // Game
	
	public Game() {
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
		setBounds(0, 0, camera.getWidth(), camera.getHeight());
		
		panel.setPreferredSize(new Dimension(camera.getWidth(), camera.getHeight()));
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
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", j, i, b);
				entityArray.add(tileMap[i][j]);
			} // for
		} // for
		
		player = new Player("images/char_sw.png", 0, 0, 0, 0);
		inv = new Inventory(3); // size of inventory
		
		entityArray.add(player);
		for (Point p : player.getCorners()) {
			System.out.println(p);
		} // for
		
		p = spawnProjectile();
		projectiles.add(p);
		entityArray.add(p);
	} // initEntities/this
	
	private void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		while (gameIsRunning) {
			
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// draw graphics
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, camera.getWidth(), camera.getHeight());
			
			camera.center(player);
			
			for (Entity e : entityArray) {
				e.draw(g, camera);
			} // for
			
			if (inventoryVisible) {
				inv.draw(g);
			} // if
			
			// moves projectiles 
			for (Projectile p : projectiles) {
				p.move(delta);
			} // for
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
			// movement
			handlePlayerMovement(delta);
			
			if (shotFired) {
				Projectile p = spawnProjectile();
				if(p != null) {
					projectiles.add(p);
					entityArray.add(p);
				} // if
			} // if
			
		} // while
	} // gameLoop

	private void handlePlayerMovement(long delta) {
		player.setXVelocity(0);
		player.setYVelocity(0);
		
		if (upPressed && !downPressed) {
			
			if (leftPressed && !rightPressed) {
				player.setXVelocity(-1 * speed);
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
				player.setXVelocity(-1 * speed);
				player.setYVelocity(speed);
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
			} else {
				player.setXVelocity(speed);
				player.setYVelocity(speed);
				player.setSprite("images/char_s.png");
				horizontalDirection = 1;
				verticalDirection = 1;
			} // else
			
		} else if (rightPressed && !leftPressed) {
			player.setXVelocity(speed);
			player.setYVelocity(-1 * speed);
			player.setSprite("images/char_e.png");
			horizontalDirection = 1;
			verticalDirection = -1;
		} // else if
		
		player.move(delta);
	} // handlePlayerMovement
	
	// 
	private Projectile spawnProjectile() { 
		// check that we've waited long enough to fire
        if ((System.currentTimeMillis() - lastFire) < firingInterval){
          return null;
        } // if
        lastFire = System.currentTimeMillis();
		System.out.println("spawn projectile");//
		return new Projectile("images/sprite1.png", (int) player.getX(), (int) player.getY(), horizontalDirection * projectileSpeed, verticalDirection * projectileSpeed);
	} // spawnProjectile
	
	
	public static void removeEntity(Entity e) {
		entityArray.remove(e);
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
			
			if (e.getKeyCode() == KeyEvent.VK_I) {
				System.out.println("Pressed: i " + inventoryVisible);
				if(inventoryVisible) {
					inventoryVisible = false;
				} else {
					inventoryVisible = true;
				} // else
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_X) {
				shotFired = true;
				//System.out.println("Pressed: space");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} // if
			
//			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
//				player.().decrement(10);
//			}

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

			if (SwingUtilities.isLeftMouseButton(e)) {
				//System.out.println("left button");
				inv.handleDrag(e);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//System.out.println("MOUSE_MOVED: " + " (" + e.getX() + "," + e.getY() + ")" + " detected on "
					//+ e.getComponent().getClass().getName());
		}

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
			inv.stopDrag(e);
		}
	} // MouseMotion
	
	public static Tile[][] getTiles() {
		return tileMap;
	}
	
	
} // Game
