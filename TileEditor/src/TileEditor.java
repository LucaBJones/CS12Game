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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TileEditor extends Canvas {
	
	private BufferStrategy strategy;
	private Graphics2D g;

	private final int WINDOW_WIDTH = 750;
	private final int WINDOW_HEIGHT = 750;
	
	private static int[][] map;
	
	private int rows = 3;
	private int columns = 3;
	
	private int xOffset = 350;
	private int yOffset = 300;
	
	private int dragPercentage = 1;
	
	private int currentTileNum = 1;
	
	private boolean settingColumns = false;
	private boolean settingRows = false;
	
	private static ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	public static void main(String[] args) {
		new TileEditor();
		
	} // Game
	
	public TileEditor() {
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
		g = (Graphics2D) strategy.getDrawGraphics();
		updateTiles();
		gameLoop();
	} // Game
	
	private void updateTiles() {
		// initiate tiles
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tiles.add(new Tile(j, i, this));
			} // for
		} // for
	} // initEntities

	private void addRow() {
		rows++;
		for (int i = 0; i < columns; i++) {
			tiles.add(new Tile(i, rows - 1, this));
			System.out.println("new, x: " + i + ", y: " + (rows - 1));
		}
	}
	
	private void addColumn() {
		columns++;
		for (int i = 0; i < rows; i++) {
			tiles.add((i + 1) * columns - 1, new Tile(columns - 1, i, this));
			System.out.println("new, x: " + (columns - 1) + ", y: " + i);
		}
	}

	private void setAllTiles() {
		for (Tile t : tiles) {
			t.setSprite(currentTileNum);
		}
	}
	
	private void setRows(int rowNum) {
		for (int i = 0; i < columns; i++) {
			tiles.get(columns * rowNum + i).setSprite(currentTileNum);
			System.out.println("settingRows: " + (columns * rowNum + i));
		}
	}
	
	private void setColumns(int columnNum) {
		for (int i = 0; i < rows; i++) {
			tiles.get(columnNum + (i * columns)).setSprite(currentTileNum);
		}
	}
	
	private void gameLoop() {
		ArrayList<Tile> tempTiles;
		
		while (true) {

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.gray);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			
			tempTiles = (ArrayList<Tile>) tiles.clone();
			for (Tile t : tempTiles) {
				t.draw(g);
			} // for
			
			g.setColor(Color.white);
			g.drawString("Current Tile: " + currentTileNum, 20, 30);
			g.drawString("Rows: " + rows, 20, 50);
			g.drawString("Columns: " + columns, 20, 70);
			
			
			g.drawString("r - add rows", 20, 110);
			g.drawString("c - add columns", 20, 130);
			g.drawString("a - fill all", 20, 150);
			g.drawString("q - fill columns", 20, 170);
			g.drawString("w - fill rows", 20, 190);
			
			g.drawString("Number keys to change Tiles", 20, 230);
			g.drawString("Click to set tiles", 20, 250);
			g.drawString("Middle Mouse Button to Drag", 20, 270);
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();
			
		} // while
	} // gameLoop

	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

		} // keyPressed

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_R) {
				addRow();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_C) {
				addColumn();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_A) {
				setAllTiles();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_Q) {
				settingColumns = !settingColumns;
				settingRows = false; // should this be here? could be cross-shaped
			}
			
			if (e.getKeyCode() == KeyEvent.VK_W) {
				settingRows = !settingRows;
				settingColumns = false; // should this be here? could be cross-shaped
			}
			
			if (e.getKeyCode() == KeyEvent.VK_1) { // move to keyTyped?
				currentTileNum = 1;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_2) {
				currentTileNum = 2;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_3) {
				currentTileNum = 3;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_4) {
				currentTileNum = 4;
			}
			
			
		} // keyReleased

		public void keyTyped(KeyEvent e) {
	
		} // keyTyped

	} // class KeyInputHandler
	
	private class MouseMotion implements MouseMotionListener, MouseListener {

		private boolean isDragging = false;
		private Point dragStart = new Point();
		
		@Override
		public void mouseDragged(MouseEvent e) {
			//System.out.println("MOUSE_DRAGGED: " + " (" + e.getX() + "," + e.getY() + ")" + " detected on " + e.getComponent().getClass().getName());
			if (SwingUtilities.isMiddleMouseButton(e)) {
				System.out.println("middle");
				System.out.println("e: " + e.getX() + ", " + e.getY());
				
				if (isDragging) {
					int dragX = e.getX() - dragStart.x;
					int dragY = e.getY() - dragStart.y;
					
					xOffset += dragX * (1.0 / dragPercentage);
					yOffset += dragY * (1.0 / dragPercentage);
					
					System.out.println("dragX: " + dragX);
					System.out.println("xOffset: " + xOffset + ", yOffset: " + yOffset);
					

					dragStart.x = e.getX();
					dragStart.y = e.getY();
					return;
				} // if
				
				isDragging = true;
				dragStart.x = e.getX();
				dragStart.y = e.getY();
			} // if
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//System.out.println("MOUSE_MOVED: " + " (" + e.getX() + "," + e.getY() + ")" + " detected on "
					//+ e.getComponent().getClass().getName());
		
		} // mouseMoved

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("clicked");
			System.out.println("x: " + e.getX() + ", y: " + e.getY());
			
			Point cart = toCart(e.getX() - xOffset, e.getY() - yOffset);
			
			if (cart.x < 0 || cart.y < 0) {
				System.out.println("out of bounds...");
				return;
			}
			
			int tileX = cart.x / Tile.TILE_LENGTH;
			int tileY = cart.y / Tile.TILE_LENGTH;
			
			if (settingColumns) {
				setColumns(tileX);
				return;
			}
			
			if (settingRows) {
				setRows(tileY);
				return;
			}
			
			System.out.println("cart, x: " + cart.x + ", y: " + cart.y);
			System.out.println("tile, x: " + tileX + ", y: " + tileY);
		
			if (tileX < 0 || tileY < 0 || tileX > columns - 1 || tileY > rows - 1) {
				System.out.println("out of bounds");
				System.out.println("tileX: " + tileX + ", columns: " + columns);
				System.out.println("tileY: " + tileY + ", rows: " + rows);
				return;
			}
			tiles.get(tileY * columns + tileX).setSprite(currentTileNum);
			
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
			isDragging = false;
		}
	} // MouseMotion
	
	public Point toCart(int isoX, int isoY) {
		int cartX = (2 * isoY + isoX) / 2;
		int cartY = (2 * isoY - isoX) / 2;
		return new Point(cartX, cartY);
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}

	private void save() {
//		BufferedWriter bw = new BufferedWriter(new OutputStream());
	}
	
	private void load() {
		String fileName = "maps/map.txt"; // temp
		InputStream input = TileEditor.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
	}
	
} // Game
