import java.awt.Point;

public class Camera {

	// coordinates of camera's top left corner
	private static int x = 0;
	private static int y = 0;

	private static int w = 1920; // camera width
	private static int h = 1080; // camera height

	// returns horizontal position
	public static int getX() {
		return x;
	} // getX

	// returns vertical position
	public static int getY() {
		return y;
	} // getY

	// returns camera width
	public static int getWidth() {
		return w;
	} // getWidth

	// returns camera height
	public static int getHeight() {
		return h;
	} // getHeight

	// center the camera on an entity using its isometric coordinates
	public static void center(Entity e) {
		Point iso = e.toIso((int) e.x, (int) e.y);
		x = iso.x + e.sprite.getWidth() / 2 - w / 2;
		y = iso.y + Entity.TILE_LENGTH - e.sprite.getHeight() / 2 - h / 2;
	} // center

} // Camera
