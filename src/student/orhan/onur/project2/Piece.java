package student.orhan.onur.project2;

import java.awt.Color;
import java.util.ArrayList;

import drawing_framework.*;

/**
 * Class that handles operations with the Tetris pieces
 * @author Onur Orhan
 */
public class Piece implements Animatable{
	Unit center, ni, san, yon;
	Tetris tetris;
	Color color;
	protected boolean gameEnd = false;
	private boolean isAI;
	private int type = 0;
	private int orientation = 0;
	
	/**
	 * Piece constructor
	 * @param center Center unit of the piece
	 * @param ni Second unit of the piece
	 * @param san Third unit of the piece
	 * @param yon Fourth unit of the piece
	 * @param color Color of the piece
	 * @param tetris The current Tetris game
	 * @param isAI If the AI is going to handle the piece or not
	 * @param type Type of the piece
	 */
	public Piece(Unit center, Unit ni, Unit san, Unit yon, Color color, Tetris tetris, boolean isAI, int type){
		this.tetris = tetris;
		this.isAI = isAI;
		this.color 	= color;
		this.type = type;
		this.center	= center;
		this.ni 	= ni;
		this.san 	= san;
		this.yon 	= yon;
	}
	
	/**
	 * Method that moves the piece downwards, replaces the current piece with
	 * the next one, decides at which stage the game is at and finally calls the AI.
	 */
	public void move(AnimationCanvas canvas) {
		tetris.setTimer(tetris.getTimer()+1);
		
		if(isAI) {
			if(tetris.getTimer() % tetris.getPeriod() == 0 ||
				tetris.getTimer() % tetris.getPeriod() == tetris.getPeriod()/3) {
				
				if(tetris.ai.getStep() == 0) tetris.ai.scanning(tetris.rows);
				tetris.ai.check(this);
			}
		}
		
		if(tetris.getTimer() % tetris.getPeriod() == 0 && !gameEnd){
			if(tetris.getTimer()>400 && tetris.getPeriod() == 10) tetris.setPeriod(8);
			else if(tetris.getTimer()>800 && tetris.getPeriod() == 8) tetris.setPeriod(7);
			else if(tetris.getTimer()>1600 && tetris.getPeriod() == 7) tetris.setPeriod(6);
			else if(tetris.getTimer()>3200 && tetris.getPeriod() == 6) tetris.setPeriod(5);
			else if(tetris.getTimer()>6400 && tetris.getPeriod() == 5) tetris.setPeriod(4);
			
			if(onTop()){
				toGarbage();
				tetris.next = tetris.randomPiece();
				rowOps();
				
				if(!tetris.next.gameEnd()){
					center = tetris.next.center;
					ni = tetris.next.ni;
					san = tetris.next.san;
					yon = tetris.next.yon;
					color = tetris.next.color;
					type = tetris.next.getType();
					orientation = 0;
					tetris.ai.setStep(0);
				} else {
					if(tetris.rows[tetris.next.center.getY()].full[tetris.next.center.getX()] == false){
						tetris.rows[tetris.next.center.getY()].addGarbage(new Garbage(tetris.next.center.getX(), tetris.next.center.getY(), tetris.next.color), tetris.animationCanvas);
					} if(tetris.rows[tetris.next.ni.getY()].full[tetris.next.ni.getX()] == false){
						tetris.rows[tetris.next.ni.getY()].addGarbage(new Garbage(tetris.next.ni.getX(), tetris.next.ni.getY(), tetris.next.color), tetris.animationCanvas);
					} if(tetris.rows[tetris.next.san.getY()].full[tetris.next.san.getX()] == false){
						tetris.rows[tetris.next.san.getY()].addGarbage(new Garbage(tetris.next.san.getX(), tetris.next.san.getY(), tetris.next.color), tetris.animationCanvas);
					} if(tetris.rows[tetris.next.yon.getY()].full[tetris.next.yon.getX()] == false){
						tetris.rows[tetris.next.yon.getY()].addGarbage(new Garbage(tetris.next.yon.getX(), tetris.next.yon.getY(), tetris.next.color), tetris.animationCanvas);
					}
					
					gameEnd = true;
					tetris.next.gameEnd = true;
				}
			} else {
				center.setY(center.getY() - 1);
				ni.setY(ni.getY() - 1);
				san.setY(san.getY() - 1);
				yon.setY(yon.getY() - 1);
			}
		}
	}
	
