import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Explosion extends Entity {

	
	public Explosion(int xPos, int yPos, int lifeTime) {
		int num = (Math.random() > 0.5) ? 1 : 0;
		
		x = xPos;
		y = yPos;
		
		sprite = SpriteStore.get().getSprite("animations/explosions/exp0_0.png");
		
		animation = new Animation(this, "animations/explosions/exp" + num + "_", ".png", 0, 3, 150, false);
		animation.start();
		
		Timer timer = new Timer(lifeTime, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               remove();
            } // actionPerformed
        });
		timer.start();
		
	} // Explosion
	
	private void remove() {
		Game.removeEntity(this);
	} // remove
	
} // Explosion
