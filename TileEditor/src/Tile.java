import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Tile { 

	public static final int TILE_LENGTH = 60;
	
	protected double x;
	protected double y;
	
	private int currentSpriteNum;
	
	TileEditor game;
	
	Sprite sprite;
	private boolean hasSprite = false;
	
	public Tile(int xTile, int yTile, TileEditor g) {
		x = xTile * TILE_LENGTH;
		y = yTile * TILE_LENGTH;
		game = g;
	} // constructor
	
	public void updatePosition(int xTile, int yTile) {
		x = xTile * TILE_LENGTH;
		y = yTile * TILE_LENGTH;
	}
	
	// convert cartesian to isometric
	public static Point toIso(int x, int y) {
		int isoX = x - y;
		int isoY = (x + y) / 2;
		return new Point(isoX, isoY);
	} // toIso
	
	// draws the sprite to the screen relative to camera position
	public void draw(Graphics g) {
		if (hasSprite) {
			
			// draw sprite
			Point isoPoint = toIso((int) x, (int) y);
			sprite.draw(g, isoPoint.x + game.getXOffset() - TILE_LENGTH, isoPoint.y + game.getYOffset());
		} else {
			
			// draw grid
			Point tl = toIso((int) x, (int) y);
			Point tr = toIso((int) x + TILE_LENGTH, (int) y);
			Point bl = toIso((int) x, (int) y + TILE_LENGTH);
			Point br = toIso((int) x + TILE_LENGTH, (int) y + TILE_LENGTH);
						
			g.setColor(Color.black);

			g.drawLine(tl.x + game.getXOffset(), tl.y + game.getYOffset(), tr.x + game.getXOffset(), tr.y + game.getYOffset());
			g.drawLine(tr.x + game.getXOffset(), tr.y + game.getYOffset(), br.x + game.getXOffset(), br.y + game.getYOffset());
			g.drawLine(bl.x + game.getXOffset(), bl.y + game.getYOffset(), br.x + game.getXOffset(), br.y + game.getYOffset());
			g.drawLine(tl.x + game.getXOffset(), tl.y + game.getYOffset(), bl.x + game.getXOffset(), bl.y + game.getYOffset());
		}
	} // draw
	
	public void setSprite(int n) {
		sprite = (SpriteStore.get()).getSprite("images/tile" + n + ".png");
		currentSpriteNum = n;
		hasSprite = true;
		System.out.println("set sprite");
	}
	
	public int getSpriteNum() {
		return currentSpriteNum;
	}
	
} // Tile