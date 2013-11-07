import javax.imageio.ImageIO;// Imported to use background file -Mike
import javax.swing.*;
import java.io.*; // Imported to read background image file -Mike
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

	// Variables used for board
	public static int WIDTH;
	public static int HEIGHT;

	private Player p1;
	private Player p2;
	private dumbAI a1;
	private betterAI a2;

	private GameConfig config;
	private Timer timer;
	private Image img;

	// Class Constructor
	// Initializes user interface and class variables
	// adds players
	public Board(int width, int height, GameConfig config) throws IOException {

		this.config = config;

		setSize(width, height);

		p1 = new Player(750, 400, "left", Color.blue);
		p2 = new Player(50, 50, "right", Color.red);
		a1 = new dumbAI(50, 400, "right", Color.orange);
		a2 = new betterAI(50, 400, "right", Color.cyan);
		addKeyListener(new TAdapter());

		img = ImageIO.read(new File("background.jpg")); // This is where the
														// background is made;

		setFocusable(true);
		if (config.speed == 1){
			timer = new Timer(10, this);
			timer.start();
		} else {
			timer = new Timer(4, this);
			timer.start();
		}

		WIDTH = getSize().width;
		HEIGHT = getSize().height;
	}

	// Method used to draw game components
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, 0, null);

		if (config.players == 1) {
			drawOnePlayer(g);
		} else if (config.players == 2) {
			drawTwoPlayer(g);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void drawTwoPlayer(Graphics g) {
		if (p1.inGame() && p2.inGame()) {
			drawPlayer(g, p1);
			drawPlayer(g, p2);
		} else {
			twoPLayerGameOver(g);
		}
	}

	public void drawOnePlayer(Graphics g) {
		if (config.difficulty == 1) {
			if (p1.inGame() && a1.inGame()) {
				drawPlayer(g, p1);
				drawPlayer(g, a1);
			} else {
				onePLayerGameOver(g);
			}
		} else {
			if (p1.inGame() && a2.inGame()) {
				drawPlayer(g, p1);
				drawPlayer(g, a2);
			} else {
				onePLayerGameOver(g);
			}
		}
	}

	// TODO: make a one player specific ending
	public void onePLayerGameOver(Graphics g) {
		String msg = "";

		// Display which player has won the game
		// UPDATE A tie now possible if head on collision -T
		if (config.difficulty == 1) {
			if (!p1.inGame() && !a1.inGame()) {
				msg = "Game Over, both players lose!";
			} else if (!p1.inGame()) {
				msg = "Game Over, the Machine wins!";
			} else if (!a1.inGame()) {
				msg = "Game Over, the Human wins!";
			}
		}
		else{
			if (!p1.inGame() && !a2.inGame()) {
				msg = "Game Over, both players lose!";
			} else if (!p1.inGame()) {
				msg = "Game Over, the Machine wins!";
			} else if (!a2.inGame()) {
				msg = "Game Over, the Human wins!";
			}
		}

		// Chooses a font and displays the winner
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.red);
		g.setFont(small);
		g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2);
	}

	// TODO: make a two player specific message
	public void twoPLayerGameOver(Graphics g) {
		String msg = "";

		// Display which player has won the game
		// UPDATE A tie now possible if head on collision -T
		if (!p1.inGame() && !p2.inGame()) {
			msg = "Game Over, both players lose!";
		} else if (!p1.inGame()) {
			msg = "Game Over, Player 2 Wins!";
		} else if (!p2.inGame()) {
			msg = "Game Over, Player 1 Wins!";
		}

		// Chooses a font and displays the winner
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.red);
		g.setFont(small);
		g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2);
	}

	// used to draw player and its tail
	public void drawPlayer(Graphics g, Player p) {
		Graphics2D g2 = (Graphics2D) g;
		// UPDATE: draw player's current line -T
		g.setColor(p.getColor());
		g2.setStroke(new BasicStroke(4)); // pen size thicker
		g.drawLine(p.getPreviousX(), p.getPreviousY(), p.getCurrX(),
				p.getCurrY()); // draws current line

		// UPDATE: this draws previously stored lines, i.e. tail -T
		for (Line l : p.getLines()) {
			g.setColor(p.getColor());
			g.drawLine(l.x1, l.y1, l.x2, l.y2);
		}

	}

	
	public void actionPerformed(ActionEvent e) {

		if (config.players == 2) {
			runTwoPlayers();
		} else if (config.players == 1) {
			runOnePlayer();
		} else {
			runTestAll();
		}

		repaint();// Re-draw the scene
	}

	public void runOnePlayer() {
		if (config.difficulty == 1) {
			if (p1.inGame() && a1.inGame()) {

				p1.checkCollision(a1);
				a1.checkCollision(p1);
				p1.move();
				a1.checkFuture(p1);
				a1.move();

			}
		}
		else{
			if (p1.inGame() && a2.inGame()) {

				p1.checkCollision(a2);
				a2.checkCollision(p1);
				p1.move();
				a2.checkFuture(p1);
				a2.move();

			}
		}
	}

	public void runTestAll() {
		if (p1.inGame() && p2.inGame() && a1.inGame()) {

			p1.checkCollision(p2);
			p1.checkCollision(a1);

			p2.checkCollision(p1);
			p2.checkCollision(a1);

			a1.checkCollision(p1);
			a1.checkCollision(p2);
			p1.move();
			p2.move();

			a1.checkFuture(p1);
			a1.checkFuture(p2);
			a1.move();

		}
	}

	public void runTwoPlayers() {
		if (p1.inGame() && p2.inGame()) {

			p1.checkCollision(p2);
			p2.checkCollision(p1);

			p1.move();
			p2.move();
		}
	}

	// Adapter class used for keyboard button presses
	private class TAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			// PLAYER 1
			// If the left key is pressed and the player is not currently moving
			// right then make the player move left
			if ((key == KeyEvent.VK_A) && (!p2.isRight())) {
				p2.setDirection("left");
			}

			// If the right key is pressed and the player is not currently
			// moving left then make the player move right
			if ((key == KeyEvent.VK_D) && (!p2.isLeft())) {
				p2.setDirection("right");
			}

			// If the up key is pressed and the player is not currently moving
			// down then make the player move up
			if ((key == KeyEvent.VK_W) && (!p2.isDown())) {
				p2.setDirection("up");
			}

			// If the down key is pressed and the player is not currently moving
			// up then make the player move down
			if ((key == KeyEvent.VK_S) && (!p2.isUp())) {
				p2.setDirection("down");
			}

			// PLAYER 2
			// If the left key is pressed and the player is not currently moving
			// right then make the player move left
			if ((key == KeyEvent.VK_LEFT) && (!p1.isRight())) {
				p1.setDirection("left");
			}

			// If the right key is pressed and the player is not currently
			// moving left then make the player move right
			if ((key == KeyEvent.VK_RIGHT) && (!p1.isLeft())) {
				p1.setDirection("right");
			}

			// If the up key is pressed and the player is not currently moving
			// down then make the player move up
			if ((key == KeyEvent.VK_UP) && (!p1.isDown())) {
				p1.setDirection("up");
			}

			// If the down key is pressed and the player is not currently moving
			// up then make the player move down
			if ((key == KeyEvent.VK_DOWN) && (!p1.isUp())) {
				p1.setDirection("down");
			}
		}
	}
}