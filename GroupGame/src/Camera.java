import java.awt.Point;

public class Camera {
	
	private int x;
	private int y;
	private int w;
	private int h;
	
	public Camera() {
		x = 0;
		y = 0;
		w = 500;
		h = 500;
	}
	
	public Camera(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public void center(Entity e) {
		Point p = toIso((int)e.x, (int)e.y);
		x = p.x + 60 - w / 2;
		y = p.y + 30 - h / 2;
	}
	
	// returns the isometric coordinates for the cartesian coordinates passed in
	private Point toIso(int x, int y) {
		int isoX = x - y;
		int isoY = (x + y) / 2;
		return new Point(isoX, isoY);
	} // toIso
	
}