	/**
	 * When a piece falls to the ground, this method checks if there is a complete row.
	 * If there is, removes the complete rows and shifts the others down when necessary.
	 */
	public void rowOps() {
		int start = minComplete();
		if(start < 20){
			ArrayList<Row> tempList = new ArrayList<Row>();
			for (int i = start; i < 20; i++) { // saving incomplete rows and removing other rows from the canvas
				if(!tetris.rows[i].getComplete()) tempList.add(tetris.rows[i]);
				else {
					tetris.setPoints(tetris.getPoints() + (tetris.getTimer() / tetris.getPeriod()));
					for(int m = 0; m < tetris.rows[i].pile.size(); m++) {
						tetris.animationCanvas.removeObject(tetris.rows[i].pile.get(m));
					}
				}
			}
			
			int a = 0;
			for(int g = start; g < start + tempList.size(); g++) { // replacing complete rows with the others
				tetris.rows[g] = tempList.get(a);
				for (int k = 0; k < tetris.rows[g].pile.size(); k++) {
					tetris.rows[g].pile.get(k).setY(g);
				}
				a++;
			}
			for(int h = start + tempList.size(); h < 20; h++) { // clearing other rows remaining on the top
				for(int m = 0; m < tetris.rows[h].pile.size(); m++) {
					tetris.animationCanvas.removeObject(tetris.rows[h].pile.get(m));
				}
				tetris.rows[h] = new Row();
			}
			tempList.clear();
		}
	}
	
	/**
	 * Returns the level of the lowest complete row
	 * @return Level of the lowest complete row (20 if there is no complete row)
	 */
	public int minComplete(){
		for(int d = Math.min(Math.min(center.getY(), ni.getY()), Math.min(san.getY(), yon.getY())); d < 20; d++){
			if(tetris.rows[d].getComplete()) return d;
		} return 20;
	}

	/**
	 * Paints the units of the piece; writes the points earned, which mode the game is at,
	 * and if the game is over.
	 */
	public void draw(AnimationCanvas canvas) {
		if(isAI) tetris.animationCanvas.drawText("AI", 9, 19, Color.BLACK);
		else tetris.animationCanvas.drawText("PLAYER", 8, 19, Color.BLACK);
		tetris.animationCanvas.drawText("Points: " + tetris.getPoints(), 0, 19, Color.BLACK);
		if(gameEnd == true) tetris.animationCanvas.drawText("GAME OVER", 0, 18, Color.BLACK);
		else {
			canvas.fillGridSquare(center.getX(), center.getY(), color);
			canvas.fillGridSquare(ni.getX(), ni.getY(), color);
			canvas.fillGridSquare(san.getX(), san.getY(), color);
			canvas.fillGridSquare(yon.getX(), yon.getY(), color);
		}
	}

	/**
	 * When a piece falls to the ground, this method saves the units as garbage and adds them
	 * to their corresponding rows.
	 */
	public void toGarbage(){
		tetris.rows[center.getY()].addGarbage(new Garbage(center.getX(), center.getY(), color), tetris.animationCanvas);
		tetris.rows[ni.getY()].addGarbage(new Garbage(ni.getX(), ni.getY(), color), tetris.animationCanvas);
		tetris.rows[san.getY()].addGarbage(new Garbage(san.getX(), san.getY(), color), tetris.animationCanvas);
		tetris.rows[yon.getY()].addGarbage(new Garbage(yon.getX(), yon.getY(), color), tetris.animationCanvas);
	}

	/**
	 * Returns true if the piece is overlapping with a previous one
	 * @return If the piece is overlapping with a previous one or not
	 */
	public boolean isOverlapping(){
		return (tetris.rows[center.getY()].full[center.getX()] 	== true	||
				tetris.rows[ni.getY()].full[ni.getX()] 			== true	||
				tetris.rows[san.getY()].full[san.getX()] 		== true	||
				tetris.rows[yon.getY()].full[yon.getX()] 		== true);
	}
	
	/**
	 * Checks if the game has reached an end
	 * @return If the game has reached an end or not
	 */
	public boolean gameEnd(){
		return (isOverlapping() || onTop());
	}
	
	/**
	 * Returns true if the piece is on the base level ( y = 0 )
	 * @return If the piece is on the base level or not
	 */
	public boolean onFloor(){
		return (center.getY() == 0 || ni.getY() == 0 || san.getY() == 0 || yon.getY() == 0);
	}
	
	/**
	 * Returns true if the piece is on top of another one
	 * @return If the piece is on top of another one or not
	 */
	public boolean onTop(){
		if(!onFloor()){ 
			return (tetris.rows[center.getY() - 1].full[center.getX()] 		== true	||
					tetris.rows[ni.getY() - 1].full[ni.getX()] 				== true	||
					tetris.rows[san.getY() - 1].full[san.getX()] 			== true	||
					tetris.rows[yon.getY() - 1].full[yon.getX()] 			== true);
		} else return true;
	}
	
