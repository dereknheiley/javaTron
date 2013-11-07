 import java.awt.Color;

public class betterAI extends dumbAI {

	public betterAI(int x, int y, String dir, Color c) {
		super(x, y, dir, c);
	}

	public void checkFuture(Player p) {
		berth = 5;
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
			setDirection(p);
		// if future point collides with this AI's stored lines
		for (Line l : getLines()) {
			if (collision(tempX, tempY, l))
				setDirection(p);
		}
		// if future point collides with other player's stored lines
		for (Line l : p.getLines()) {
			if (collision(tempX, tempY, l))
				setDirection(p);
		}
		// If player is beyond the height of the board
		if (tempY >= Board.HEIGHT) {
			setDirection(p);
		}

		// If the player is less than the minimum height of the game board
		if (tempY <= 0) {
			setDirection(p);
		}

		// If the player is beyond the width of the game board
		if (tempX >= Board.WIDTH) {
			setDirection(p);
		}

		// If the player is less than the minimum width of the game board
		if (tempX <= 0) {
			setDirection();
		}
	}

	public void setDirection(Player p) {
		// scan to 100 pixels
		// take smallest collision distance in both up and down directions
		// take the larger of the up and down distances ad move that way
		int scan = 100;
		int scanDown = 1000; // arbitrary large number to check against
		int scanUp = 1000;
		int scanLeft = 1000;
		int scanRight = 1000;
		if (isLeft() || isRight()) {
			// cases when at a corner
			if (tempY + 1 > Board.HEIGHT)
				setDirection("up");
			else if (tempY - 1 < 0)
				setDirection("down");
			else {
				for (int i = 0; i < scan; i++) {
					// if a collision happens within 100 pixels if AI is to turn
					// 'down'
					for (Line l : getLines()) {
						if (collision(tempX, tempY + i, l)) {
							if (i < scanDown) {
								scanDown = i;
							}
						}
					}
				}
				// if a collision happens within 100 pixels if AI is to turn
				// 'up'
				for (int i = 0; i < scan; i++) {
					for (Line l : getLines()) {
						if (collision(tempX, tempY - i, l)) {
							if (i < scanUp) {
								scanUp = i;
							}
						}
					}
				}
				/*
				  if (collision(tempX, tempY + i,
				 * p.getLine())) { // count the distance from the collision if
				 * (i < scanDown) { scanDown = i; } } if (collision(tempX, tempY
				 * - i, p.getLine())) { // count the distance from the collision
				 * if (i < scanUp) { scanUp = i; break; // break away when
				 * there's a collision } }
				 */
				for (int i = 0; i < scan; i++) {
					for (Line l : p.getLines()) { 
						if (collision(tempX, tempY+i, l)) { 
							if (i < scanDown) { 
								scanDown = i;  
							}
						}
					}
				}
				for (int i = 0; i < scan; i++) {
					for (Line l : p.getLines()) { 
						if (collision(tempX, tempY-i, l)) { 
							if (i < scanUp) { 
								scanUp = i; 
							}
						}
					}
				}
				for (int i = 0; i < scan; i++) {		 
					if (collision(tempX, tempY+i, p.getLine())) { 
						if (i < scanDown) { 
							scanDown = i;
						}
					}
				}
				for (int i = 0; i < scan; i++) {				
					if (collision(tempX, tempY-i, p.getLine())) { 
						if (i < scanUp) { 
							scanUp = i; 
						}
					}
				}
				if (scanUp > scanDown)
					setDirection("up");
				else if (scanUp < scanDown)
					setDirection("down");
				else
					setRandomDirection();
			}

		} else if (isUp() || isDown()) {
			if (tempX + 1 > Board.WIDTH)
				setDirection("left");
			else if (tempX - 1 < 0)
				setDirection("right");
			else {
				for (int i = 0; i < scan; i++) {
					// if a collision happens within 100 pixels if AI is to turn
					// 'right'
					for (Line l : getLines()) {
						if (collision(tempX + i, tempY, l)) {
							if (i < scanRight) {
								scanRight = i;
							}
						}
					}
				}
				// if a collision happens within 100 pixels if AI is to turn
				// 'left'
				for (int i = 0; i < scan; i++) {
					for (Line l : getLines()) {
						if (collision(tempX - i, tempY, l)) {
							if (i < scanLeft) {
								scanLeft = i;
							}
						}
					}
				}
				for (int i = 0; i < scan; i++) {
					for (Line l : p.getLines()) { 
						if (collision(tempX+i, tempY, l)) { 
							if (i < scanRight) { 
								scanRight = i;  
							}
						}
					}
				}
				for (int i = 0; i < scan; i++) {
					for (Line l : p.getLines()) { 
						if (collision(tempX-i, tempY, l)) { 
							if (i < scanLeft) { 
								scanLeft = i; 
							}
						}
					}
				}
				for (int i = 0; i < scan; i++) {		 
					if (collision(tempX+i, tempY, p.getLine())) { 
						if (i < scanRight) { 
							scanRight = i;
						}
					}
				}
				for (int i = 0; i < scan; i++) {				
					if (collision(tempX-i, tempY, p.getLine())) { 
						if (i < scanLeft) { 
							scanLeft = i; 
						}
					}
				}
				 
				if (scanLeft > scanRight)
					setDirection("left");
				else if (scanLeft < scanRight)
					setDirection("right");
				else
					setRandomDirection();
			}
		}
	}
}
