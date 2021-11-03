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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
	private int currentLayer = 0;

	private boolean settingColumns = false;
	private boolean settingRows = false;
	
	private boolean changingTile = false;
	private String currentText = "";
	
	private boolean copyingTile = false;
	
	private static ArrayList<Tile> tiles = new ArrayList<Tile>();

	private static Scanner in;
	
	public static void main(String[] args) {
		in = new Scanner(System.in);
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
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen

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
			t.setSprite(currentTileNum, currentLayer);
		}
	}

	private void setRows(int rowNum) {
		for (int i = 0; i < columns; i++) {
			tiles.get(columns * rowNum + i).setSprite(currentTileNum, currentLayer);
		}
	}

	private void setColumns(int columnNum) {
		for (int i = 0; i < rows; i++) {
			tiles.get(columnNum + (i * columns)).setSprite(currentTileNum, currentLayer);
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
			g.drawString("Current Layer: " + currentLayer + " (e to toggle)", 20, 50);
			g.drawString("Rows: " + rows, 20, 70);
			g.drawString("Columns: " + columns, 20, 90);
			
			g.drawString("r - add rows", 20, 130);
			g.drawString("c - add columns", 20, 150);
			
			g.drawString("a - paint all", 20, 170);
			g.drawString("Painting Columns: " + settingColumns + " (q to toggle)", 20, 190);
			g.drawString("Painting Rows: " + settingRows + " (w to toggle)", 20, 210);

			g.drawString("Click/Drag to set tiles", 20, 270);
			g.drawString("Middle Mouse Button to Drag", 20, 290);
			g.drawString("Hold alt and click to copy tile", 20, 310);

			g.drawString("s - save to map.txt", 20, 340);
			
			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

		} // while
	} // gameLoop

	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_R) {
				addRow();
			} // if

			if (e.getKeyCode() == KeyEvent.VK_C) {
				addColumn();
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (changingTile) { return; }
				changingTile = true;
				System.out.print("Type in tile num: ");
			} // if
			
			if (e.getKeyCode() == KeyEvent.VK_ALT) {
				copyingTile = true;
			}

		} // keyPressed

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_A) {
				setAllTiles();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_ALT) {
				copyingTile = false;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (!changingTile) { return; }
				
				currentTileNum = Integer.parseInt(currentText);
				System.out.println();
				System.out.println("currentTile: " + currentTileNum);
				changingTile = false;
				
				currentText = "";
			}

			if (e.getKeyCode() == KeyEvent.VK_S) {
				save();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_E) {
				currentLayer = (currentLayer == 0) ? 1 : 0;
				
			}

			if (e.getKeyCode() == KeyEvent.VK_Q) {
				settingColumns = !settingColumns;
				settingRows = false; // should this be here? could be cross-shaped
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				settingRows = !settingRows;
				settingColumns = false; // should this be here? could be cross-shaped
			}

		} // keyReleased

		public void keyTyped(KeyEvent e) {
			if (changingTile) {
				if ((e.getKeyChar() + "").toString().matches("[0-9]") || (e.getKeyChar() + "").toString().matches("-")) {
					currentText += "" + e.getKeyChar();
					System.out.print(e.getKeyChar());
				}	
			}
			
			
		} // keyTyped

	} // class KeyInputHandler

	private class MouseMotion implements MouseMotionListener, MouseListener {

		private boolean isDragging = false;
		private Point dragStart = new Point();

		@Override
		public void mouseDragged(MouseEvent e) {

			// drag screen
			if (SwingUtilities.isMiddleMouseButton(e)) {

				if (isDragging) {
					int dragX = e.getX() - dragStart.x;
					int dragY = e.getY() - dragStart.y;

					xOffset += dragX * (1.0 / dragPercentage);
					yOffset += dragY * (1.0 / dragPercentage);

					dragStart.x = e.getX();
					dragStart.y = e.getY();
					return;
				} // if

				isDragging = true;
				dragStart.x = e.getX();
				dragStart.y = e.getY();
			} // if

			// "paint" tiles
			if (SwingUtilities.isLeftMouseButton(e)) {
				// get cartesian coordinates of mouse
				Point cart = toCart(e.getX() - xOffset, e.getY() - yOffset);

				// return if mouse was not on map
				if (cart.x < 0 || cart.y < 0) {
					return;
				} // if

				// get tile coordinates of mouse click
				int tileX = cart.x / Tile.TILE_LENGTH;
				int tileY = cart.y / Tile.TILE_LENGTH;

				// check if outside of map
				if (tileX > columns - 1 || tileY > rows - 1) {
					return;
				} // if

				// "paint" columns
				if (settingColumns) {
					setColumns(tileX);
					return;
				} // if

				// "paint" rows
				if (settingRows) {
					setRows(tileY);
					return;
				} // if

				// set sprite of single tile
				tiles.get(tileY * columns + tileX).setSprite(currentTileNum, currentLayer);
			} // if

		} // mouseDragged

		@Override
		public void mouseMoved(MouseEvent e) {
			// System.out.println("MOUSE_MOVED: " + " (" + e.getX() + "," + e.getY() + ")" +
			// " detected on "
			// + e.getComponent().getClass().getName());

		} // mouseMoved

		@Override
		public void mouseClicked(MouseEvent e) {

			// get cartesian coordinates of mouse
			Point cart = toCart(e.getX() - xOffset, e.getY() - yOffset);

			// return if mouse was not on map
			if (cart.x < 0 || cart.y < 0) {
				return;
			} // if

			// get tile coordinates of mouse click
			int tileX = cart.x / Tile.TILE_LENGTH;
			int tileY = cart.y / Tile.TILE_LENGTH;

			// check if outside of map
			if (tileX > columns - 1 || tileY > rows - 1) {
				return;
			} // if
			
			if (copyingTile) {
				currentTileNum = tiles.get(tileY * columns + tileX).getSpriteNum();
				System.out.println("Copied! currentTile: " + currentTileNum);
				return;
			}

			// "paint" columns
			if (settingColumns) {
				setColumns(tileX);
			} // if

			// "paint" rows
			if (settingRows) {
				setRows(tileY);
				return;
			} // if

			// set sprite of single tile
			tiles.get(tileY * columns + tileX).setSprite(currentTileNum, currentLayer);

		} // mouseClicked

		@Override
		public void mouseEntered(MouseEvent e) {
			// System.out.println("enter");

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// System.out.println("exit");
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// System.out.println("pressed");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// System.out.println("released");
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

	public int getCurrentLayer() {
		return currentLayer;
	}
	
	public void save() {
		int[][] tempMap = new int[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tempMap[i][j] = tiles.get(i * columns + j).getSpriteNum();
			} // for
		} // for

		writeArrayToFile("map.txt", tempMap);
	}

	// writes the array a to fileName, one array element per line in the file
	public void writeArrayToFile(String fileName, int[][] a) {
		try {

			System.out.println("save");
			// output file pointer
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			out.write(rows + "");
			out.newLine();
			out.write(columns + "");
			out.newLine();
			
			for (int i = 0; i < rows; i++) {
				out.write("{");
				for (int j = 0; j < columns; j++) {
					out.write("" + a[i][j]);
					if (j < columns - 1) {
						out.write(", ");
					} // if
				} // for
				out.write("},");
				out.newLine();
			} // for

			out.close();

		} catch (Exception e) {
			System.out.println("File Output Error: " + e);
		} // catch

	} // writeArrayToFile

} // Game