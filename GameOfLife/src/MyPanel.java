import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {

	final static int BOARD_LENGTH = 50;
	final static int BOARD_HEIGHT = 50;
	final static int PANEL_WIDTH = 500;
	final static int PANEL_HEIGHT = 500;
	static boolean[][] board;
	Timer timer;
	final static int DELAY = 10;

	MyPanel() {
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBackground(Color.black);
		timer = new Timer(DELAY, this);
		timer.start();
		board = randBoard(BOARD_LENGTH, BOARD_HEIGHT);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		for (int x = 0; x < BOARD_LENGTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				if(board[x][y])
					g2D.fillRect(x * PANEL_WIDTH / BOARD_LENGTH, y * PANEL_HEIGHT / BOARD_HEIGHT,
						PANEL_WIDTH / BOARD_LENGTH, PANEL_HEIGHT / BOARD_HEIGHT);
				g2D.setColor(Color.white);
				g2D.drawRect(x * PANEL_WIDTH / BOARD_LENGTH, y * PANEL_HEIGHT / BOARD_HEIGHT,
						PANEL_WIDTH / BOARD_LENGTH, PANEL_HEIGHT / BOARD_HEIGHT);
				g2D.setColor(Color.gray);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		calcBoard();
		repaint();
	}

	static void calcBoard() {
		int[][] boardInt = new int[BOARD_LENGTH][BOARD_HEIGHT];
		boolean[][] boardBool = board;
		// boardInt represents the number of live nearby cells per (x,y)
		for (int x = 0; x < BOARD_LENGTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				boardInt[x][y] = 0;
			}
		}
		for (int x = 0; x < BOARD_LENGTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				if (boardBool[x][y]) {
					boardInt[wrap(x, -1, 0, BOARD_LENGTH)][wrap(y, -1, 0, BOARD_HEIGHT)] += 1;
					boardInt[wrap(x, -1, 0, BOARD_LENGTH)][y] += 1;
					boardInt[wrap(x, -1, 0, BOARD_LENGTH)][wrap(y, 1, 0, BOARD_HEIGHT)] += 1;
					boardInt[x][wrap(y, -1, 0, BOARD_HEIGHT)] += 1;
					boardInt[x][wrap(y, 1, 0, BOARD_HEIGHT)] += 1;
					boardInt[wrap(x, 1, 0, BOARD_LENGTH)][wrap(y, -1, 0, BOARD_HEIGHT)] += 1;
					boardInt[wrap(x, 1, 0, BOARD_LENGTH)][y] += 1;
					boardInt[wrap(x, 1, 0, BOARD_LENGTH)][wrap(y, 1, 0, BOARD_HEIGHT)] += 1;
				}
			}
		}
		for (int x = 0; x < BOARD_LENGTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				if (!boardBool[x][y] && boardInt[x][y] == 3) {
					boardBool[x][y] = true;
				} else if (boardBool[x][y]) {
					if (boardInt[x][y] != 2 && boardInt[x][y] != 3) {
						boardBool[x][y] = false;
					}
				}
			}
		}
		board = boardBool;
	}

	static int wrap(int n, int x, int min, int max) {
		int num = n + x;
		if (num < min)
			num = min;
		else if (num > max - 1)
			num = max - 1;
		return num;
	}

	static boolean[][] randBoard(int length, int height) {
		boolean[][] board = new boolean[height][length];
		Random random = new Random();

		for (int x = 0; x < height; x++)
			for (int y = 0; y < length; y++)
				board[x][y] = random.nextBoolean();
		return board;
	}
}