	/**
	 * Returns true if there is a barrier next to the piece on the declared side
	 * @return If there is a barrier next to the piece on the declared side or not
	 */
	public boolean nextTo(boolean right){
		if(right){
			if (center.getX() >= 9 || ni.getX() >= 9 || san.getX() >= 9 || yon.getX() >= 9) return true;
			else return (tetris.rows[center.getY()].full[center.getX() + 1] 	== true	||
						tetris.rows[ni.getY()].full[ni.getX() + 1] 				== true	||
						tetris.rows[san.getY()].full[san.getX() + 1] 			== true	||
						tetris.rows[yon.getY()].full[yon.getX() + 1] 			== true);
		} else {
			if (center.getX() <= 0 || ni.getX() <= 0 || san.getX() <= 0 || yon.getX() <= 0) return true;
			else return (tetris.rows[center.getY()].full[center.getX() - 1] 	== true	||
						tetris.rows[ni.getY()].full[ni.getX() - 1] 			== true	||
						tetris.rows[san.getY()].full[san.getX() - 1] 		== true	||
						tetris.rows[yon.getY()].full[yon.getX() - 1] 		== true);
		}
	}
	
	/**
	 * Shifts the piece to the declared side ( true shifts right, false shifts left )
	 * @param right To be shifted to right or not
	 */
	public void shift(boolean right){
		if(!nextTo(right)){
			int v = 1;
			if(!right) v*=-1;
		
			center.setX(center.getX() + v);
			ni.setX(ni.getX() + v);
			san.setX(san.getX() + v);
			yon.setX(yon.getX() + v);
		}
	}
	
	/**
	 * Method that decides if the piece is within the canvas
	 * @return If the piece is within the canvas or not
	 */
	public boolean isWithin(){
		return (center.getX() >= 0	&& ni.getX() >= 0 	&& san.getX() >= 0	&& yon.getX() >= 0 &&
				center.getX() <= 9 	&& ni.getX() <= 9 	&& san.getX() <= 9	&& yon.getX() <= 9 &&
				center.getY() >= 0	&& ni.getY() >= 0 	&& san.getY() >= 0	&& yon.getY() >= 0);
	}
	
	/**
	 * Rotates the piece 90 degrees counterclockwise.
	 */
	public void rotate(){
		if(tetris.getTimer() % tetris.getPeriod() != 0 &&
				!onTop() && type != 0 && !(nextTo(true)&&nextTo(false))){
			Unit one = center;
			Unit two = ni;
			Unit three = san;
			Unit four = yon;
			int dx, dy;
			
			dx = ni.getX() - center.getX();
			dy = ni.getY() - center.getY();
			ni.setX(center.getX() - dy);
			ni.setY(center.getY() + dx);

			dx = san.getX() - center.getX();
			dy = san.getY() - center.getY();
			san.setX(center.getX() - dy);
			san.setY(center.getY() + dx);

			dx = yon.getX() - center.getX();
			dy = yon.getY() - center.getY();
			yon.setX(center.getX() - dy);
			yon.setY(center.getY() + dx);
			
			orientation++;
			
			if(!isWithin()) {
				int p = sideBorderCross();
				center.setX(center.getX() - p);
				ni.setX(ni.getX() - p);
				san.setX(san.getX() - p);
				yon.setX(yon.getX() - p);
			}
			
			if(isOverlapping()) {
					center = one;
					ni = two;
					san = three;
					yon = four;
					orientation--;
			}
		}
		orientation %= 4;
	}
	
	/**
	 * Returns how far is the piece away from the borders ( Returns 0 if inside the borders )
	 * @return How far is the piece away from the borders
	 */
	public int sideBorderCross(){
		int a =  Math.min(Math.min(center.getX(), ni.getX()), Math.min(san.getX(), yon.getX()));
		int b =  Math.max(Math.max(center.getX(), ni.getX()), Math.max(san.getX(), yon.getX()));
		if(a < 0) return a;
		else if(b > 9) return (b - 9);
		else return 0;
	}
	
	/**
	 * Shifts the piece one unit down for acceleration
	 */
	public void speedUp(){
		if(!onTop()/* && (period % timer>period*0.8)*/){
			center.setY(center.getY() - 1);
			ni.setY(ni.getY() - 1);
			san.setY(san.getY() - 1);
			yon.setY(yon.getY() - 1);
			tetris.setPoints(tetris.getPoints() + 1);
		}
	}

	/**
	 * Gets the type of the piece
	 * @return The type of the piece
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the piece
	 * @return The type of the piece
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the orientation of the piece
	 * @return The orientation of the piece
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Sets the orientation of the piece
	 * @return The orientation of the piece
	 * @param orientation The orientation of the piece
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
}
