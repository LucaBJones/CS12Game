import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Animation {

	private ArrayList<Sprite> frames;
	
	private int currentFrame;
	private int totalFrames;
	
	private int counter;
	private int delayBetweenFrames;
	
	private boolean isPlaying;
	
	public Animation(String animName) { // temp
		this.frames = new ArrayList<Sprite>();

		for (int i = 0; i < 15; i++) {
			frames.add((SpriteStore.get()).getSprite("animations/" + animName + i + ".png"));
		} // for
		
		
		totalFrames = this.frames.size();
		
		currentFrame = 0;
		counter = 0;
		delayBetweenFrames = 180;
		
		isPlaying = false;
	} // Animation
	
//	public Animation(ArrayList<Sprite> frames) {
//		this.frames = frames;
//		totalFrames = this.frames.size();
//		
//		currentFrame = 0;
//		counter = 0;
//		delayBetweenFrames = 1;
//		
//		isPlaying = false;
//	} // Animation
	
	public void start() {
		if (totalFrames < 1) { return; }
		isPlaying = true;
	} // start
	
	public void stop() {
		
	}
	
	// update animation
	public void update(long delta) {
		if (!isPlaying) { return; }
		counter++; // use delta?
		
		if (counter > delayBetweenFrames) {
			counter = 0;

			
			currentFrame++;
			if (currentFrame > totalFrames - 1) {
				currentFrame = 0;
			}
		} // if
	} // update
	
//	// temp
//	public void draw(Graphics g) {
//		if (!isPlaying || totalFrames < 1) { return; }
//		
//		frames.get(currentFrame).draw(g, 10, 10);
//	}
	
	public void draw(Graphics g, int xPos, int yPos) {
		if (!isPlaying || totalFrames < 1) { return; }
		
		frames.get(currentFrame).draw(g, xPos, yPos);
	}
	
} // Animation
