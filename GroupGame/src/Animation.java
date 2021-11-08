import java.util.ArrayList;

public class Animation {

	private ArrayList<Sprite> frames;
	
	// stores frame numbers
	private int min; // first frame number
	private int currentFrame;
	private int totalFrames;
	
	// used for delaying animations
	private int counter;	 
	private int delayBetweenFrames;
	
	private boolean isPlaying;
	private boolean loop;
	
	private Entity entity;
	
	//parts of file name
	private String prefix;
	private String suffix;
	
	public Animation(Entity e, String prefix, String suffix, int min, int max, int frameDelay, boolean loop) { // temp
		this.frames = new ArrayList<Sprite>();
		
		this.prefix = prefix;
		this.suffix = suffix;
		
		this.min = min;
		this.loop = loop;
		
		for (int i = min; i <= max; i++) {
			frames.add((SpriteStore.get()).getSprite(prefix + i + suffix));
			System.out.println(prefix + i + suffix);
		} // for
		
		totalFrames = this.frames.size();
		
		currentFrame = min;
		counter = 0;
		delayBetweenFrames = frameDelay;
		
		isPlaying = false;
		entity = e;
		
	} // Animation
	
	public void start() {
		if (totalFrames < 1) { return; }
		isPlaying = true;
	} // start
	
	// update animation
	public void update(long delta) {
		if (!isPlaying) { return; }
		counter += delta; // use delta?
		
		// if is time to update frame
		if (counter > delayBetweenFrames) {
			counter = 0;
			
			// update to next frame
			currentFrame++;
			
			// check if is at last frame
			if (currentFrame > totalFrames + min - 1) { // ? is this right?
				
				// loop animation
				if (loop) {
					currentFrame = min;
				} else {
					currentFrame = totalFrames + min - 1;
					isPlaying = false;
				} // else
			} // if
		} // if
		
		entity.setSprite(prefix + currentFrame + suffix);
	} // update
	
} // Animation
