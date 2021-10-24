import java.awt.Point;

public class Camera {

	// coordinates of camera's top left corner
	private int x;
	private int y;

	private int w; // camera width
	private int h; // camera height

	// constructors

	public Camera() {
		x = 0;
		y = 0;
		w = 750;
		h = 750;
	} // default constructor

	public Camera(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	} // constructor

	// get methods

	public int getX() {
		return x;
	} // getX

	public int getY() {
		return y;
	} // getY

	public int getWidth() {
		return w;
	} // getWidth

	public int getHeight() {
		return h;
	} // getHeight

	// center the camera on an entity using its isometric coordinates
	public void center(Entity e) {
		Point iso = e.toIso((int) e.x, (int) e.y);
		x = iso.x + e.sprite.getWidth() / 2 - w / 2;
		y = iso.y + Entity.TILE_LENGTH - e.sprite.getHeight() / 2 - h / 2;
	} // center

} // Camera