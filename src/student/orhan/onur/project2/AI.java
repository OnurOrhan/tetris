package student.orhan.onur.project2;

/**
 * Artificial Intelligence for Tetris
 * @author Onur Orhan
 */
public class AI {
	private int type;
	private int orientation = 0;
	private int orientation2 = 0;
	private int step = 0;
	private int centerX, centerY;
	private int[] surfaceY = new int[10];
	private int targetX; // the leftmost x value of the targeted spot
	private int targetY;

	/**
	 * AI constructor
	 */
	public AI(){
	}
	
	/**
	 * Leads the AI to corresponding methods according to the type of the piece
	 * and refreshes some information about the piece.
	 * @param piece The piece that needs orders
	 */
	public void check(Piece piece){
		centerX = piece.center.getX();
		centerY = piece.center.getY();
		orientation = piece.getOrientation();
		type = piece.getType();
		
		if(step==1){
			if(type==0) suitablePlace0();
			else if(type==1) suitablePlace1();
			else if(type==2) suitablePlace2();
			else if(type==3) suitablePlace3();
			else if(type==4) suitablePlace4();
			else if(type==5) suitablePlace5();
			else suitablePlace6();
			step++;
		} else if(step==2){
			turn(piece);
			align(piece);
		}
	}
	
	/**
	 * Scans all the columns and finds which unit is empty at the top.
	 * Saves these Y coordinates into an array called surfaceY[];
	 * @param rows The 20 rows on the game board
	 */
	public void scanning(Row[] rows){
		orientation2 = 0;
		int temp = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 19; j++) {
				if(rows[j].full[i] == true) temp = j + 1;
			}
			surfaceY[i] = temp;
			temp = 0;
		}
		step++;
	}
	
	/**
	 * Finds a suitable place for the piece O.
	 */
	public void suitablePlace0(){
		int x = 10;
		int y = 20;
		for (int i = 0; i < 9; i++) {
			if(surfaceY[i] == surfaceY[i+1]) {
				if(surfaceY[i] < y) {
					y = surfaceY[i];
					x = i;
				}
			}
		} if(y == 20) {
			for (int n = 0; n < 9; n++) {
				if(surfaceY[n] < y && surfaceY[n + 1] < y) {
					y = Math.max(surfaceY[n], surfaceY[n + 1]);
					x = n;
				}
			}
		}
		targetX = x;
		targetY = y;
	}
	
	/**
	 * Finds a suitable place for the piece I.
	 */
	public void suitablePlace1(){
		int x = 10;
		int y = 20;
		int tricky = -100;
		int depth = 0;
		int flat = 20;
		int flatIndex = -1;
		
		for (int i = 0; i < 9; i++) {
			if(i < 7) { // for flat surface with 4 available slots
				if(surfaceY[i] == surfaceY[i+1] && surfaceY[i+1] == surfaceY[i+2] &&
					surfaceY[i+2] == surfaceY[i+3] && surfaceY[i] < flat) {
					flat = surfaceY[i];
					flatIndex = i;
				}
			}
			if(Math.pow(Math.abs(surfaceY[i] - surfaceY[i+1]),2) - Math.min(surfaceY[i], surfaceY[i+1]) > tricky) {
				depth = Math.abs(surfaceY[i] - surfaceY[i+1]);
				tricky = (int) (Math.pow(depth, 2) - Math.min(surfaceY[i], surfaceY[i+1]));
				if(surfaceY[i] < surfaceY[i+1]) {
					x = i;
					y = surfaceY[i];
				} else {
					x = i + 1;
					y = surfaceY[i+1];
				}
			}
		}
		if(depth > 2) {
			targetX = x;
			targetY = y;
			orientation2 = 1;
		} else if(flatIndex != -1) {
			targetX = flatIndex;
			targetY = flat;
		} else {
			targetX = x;
			targetY = y;
			orientation2 = 1;
		}
	}

	/**
	 * Finds a suitable place for the piece S.
	 */
	public void suitablePlace2(){
		int flatx = 10;
		int flaty = 20;
		int vertx = 10;
		int verty = 20;
		for (int i = 0; i < 8; i++) {
			if(surfaceY[i] == surfaceY[i+1] && surfaceY[i+2] == surfaceY[i+1] + 1 &&
					surfaceY[i] < flaty) {
				flaty = surfaceY[i];
				flatx = i;
			}
		}
		for (int i = 0; i < 9; i++) {
			if(surfaceY[i] - 1 == surfaceY[i+1] && surfaceY[i] < verty) {
				verty = surfaceY[i];
				vertx = i;
			}
		} if(flaty < 20 || verty < 20) {
			if(flaty <= verty - 1) {
				targetX = flatx;
				targetY = flaty;
			} else {
				targetX = vertx;
				targetY = verty;
				orientation2 = 1;
			}
		} else { // if there is no perfect place for the piece, then searching for alternatives...
			for (int i = 0; i < 8; i++) {
				if(surfaceY[i] < surfaceY[i+2] && surfaceY[i+1] < surfaceY[i+2] && surfaceY[i+2] - 1 < flaty) {
					flaty = surfaceY[i+2] - 1;
					flatx = i;
				}
			}
			for (int i = 0; i < 9; i++) {
				if(surfaceY[i] > surfaceY[i+1] && surfaceY[i] < verty) {
					verty = surfaceY[i];
					vertx = i;
				}
			}
		} if(flaty <= verty - 1) {
			targetX = flatx;
			targetY = flaty;
		} else {
			targetX = vertx;
			targetY = verty;
			orientation2 = 1;
		}
	}

	/**
	 * Finds a suitable place for the piece Z.
	 */
	public void suitablePlace3(){
		int flatx = 10;
		int flaty = 20;
		int vertx = 10;
		int verty = 20;
		for (int i = 0; i < 8; i++) {
			if(surfaceY[i+1] == surfaceY[i+2] && surfaceY[i] == surfaceY[i+1] + 1 &&
					surfaceY[i] < flaty) {
				flaty = surfaceY[i];
				flatx = i;
			}
		}
		for (int i = 0; i < 9; i++) {
			if(surfaceY[i] + 1 == surfaceY[i+1] && surfaceY[i] < verty) {
				verty = surfaceY[i];
				vertx = i;
			}
		} if(flaty < 20 || verty < 20) {
			if(flaty - 1 <= verty) {
				targetX = flatx;
				targetY = flaty;
			} else {
				targetX = vertx;
				targetY = verty;
				orientation2 = 1;
			}
		} else { // searching for other alternatives...
			for (int i = 0; i < 8; i++) {
				if(surfaceY[i] > surfaceY[i+1] && surfaceY[i] > surfaceY[i+2] && surfaceY[i] < flaty) {
					flaty = surfaceY[i];
					flatx = i;
				}
			}
			for (int i = 0; i < 9; i++) {
				if(surfaceY[i] < surfaceY[i+1] && surfaceY[i+1] - 1 < verty) {
					verty = surfaceY[i+1] - 1;
					vertx = i;
				}
			}
		} if(flaty - 1 <= verty) {
			targetX = flatx;
			targetY = flaty;
		} else {
			targetX = vertx;
			targetY = verty;
			orientation2 = 1;
		}
	}

	/**
	 * Finds a suitable place for the piece L.
	 */
	public void suitablePlace4(){
		int flat2x = 10;	int flat2y = 20;
		int flat3x = 10;	int flat3y = 20;
		int deepx = 10;		int deepy = 20;
		int shallowx = 10;	int shallowy = 20;
		int x = 10;			int y = 20;
		
		for (int i = 0; i < 9; i++) {
			if(i < 8) {
				if(surfaceY[i] + 1 == surfaceY[i+1] && surfaceY[i+1] == surfaceY[i+2] &&
					surfaceY[i] < shallowy) {
					shallowy = surfaceY[i];
					shallowx = i;
				} if(surfaceY[i] == surfaceY[i+1] && surfaceY[i+1] == surfaceY[i+2] &&
					surfaceY[i] < flat3y) {
					flat3y = surfaceY[i];
					flat3x = i;
				}
			}
			if(surfaceY[i] - 2 == surfaceY[i+1] && surfaceY[i] < deepy) {
				deepy = surfaceY[i];
				deepx = i;
			}
			if(surfaceY[i] == surfaceY[i+1] && surfaceY[i] < flat2y) {
				flat2y = surfaceY[i];
				flat2x = i;
			}
		} if(deepy < 20 || shallowy < 20 || flat2y < 20 || flat3y < 20) {
			if(Math.min(Math.min(shallowy, deepy - 2), Math.min(flat2y, flat3y)) == deepy - 2) {
				targetX = deepx;
				targetY = deepy;
				orientation2 = 3;
			} else if(Math.min(Math.min(shallowy, deepy - 2), Math.min(flat2y, flat3y)) == shallowy) {
				targetX = shallowx;
				targetY = shallowy;
			} else if(Math.min(shallowy, Math.min(flat2y, flat3y)) == flat3y){
				targetX = flat3x;
				targetY = flat3y;
				orientation2 = 2;
			} else {
				targetX = flat2x;
				targetY = flat2y;
				orientation2 = 1;
			}
		} else {
			for (int p = 0; p < 9; p++) {
				if(p < 8) {
					if(surfaceY[p] < surfaceY[p+1] && surfaceY[p+2] < surfaceY[p+1] &&
						surfaceY[p+1] - surfaceY[p] < 3 && surfaceY[p+1] - surfaceY[p+2] < 3 &&
						surfaceY[p+1] < y) {
						y = surfaceY[p+1] - 1;
						x = p;
					}
				}
				if(surfaceY[p] > surfaceY[p+1] && surfaceY[p] < y) {
					y = surfaceY[p];
					x = p;
					orientation2 = 3;
				}
			}
			targetX = x;
			targetY = y;
		}
	}

	/**
	 * Finds a suitable place for the piece J.
	 */
	public void suitablePlace5(){
		int flat2x = 10;	int flat2y = 20;
		int flat3x = 10;	int flat3y = 20;
		int deepx = 10;		int deepy = 20;
		int shallowx = 10;	int shallowy = 20;
		int x = 10;			int y = 20;
		
		for (int i = 0; i < 9; i++) {
			if(i < 8) {
				if(surfaceY[i+2] + 1 == surfaceY[i] && surfaceY[i] == surfaceY[i+1] &&
					surfaceY[i] < shallowy) {
					shallowy = surfaceY[i];
					shallowx = i;
				} if(surfaceY[i] == surfaceY[i+1] && surfaceY[i+1] == surfaceY[i+2] &&
					surfaceY[i] < flat3y) {
					flat3y = surfaceY[i];
					flat3x = i;
				}
			}
			if(surfaceY[i] + 2 == surfaceY[i+1] && surfaceY[i] < deepy) {
				deepy = surfaceY[i];
				deepx = i;
			}
			if(surfaceY[i] == surfaceY[i+1] && surfaceY[i] < flat2y) {
				flat2y = surfaceY[i];
				flat2x = i;
			}
		} if(deepy < 20 || shallowy < 20 || flat2y < 20 || flat3y < 20) {
			if(Math.min(Math.min(shallowy - 1, deepy), Math.min(flat2y, flat3y)) == deepy) {
				targetX = deepx;
				targetY = deepy;
				orientation2 = 1;
			} else if(Math.min(Math.min(shallowy - 1, deepy), Math.min(flat2y, flat3y)) == shallowy - 1) {
				targetX = shallowx;
				targetY = shallowy;
			} else if(Math.min(Math.min(shallowy - 1, deepy), Math.min(flat2y, flat3y)) == flat3y){
				targetX = flat3x;
				targetY = flat3y;
				orientation2 = 2;
			} else {
				targetX = flat2x;
				targetY = flat2y;
				orientation2 = 3;
			}
		} else {
			for (int p = 0; p < 9; p++) {
				if(p < 8) {
					if(surfaceY[p+1] > surfaceY[p] && surfaceY[p+1] > surfaceY[p+2] &&
						surfaceY[p+1] - surfaceY[p] < 3 && surfaceY[p+1] - surfaceY[p+2] < 3 &&
						surfaceY[p+1] < y) {
						y = surfaceY[p+1];
						x = p;
					}
				}
				if(surfaceY[p] < surfaceY[p+1] && surfaceY[p+1] < y) {
					y = surfaceY[p];
					x = p;
					orientation2 = 1;
				}
			}
			targetX = x;
			targetY = y;
		}		
	}

	/**
	 * Finds a suitable place for the piece T.
	 */
	public void suitablePlace6(){
		int leftx = 10;		int lefty = 20;		// faces left
		int rightx = 10;	int righty = 20; 	// faces right
		int flatx = 10;		int flaty = 20;		// faces up
		int tx = 10;		int ty = 20;		// faces down
		int x = 10;			int y = 20;
		
		for (int i = 0; i < 9; i++) {
			if(i < 8) {
				if(surfaceY[i] == surfaceY[i+2] && surfaceY[i+1] + 1 == surfaceY[i] &&
					surfaceY[i] < ty) {
					ty = surfaceY[i];
					tx = i;
				}
				if(surfaceY[i] == surfaceY[i+1] && surfaceY[i+1] == surfaceY[i+2] &&
					surfaceY[i] < flaty) {
					flaty = surfaceY[i];
					flatx = i;
				}
			}
			if(surfaceY[i] + 1 == surfaceY[i+1] && surfaceY[i] < righty) {
				righty = surfaceY[i];
				rightx = i;
			}
			if(surfaceY[i] - 1 == surfaceY[i+1] && surfaceY[i] < lefty) {
				lefty = surfaceY[i];
				leftx = i;
			}
		} if(righty < 20 || lefty < 20 || flaty < 20 || ty < 20) {
			if(Math.min(Math.min(ty - 1, flaty), Math.min(righty, lefty - 1)) == flaty) {
				targetX = flatx;
				targetY = flaty;
				orientation2 = 2;
			} else if(Math.min(Math.min(ty - 1, flaty), Math.min(righty, lefty - 1)) == ty - 1) {
				targetX = tx;
				targetY = ty;
			} else if(Math.min(Math.min(ty - 1, flaty), Math.min(righty, lefty - 1)) == righty){
				targetX = rightx;
				targetY = righty;
				orientation2 = 1;
			} else {
				targetX = leftx;
				targetY = lefty;
				orientation2 = 3;
			}
		} else {
			for (int p = 0; p < 9; p++) {
				if(Math.abs(surfaceY[p+1] - surfaceY[p]) < 3 && Math.min(surfaceY[p], surfaceY[p+1]) < y) {
					if(surfaceY[p] <= surfaceY[p+1]) orientation2 = 1;
					else orientation = 3;
					y = surfaceY[p];
					x = p;
				}
			}
			targetX = x;
			targetY = y;
		}
	}
	
	/**
	 * Rotates the piece until it is in the desired orientation
	 * @param piece The piece that needs orders
	 */
	public void turn(Piece piece){
		if(orientation2 > orientation) piece.rotate();
	}
	
	/**
	 * Aligns the piece according to the targeted unit by using
	 * the shift() method
	 * @param piece The piece that needs orders
	 */
	public void align(Piece piece){
		int difference = difference();
		if(difference > 0) piece.shift(true);
		else if(difference < 0) piece.shift(false);
		else piece.speedUp();
	}
	
	/**
	 * Gets how far the piece is from the target
	 * @return How far is the piece aligned from the target
	 */
	public int difference(){
		if(type==0 || (type==2 && orientation==0) || (type==3 && orientation==0) ||
			(type==4 && (orientation==0 || orientation==2 || orientation==3)) ||
			(type==5 && (orientation==0 || orientation==2 || orientation==3)) ||
			(type==6 && (orientation==0 || orientation==2 || orientation==3))){
			return targetX - centerX + 1;
		}
		else if(type==1 && orientation==0){
			return targetX - centerX + 2;
		}
		else if((type==1 && orientation==1) || (type==2 && orientation==1) ||
				(type==3 && orientation==1) || (type==4 && orientation==1) ||
				(type==5 && orientation==1) || (type==6 && orientation==1)){
			return targetX - centerX;
		}
		else return targetX - centerX + 1;
	}

	/**
	 * Gets the type of the current piece
	 * @return Type of the current piece
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the current piece
	 * @return Type of the current piece
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the stage at which AI is
	 * @return At which stage AI is
	 */
	public int getStep() {
		return step;
	}

	/**
	 * Sets the stage at which AI is
	 * @param step At which stage AI is
	 */
	public void setStep(int step) {
		this.step = step;
	}

	/**
	 * Sets the X coordinate of the center of the piece
	 * @param centerX The X coordinate of the center of the piece
	 */
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	/**
	 * Sets the Y coordinate of the center of the piece
	 * @param centerX The Y coordinate of the center of the piece
	 */
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	
}
