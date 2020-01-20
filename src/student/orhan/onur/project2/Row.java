package student.orhan.onur.project2;

import java.util.ArrayList;

import drawing_framework.Animatable;
import drawing_framework.AnimationCanvas;

/**
 * Class that holds the Rows along with their Garbage and occupancy information
 * @author Onur
 */
public class Row implements Animatable{
	ArrayList<Garbage> pile = new ArrayList<Garbage>();
	boolean[] full = new boolean[10];
	
	/**
	 * Row constructor
	 */
	public Row(){
		for (int i = 0; i < 10; i++) {
			full[i] = false;
		}
	}
	
	/**
	 * Method that adds Garbage from a dead piece to its Garbage Pile 
	 * @param addition The garbage to be added
	 * @param canvas Animation Canvas
	 */
	public void addGarbage(Garbage addition, AnimationCanvas canvas){
		pile.add(addition);
		full[addition.getX()] = true;
		canvas.addObject(addition);
	}
	
	/**
	 * Returns true if the row is complete
	 * @return If the row is complete or not
	 */
	public boolean getComplete(){
		if(pile.size() == 10) return true;
		return false;
	}
	
	/**
	 * Returns true if the row is empty
	 * @return If the row is empty or not
	 */
	public boolean getEmpty(){
		if(pile.size() == 0) return true;
		return false;
	}

	public void move(AnimationCanvas canvas) {
		
	}

	/**
	 * Method that deals with drawing all the Garbage in the current pile
	 */
	public void draw(AnimationCanvas canvas) {
		for (int i = 0; i < pile.size(); i++) {
			pile.get(i).draw(canvas);
		}
	}

}
