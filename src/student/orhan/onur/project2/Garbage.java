package student.orhan.onur.project2;

import java.awt.Color;

import drawing_framework.*;

/**
 * Class that saves the dead Units as Garbage
 * @author Onur Orhan
 */
public class Garbage extends Unit implements Animatable {
	private Color color;
	
	/**
	 * Garbage constructor
	 * @param x X coordinate of the Garbage
	 * @param y Y coordinate of the Garbage
	 * @param color Color of the Garbage
	 */
	public Garbage(int x, int y, Color color) {
		super(x, y);
		this.color = color;
	}

	/**
	 * Gets the color of the garbage
	 * @return The color of the garbage
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the garbage
	 * @return The color of the garbage
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	public void move(AnimationCanvas canvas) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Fills the corresponding coordinate with the current color
	 */
	public void draw(AnimationCanvas canvas) {
		canvas.fillGridSquare(super.getX(), super.getY(), color);
	}
	
}
