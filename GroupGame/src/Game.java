
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Game extends Canvas {
	
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 500;
	
	private BufferStrategy strategy;

	private boolean gameIsRunning = false;
	
	private static int[][] map = {
        	{2, 2, 2, 2},
        	{2, 2, 2, 2},
        	{2, 1, 2, 2},
        	{2, 2, 2, 2}
		};
	
	private static Tile[][] tileMap = new Tile[map.length][map[0].length]; // array or arrayList?
	
	private Player player;
	
	private boolean upPressed = false;
	private boolean leftPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	
	private static int speed = 100;
	private static int xDiagonal = (int) (2 * (double) speed / (Math.pow(5, 0.5)));
	private static int yDiagonal = (int) ((double) speed / (Math.pow(5, 0.5)));
	
	public static void main(String[] args) {
		new Game();
		
	} // Game
	
	public Game() {
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setResizable(false); // can change
		frame.setVisible(true);
		
		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());
		requestFocus();
		
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
				System.out.println(tileMap[i][j].getIsPassable() ? "true" : "false");
			} // for
		} // for
		
		player = new Player("images/char_sw.png", 1, 1, 0, 0);
		
	} // initEntities
	
	private void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		while (gameIsRunning) {
			
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// draw graphics
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			
			// draw tiles
			for (int i = 0; i < tileMap.length; i++) {
				for (int j = 0; j < tileMap.length; j++) {
					tileMap[i][j].draw(g);
				} // for
			} // for
			
			player.draw(g);
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
			// movement
			player.setXVelocity(0);
			player.setYVelocity(0);
			
			if (upPressed && !downPressed) {
				
				if (leftPressed && !rightPressed) {
					player.setXVelocity(-1 * speed);
					player.setSprite("images/char_nw.png");
				} else if (rightPressed && !leftPressed) {
					player.setYVelocity(-1 * speed);
					player.setSprite("images/char_ne.png");
				} else {
					player.setXVelocity(-1 * speed);
					player.setYVelocity(-1 * speed);
					player.setSprite("images/char_n.png");
				}
				
			}
			else if (leftPressed && !rightPressed) {
				
				if (downPressed && !upPressed) {
					player.setYVelocity(speed);
					player.setSprite("images/char_sw.png");
				} else {
					player.setXVelocity(-1 * speed);
					player.setYVelocity(speed);
					player.setSprite("images/char_w.png");
				}
				
			}
			else if (downPressed && !upPressed) {
				if (rightPressed && !leftPressed) {
					player.setXVelocity(speed);
					player.setSprite("images/char_se.png");
				} else {
					player.setXVelocity(speed);
					player.setYVelocity(speed);
					player.setSprite("images/char_s.png");
				}
				
			}
			else if (rightPressed && !leftPressed) {
				player.setXVelocity(speed);
				player.setYVelocity(-1 * speed);
				player.setSprite("images/char_e.png");
			}
			
			player.move(delta);
			
		} // while
	} // gameLoop

	
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
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.getHp().decrement(10);
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
			
		} // keyReleased

		// do we need this?
		public void keyTyped(KeyEvent e) {
	
		} // keyTyped

	} // class KeyInputHandler
	
	public static Tile[][] getTiles() {
		return tileMap;
	}
	
} // Game
