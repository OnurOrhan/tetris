package student.orhan.onur.project2;

import java.awt.Color;

import drawing_framework.*;

/**
 * Testing class for Tetris
 * @author Onur Orhan
 */
public class Test {
	static GUI gui = new GUI();
	
	/**
	 * Main method
	 * @param args ...
	 */
	public static void main (String [] args) {
		both(); 		// Player versus the AI
//		AI();			// Only AI
//		humanPlayer();	// Only player
	}
	
	/**
	 * Only AI
	 */
	public static void AI(){
		Tetris show = new Tetris(gui);
		show.AI();
	}
	
	/**
	 * Only Player
	 */
	public static void humanPlayer(){
		Tetris game = new Tetris(gui);
		game.game();
	}
	
	/**
	 * Both AI and Player
	 */
	public static void both(){
		Canvas border = new Canvas(32, 640, 32);
		for (int j = 0; j < 20; j++) {
			border.fillGridSquare(0, j, Color.BLACK);
		}
				
		AI();
		gui.addCanvas(border);

		humanPlayer();
	}
	
}
