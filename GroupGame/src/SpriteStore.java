/***********************************************************************
* Name: SpriteStore.java
* Author: Mrs. Wear
* Purpose:  Manages the sprites in the game. Caches them for future use.
************************************************************************/

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SpriteStore {

	private static SpriteStore spriteStore = new SpriteStore(); // one instance of this class will exist
	
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>(); // stores sprites
	private HashMap<String, Animation> animations = new HashMap<String, Animation>(); // stores sprites
	

	// returns the riteStore instance
	public static SpriteStore get() {
		return spriteStore;
	} // get

	// returns the sprite associated with the String passed in
	public Sprite getSprite(String ref) {
		
		// if the sprite is already in the HashMap, return it
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		} // if

		System.out.println("ref: " + ref);
		
		// else, load the image into the HashMap
		BufferedImage sourceImage = null;

		try {
			
			// get the image location
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				System.out.println("Failed to load: " + ref);
				System.exit(0); // exit program if file not found
			} // if
			sourceImage = ImageIO.read(url); // get image
			
		} catch (IOException e) {
			
			System.out.println("Failed to load: " + ref);
			
			// exit program if file not loaded
			System.exit(0); 
			
		} // catch

		// create an accelerated image (correct size) to store the sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

		// draw the source image into the accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);

		// create a sprite, add it to the cache, and return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);

		return sprite;
	} // getSprite
	
} // SpriteStore
