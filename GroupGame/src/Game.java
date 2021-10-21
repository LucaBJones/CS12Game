import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 500;
	
	private static JPanel statusPanel;
	
	private BufferStrategy strategy;

	private boolean gameIsRunning = false;
	
	private static int[][] map = {
        	{1, 1, 1, 1},
        	{1, 1, 1, 1},
        	{1, 1, 1, 1}
		};
	
	private Tile[][] tileMap = new Tile[map.length][map[0].length]; // array or arrayList?
	
	private Player player;
	
	private boolean wPressed = false;
	private boolean aPressed = false;
	private boolean sPressed = false;
	private boolean dPressed = false;
	
	public static void main(String[] args) {
		new Game();
	} // Game
	
	public Game() {
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		statusPanel = new JPanel();
		
		statusPanel.setBackground(Color.red);
		statusPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // add to frame
		
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		//frame.add(statusPanel);
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
				tileMap[i][j] = new Tile("images/tile" + map[i][j] + ".png", i, j);
			} // for
		} // for
		
		player = new Player("images/char_sw.png", 0, 0, 0, 0);
		
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
			
			// game logic?
			player.setXVelocity(0);
			player.setYVelocity(0);
			
			if (wPressed && !sPressed) {
				player.setYVelocity(-50);
				if (aPressed && !dPressed) {
					player.setXVelocity(-100);
				} else if (dPressed && !aPressed) {
					player.setXVelocity(100);
				}
			}
			else if (aPressed && !dPressed) {
				player.setXVelocity(-50);
				if (sPressed && !wPressed) {
					player.setXVelocity(-100);
					player.setYVelocity(50);
				}
			}
			else if (sPressed && !wPressed) {
				player.setYVelocity(50);
				if (dPressed && !aPressed) {
					player.setXVelocity(100);
				}
			}
			else if (dPressed && !aPressed) {
				player.setXVelocity(50);
			}
			
			player.move(delta);
			
		} // while
	} // gameLoop

	
	// handles keyboard input from the user
	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				System.out.println("Pressed: w or up");
				wPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				System.out.println("Pressed: a or left");
				aPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				System.out.println("Pressed: s or down");
				sPressed = true;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				System.out.println("Pressed: d or right");
				dPressed = true;
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				System.out.println("Released: w or up");
				wPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				System.out.println("Released: a or left");
				aPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				System.out.println("Released: s or down");
				sPressed = false;
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				System.out.println("Released: d or right");
				dPressed = false;
			} // if
			
		} // keyReleased

		// do we need this?
		public void keyTyped(KeyEvent e) {
	
		} // keyTyped

	} // class KeyInputHandler
	
	public static JPanel getStatusPanel() {
		return statusPanel;
	}
	
} // Game
