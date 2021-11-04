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
	
	private Entity entity;
	private String prefix;
	private String suffix;
	
	private int min;
	
	public Animation(Entity e, String prefix, String suffix, int min, int max) { // temp
		this.frames = new ArrayList<Sprite>();
		
		this.prefix = prefix;
		this.suffix = suffix;
		
		this.min = min;
		
		for (int i = min; i < max; i++) {
			System.out.println("i " + i);
			frames.add((SpriteStore.get()).getSprite(prefix + i + suffix));
			System.out.println(prefix + i + suffix);
		} // for
		
		
		totalFrames = this.frames.size();
		
		currentFrame = min;
		counter = 0;
		delayBetweenFrames = 100;
		
		isPlaying = false;
		entity = e;
		
		//SpriteStore.get().addAnimation(prefix, this);
	} // Animation
	
	
	public void start() {
		if (totalFrames < 1) { return; }
		isPlaying = true;
	} // start
	
	public void stop() { // not implemented
		
	}
	
	// update animation
	public void update(long delta) {
		if (!isPlaying) { return; }
		counter++; // use delta?
		
		if (counter > delayBetweenFrames) {
			counter = 0;
			
			currentFrame++;
			if (currentFrame > totalFrames - 1) {
				currentFrame = min;
			}
		} // if
		
		entity.setSprite(prefix + currentFrame + suffix);
	} // update
	
} // Animation
