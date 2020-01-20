package student.orhan.onur.project2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import drawing_framework.*;

/**
 * Class that initializes Tetris
 * @author Onur Orhan
 */
public class Tetris implements KeyListener{
	AI ai = new AI();
	AnimationCanvas animationCanvas = new AnimationCanvas(30);
	Piece current, next;
	Random random = new Random();
	int[] counter = new int[7];
	Row[] rows = new Row[20];
	private int period = 10;
	private int timer = 0;
	private int points = 0;
	private boolean isAI = false;
	
	/**
	 * Tetris constructor
	 * @param gui Graphical User Interface
	 */
	public Tetris(GUI gui){
		gui.addCanvas(animationCanvas);
		for (int i = 0; i < 20; i++) {
			rows[i] = new Row();
		}
	}
	
	/**
	 * Initializes Tetris with AI
	 */
	public void AI(){
		isAI = true;
		current = randomPiece();
		ai.setType(current.getType());
		ai.setCenterX(current.center.getX());
		ai.setCenterY(current.center.getY());
		animationCanvas.addObject(current);
		
		animationCanvas.start();
	}
	
	/**
	 * Initializes Tetris for Player
	 */
	public void game(){
		current = randomPiece();
		animationCanvas.addObject(current);
		
		animationCanvas.addKeyListener(this);
		animationCanvas.setFocusable(true);
		
		animationCanvas.start();
	}

	/**
	 * Initializes Tetris for AI and Player
	 */
	public void both(){
		AI();
		game();
	}
	
	/**
	 * Checks if a type of piece is too frequently spawning
	 * @param a Type of the piece
	 * @return If the piece is too abundant or not
	 */
	public boolean exceed(int a){
		double average = 0;
		
		for (int i = 0; i < counter.length; i++) {
			average += counter[i];
		}
		
		average = (double) average / counter.length;
		
		if(counter[a] - average >= 0.2) return true;
		return false;
	}

	/**
	 * Generates a random Tetris piece
	 * @return A random Tetris piece
	 */
	public Piece randomPiece(){
		int a = (random.nextInt(49) + 1) % 7;
		while(exceed(a)){
			a = (random.nextInt(49) + 1) % 7;
		}
		counter[a]++;
		if(a == 0){
			return new Piece(new Unit(5,19), new Unit(5,18), new Unit(4,19), new Unit(4,18), Color.YELLOW, this, isAI, a);
		} else if(a == 1){
			return new Piece(new Unit(5,19), new Unit(6,19), new Unit(4,19), new Unit(3,19), Color.CYAN, this, isAI, a);
		} else if(a == 2){
			return new Piece(new Unit(5,19), new Unit(6,19), new Unit(5,18), new Unit(4,18), Color.GREEN, this, isAI, a);
		} else if(a == 3){
			return new Piece(new Unit(5,19), new Unit(4,19), new Unit(5,18), new Unit(6,18), Color.RED, this, isAI, a);
		} else if(a == 4){
			return new Piece(new Unit(5,19), new Unit(6,19), new Unit(4,19), new Unit(4,18), Color.ORANGE, this, isAI, a);
		} else if(a == 5){
			return new Piece(new Unit(5,19), new Unit(4,19), new Unit(6,19), new Unit(6,18), Color.BLUE, this, isAI, a);
		} else return new Piece(new Unit(5,19), new Unit(6,19), new Unit(4,19), new Unit(5,18), Color.MAGENTA, this, isAI, a);
	}
	
	/**
	 * Takes keyboard input from Player
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.current.rotate();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.current.speedUp();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.current.shift(false);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.current.shift(true);
		}
	}

	public void keyTyped(KeyEvent e) {

	}
	
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Returns period of falling down
	 * @return Period of falling down
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * Sets period of falling down
	 * @param period Period of falling down
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * Gets timer that increases as the game continues
	 * @return Timer
	 */
	public int getTimer() {
		return timer;
	}

	/**
	 * Sets the game timer
	 * @param timer Game timer
	 */
	public void setTimer(int timer) {
		this.timer = timer;
	}

	/**
	 * Gets points earned
	 * @return Points earned
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets points earned
	 * @param points Points earned
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
}
