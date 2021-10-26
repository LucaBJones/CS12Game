import java.awt.Point;

public class Camera {

	// coordinates of camera's top left corner
	private static int x = 0;
	private static int y = 0;

	private static int w = 750; // camera width
	private static int h = 750; // camera height

	public static int getX() {
		return x;
	} // getX

	public static int getY() {
		return y;
	} // getY

	public static int getWidth() {
		return w;
	} // getWidth

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