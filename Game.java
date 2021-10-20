import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 500;
	
	public static void main(String[] args) {
		Game game = new Game();
	} // Game
	
	public Game() {
		JFrame frame = new JFrame("Game");
		JPanel panel = new JPanel();
		
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	} // Game
	
} // Game