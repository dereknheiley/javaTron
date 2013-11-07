import java.awt.Color;

public class dumbAI extends Player {
	protected int berth = 10; // decrease this for sharper turns
	protected int tempX; // temporary x location
	protected int tempY; // temporary y location
	protected int random; // a random number

	public dumbAI(int x, int y, String dir, Color c) {
		super(x, y, dir, c);
	}
	
	public int getBerth() {
		return berth;
	}

	public void setBerth(int berth) {
		this.berth = berth;
	}

	public int getTempX() {
		return tempX;
	}

	public void setTempX(int tempX) {
		this.tempX = tempX;
	}

	public int getTempY() {
		return tempY;
	}

	public void setTempY(int tempY) {
		this.tempY = tempY;
	}

	// checks if there's a collision on current path
	public void checkFuture(Player p) {
		tempX = getCurrX();
		tempY = getCurrY();
		// depending on direction, project future location by berth number of
		// pixels
		if (isLeft())
			tempX -= berth;
		else if (isRight())
			tempX += berth;
		else if (isUp())
			tempY -= berth;
		else if (isDown())
			tempY += berth;
		// if future point collides with other player's current line
		if (collision(tempX, tempY, p.getLine()))
			setDirection();
		// if future point collides with this AI's stored lines
		for (Line l : getLines()) {
			if (collision(tempX, tempY, l))
				setDirection();
		}
		// if future point collides with other player's stored lines
		for (Line l : p.getLines()) {
			if (collision(tempX, tempY, l))
				setDirection();
		}
		// If player is beyond the height of the board
		if (tempY >= Board.HEIGHT) {
			setDirection();
		}

		// If the player is less than the minimum height of the game board
		if (tempY <= 0) {
			setDirection();
		}

		// If the player is beyond the width of the game board
		if (tempX >= Board.WIDTH) {
			setDirection();
		}

		// If the player is less than the minimum width of the game board
		if (tempX <= 0) {
			setDirection();
		}
	}
	public void setDirection(){
		if(isLeft() || isRight()){
			//cases when at a corner
			//when going down causes collision
			if(tempY+1 > Board.HEIGHT)
				setDirection("up");
			//when going up causes collision
			else if(tempY-1 < 0)
				setDirection("down");
			else
				setRandomDirection();
		}else if(isUp() || isDown()){
			//when going right causes collision
			if(tempX+1 > Board.WIDTH)
				setDirection("left");
			//when going left causes collision
			else if(tempX-1 < 0)
				setDirection("right");
			else
				setRandomDirection();
		}
	}
	// sets a random direction for AI
	public void setRandomDirection() {
		// choose a random number between 0 and 1
		random = (int) (Math.random() * 2);
		// change direction based on current direction and random number
		if (random == 0) {
			if (isLeft() || isRight()) {
				setDirection("up");
			} else if (isUp() || isDown()) {
				setDirection("right");
			}
		} else if (random == 1) {
			if (isLeft() || isRight()) {
				setDirection("down");
			} else if (isUp() || isDown()) {
				setDirection("left");
			}
		}
	}
}
