import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 500;
	
	private int xOffset = 300;
	private int yOffset = 50;
	
	private BufferStrategy strategy;
	
	private static Game game;
	private boolean gameIsRunning = false;
	
	private ArrayList<Tile> tileMap = new ArrayList<Tile>(); // array or arrayList?
	
	static int[][] map = {
        	{3,3,3},
        	{1,1,1},
        	{2,2,2}
		};
	
	private SpriteStore store = SpriteStore.get(); // should this be a class variable?
	
	
	public static void main(String[] args) {
		game = new Game();
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
			for (int j = 0; j < map[0].length; j++) {
				tileMap.add(new Tile("images/tile" + map[i][j] + ".png", i, j, this));
				System.out.println("i: " + i + ", j: " + j + "| x: " + (i * 120) + ", y: " + (j * 60));
			}
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
	
	// returns the isometric coordinates for the cartesian coordinates passed in
	public Point toIso(int x, int y) {
		int isoX = x - y;
		int isoY = (x + y) / 2;
		return new Point(isoX, isoY);
	} // toIso
	
} // Game
