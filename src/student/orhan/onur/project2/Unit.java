package student.orhan.onur.project2;

/**
 * Class that saves a Unit with X and Y coordinates belonging to a piece
 * @author Onur Orhan
 */
public class Unit {
	private int x;
	private int y;
	
	/**
	 * Unit constructor
	 * @param x X coordinate of the Unit
	 * @param y Y coordinate of the Unit
	 */
	public Unit(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the X coordinate of the unit
	 * @return The X coordinate of the unit
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the X coordinate of the unit
	 * @return The X coordinate of the unit
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the Y coordinate of the unit
	 * @return The Y coordinate of the unit
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the Y coordinate of the unit
	 * @return The Y coordinate of the unit
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Sets both the coordinates of the unit
	 * @param x The X coordinate of the unit
	 * @param y The Y coordinate of the unit
	 */
	public void setCoordinates(int x, int y){
		setX(x);
		setY(y);
	}
	
}
