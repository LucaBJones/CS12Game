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
	
	private BufferStrategy strategy;

	private boolean gameIsRunning = false;
	
	private ArrayList<Tile> tileMap = new ArrayList<Tile>(); // array or arrayList?
	
	static int[][] map = {
        	{3,3,3,3},
        	{1,1,1,4},
        	{2,2,2}
		};
	
	public static void main(String[] args) {
		new Game();
	} // Game
	
	public Game() {
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		panel.setBackground(Color.gray);
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
				tileMap.add(new Tile("images/tile" + map[i][j] + ".png", i, j, this));
			} // for
		} // for
		
	} // initEntities
	
	private void gameLoop() {
		while (gameIsRunning) {
			
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			
			// draw tiles
			for (int i = 0; i < tileMap.size(); i++) {
				tileMap.get(i).draw(g);
			} // for
			
			
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
		} // while
	} // gameLoop

	
	// handles keyboard input from the user
	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				System.out.println("Pressed: w or up");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				System.out.println("Pressed: a or left");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				System.out.println("Pressed: s or down");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				System.out.println("Pressed: d or right");
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				System.out.println("Released: w or up");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
				System.out.println("Released: a or left");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				System.out.println("Released: s or down");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				System.out.println("Released: d or right");
			} // if
			
		} // keyReleased

		// do we need this?
		public void keyTyped(KeyEvent e) {
	
		} // keyTyped

	} // class KeyInputHandler
	
} // Game
