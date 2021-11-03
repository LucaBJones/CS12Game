import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Tile {

	public static final int TILE_LENGTH = 60;

	private final int LAYER_PADDING = 50;
	
	protected double x;
	protected double y;

	private int currentSpriteNum;

	TileEditor game;

	Sprite[] sprites = new Sprite[2];

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
		Point isoPoint = toIso((int) x, (int) y);

		if (sprites[0] != null) {
			sprites[0].draw(g, isoPoint.x + game.getXOffset() - TILE_LENGTH,
					isoPoint.y - sprites[0].getHeight() + TILE_LENGTH + game.getYOffset());
		}

		if (sprites[1] != null) {
			sprites[1].draw(g, isoPoint.x + game.getXOffset() - TILE_LENGTH,
					isoPoint.y - sprites[1].getHeight() + TILE_LENGTH + game.getYOffset());
		}

		if (sprites[0] == null) {

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
		
		if (sprites[1] == null) {

			// draw grid
			Point tl = toIso((int) x, (int) y);
			Point tr = toIso((int) x + TILE_LENGTH, (int) y);
			Point bl = toIso((int) x, (int) y + TILE_LENGTH);
			Point br = toIso((int) x + TILE_LENGTH, (int) y + TILE_LENGTH);

			g.setColor(new Color(255, 255, 0, 30));

			g.drawLine(tl.x + game.getXOffset(), tl.y + game.getYOffset() - LAYER_PADDING, tr.x + game.getXOffset(), tr.y + game.getYOffset() - LAYER_PADDING);
			g.drawLine(tr.x + game.getXOffset(), tr.y + game.getYOffset() - LAYER_PADDING, br.x + game.getXOffset(), br.y + game.getYOffset() - LAYER_PADDING);
			g.drawLine(bl.x + game.getXOffset(), bl.y + game.getYOffset() - LAYER_PADDING, br.x + game.getXOffset(), br.y + game.getYOffset() - LAYER_PADDING);
			g.drawLine(tl.x + game.getXOffset(), tl.y + game.getYOffset() - LAYER_PADDING, bl.x + game.getXOffset(), bl.y + game.getYOffset() - LAYER_PADDING);
		}
		
	} // draw

	public void setSprite(int n, int currentLayer) {
		if (currentLayer == 0) {
			sprites[currentLayer] = (SpriteStore.get()).getSprite("images/tile" + n + ".png");
		} else if (currentLayer == 1) {
			sprites[currentLayer] = (SpriteStore.get()).getSprite("images/obs" + n + ".png");
		}
		
		currentSpriteNum = n;
	}

	public int getSpriteNum() {
		return currentSpriteNum;
	}

} // Tile