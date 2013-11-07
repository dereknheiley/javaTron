import java.util.ArrayList;
import java.awt.Color;

public class Player {
	private ArrayList<Line> lines = new ArrayList<Line>(); // stores lines
	// variables to draw line
	// starting point
	private int previousX;
	private int previousY;
	// end point
	private int currX;
	private int currY;

	private Color color; // color of the bike
	// variables for direction
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;

	private boolean inGame = true; // whether player is still in game
	private boolean changeDir = false; // whether player changed direction

	public Player(int x, int y, String dir, Color c) {
		// coordinates of starting position
		previousX = x;
		previousY = y;
		currX = x;
		currY = y;
		// color of bike
		color = c;
		// direction of player
		setDirection(dir);
	}

	public int getPreviousX() {
		return previousX;
	}

	public void setPreviousX(int previousX) {
		this.previousX = previousX;
	}

	public int getPreviousY() {
		return previousY;
	}

	public void setPreviousY(int previousY) {
		this.previousY = previousY;
	}

	public int getCurrX() {
		return currX;
	}

	public void setCurrX(int currX) {
		this.currX = currX;
	}

	public int getCurrY() {
		return currY;
	}

	public void setCurrY(int currY) {
		this.currY = currY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}
	public Line getLine(){
		return new Line(previousX, previousY, currX, currY);
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean inGame() {
		return inGame;
	}

	// method for changing player's direction
	public void setDirection(String dir) {
		if (dir.equalsIgnoreCase("up")) {
			up = true;
			down = false;
			left = false;
			right = false;
		} else if (dir.equalsIgnoreCase("down")) {
			up = false;
			down = true;
			left = false;
			right = false;
		} else if (dir.equalsIgnoreCase("left")) {
			up = false;
			down = false;
			left = true;
			right = false;
		} else if (dir.equalsIgnoreCase("right")) {
			up = false;
			down = false;
			left = false;
			right = true;
		}
		// changeDir set to true when direction changes
		changeDir = true;
	}

	public void move() {
		// if player changed direction, store current line
		if (changeDir) {
			changeDir = false;
			// add line to ArrayList
			lines.add(new Line(previousX, previousY, currX, currY));
			// current end point becomes new starting point
			previousX = currX;
			previousY = currY;
		} else {
			// If traveling left, continue to do so
			if (left) {
				currX -= 1;
			}

			// If traveling right, continue to do so
			if (right) {
				currX += 1;
			}

			// If traveling up, continue to do so
			if (up) {
				currY -= 1;
			}

			// If traveling down, continue to do so
			if (down) {
				currY += 1;
			}
		}
	}

	public void checkCollision(Player p) {
		// Player p's current line
		Line otherLine = p.getLine();
		// checks if this player's current point collides with p's current line
		if (collision(currX, currY, otherLine))
			inGame = false;
		// checks if this player collides with this player's stored lines
		for (Line l : lines) {
			if (collision(currX, currY, l))
				inGame = false;
		}
		// checks if this player collides with p's stored lines
		for (Line l : p.getLines()) {
			if (collision(currX, currY, l))
				inGame = false;
		}

		// If the player is beyond the height of the game board
		if (currY > Board.HEIGHT) {
			inGame = false;
		}

		// If the player is less than the minimum height of the game board
		if (currY < 0) {
			inGame = false;
		}

		// If the player is beyond the width of the game board
		if (currX > Board.WIDTH) {
			inGame = false;
		}

		// If the player is less than the minimum width of the game board
		if (currX < 0) {
			inGame = false;
		}
	}

	// method for checking collision
	public boolean collision(int x, int y, Line line) {

		// takes the minimum x and y of given line
		int minX = Math.min(line.x1, line.x2);
		int minY = Math.min(line.y1, line.y2);
		// takes the maximum x and y of given line
		int maxX = Math.max(line.x1, line.x2);
		int maxY = Math.max(line.y1, line.y2);

		boolean collides = false;

		// check if point x,y falls in given line
		// UPDATE: test for collision on vertical line segment -T
		if (x == line.x1) {
			if (y < maxY && y > minY) {
				collides = true;
			}
		}

		// UPDATE: test for collision on horizontal line segment -T
		if (y == line.y1) {
			if (x < maxX && x > minX) {
				collides = true;
			}
		}

		// UPDATE: ignore collisions with end points -T
		if ((x == line.x1 && y == line.y1) || (x == line.x1 && y == line.y1)) {
			collides = false;
		}

		return collides;
	}
}
