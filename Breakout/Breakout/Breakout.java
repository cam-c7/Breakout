/*
 * File: Breakout.java
 * -------------------
 * This file will eventually implement the game of Breakout.
 *
 * TODO: Update this file with a description of what your program
 * actually does!
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
//line 150: set y speed
public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 100;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	/** velocity trackers */
	private double vx, vy;

	private GRect paddle;

	public void run() {
		while(true) {
			GLabel title = new GLabel("BREAKOUT", 100, 100);
			title.setColor(new Color(0,0,0,0));
			title.setFont("IMPACT-80");
			add(title);
			title = new GLabel("BREAKOUT", (getWidth()-title.getWidth())/2, 100);
			title.setColor(Color.BLACK);
			title.setFont("IMPACT-80");
			add(title);
			GLabel playText = new GLabel("Click to play", 100, 100);
			playText.setColor(new Color(0,0,0,0));
			playText.setFont("IMPACT-50");
			add(playText);
			playText = new GLabel("Click to play", (getWidth()-playText.getWidth())/2, 150);
			playText.setColor(Color.GRAY);
			playText.setFont("IMPACT-50");
			add(playText);
			for(int i = 0; i < NBRICKS_PER_ROW; i++) {
				for(int j = 0; j < NBRICK_ROWS; j++) {
					//sets x and y coordinates
					double x = BRICK_WIDTH*i+BRICK_SEP*i+((WIDTH-NBRICKS_PER_ROW*BRICK_WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP)/2);
					double y = 200 + BRICK_HEIGHT*j+BRICK_SEP*j;
					//draws the boxes
					GRect box = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
					box.setFilled(true);
					if (j < NBRICK_ROWS/5) {
						box.setColor(Color.RED);
					} else if (j < 2*NBRICK_ROWS/5) {
						box.setColor(Color.ORANGE);
					} else if (j < 3*NBRICK_ROWS/5) {
						box.setColor(Color.YELLOW);
					} else if (j < 4*NBRICK_ROWS/5) {
						box.setColor(Color.GREEN);
					} else {
						box.setColor(Color.CYAN);
					}
					add(box);
				}
			}
			waitForClick();
			removeAll();
			RandomGenerator rgen = RandomGenerator.getInstance();
			//draw bricks
			for(int i = 0; i < NBRICKS_PER_ROW; i++) {
				for(int j = 0; j < NBRICK_ROWS; j++) {
					//sets x and y coordinates
					double x = BRICK_WIDTH*i+BRICK_SEP*i+((WIDTH-NBRICKS_PER_ROW*BRICK_WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP)/2);
					double y = BRICK_Y_OFFSET + BRICK_HEIGHT*j+BRICK_SEP*j;
					//draws the boxes
					GRect box = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
					box.setFilled(true);
					if (j < NBRICK_ROWS/5) {
						box.setColor(Color.RED);
					} else if (j < 2*NBRICK_ROWS/5) {
						box.setColor(Color.ORANGE);
					} else if (j < 3*NBRICK_ROWS/5) {
						box.setColor(Color.YELLOW);
					} else if (j < 4*NBRICK_ROWS/5) {
						box.setColor(Color.GREEN);
					} else {
						box.setColor(Color.CYAN);
					}
					add(box);
				}
			}
			//ball x and y values
			double ballX = getWidth()/2-BALL_RADIUS;
			double ballY = getHeight()/2-BALL_RADIUS;
			//control paddle
			addPaddle();
			addMouseListeners();
			//game
			int turns = NTURNS;
			int bricksLeft = NBRICK_ROWS*NBRICKS_PER_ROW;
			while(turns > 0) {
				GLabel turnText = new GLabel("Lives: " + turns, 300, 25);
				turnText.setColor(Color.BLACK);
				add(turnText);
				//draw ball in center
				GOval ball = new GOval(ballX, ballY, BALL_RADIUS*2, BALL_RADIUS*2);
				ball.setFilled(true);
				add(ball);
				waitForClick();

				//initialize two 
				vy = 3; //set y speed
				vx = rgen.nextDouble(1.0, 3.0);
				//bricks left
				if (rgen.nextBoolean(0.5)) vx = -vx;
				boolean canHitBrick = true;
				while(ball.getY()+BALL_RADIUS*2<getHeight() && bricksLeft > 0) {

					ball.move(vx, vy);
					if(ball.getX()<0 || ball.getX()+BALL_RADIUS*2 > getWidth()) {
						//when the ball his the walls speed is reversed
						vx = -vx;
						canHitBrick = true;
					}
					if(ball.getY()<0 /*|| ball.getY()+BALL_RADIUS*2 > getHeight()*/) {
						//when ball hits ceiling ball reverses y velocity
						vy = -vy;
						canHitBrick = true;
					}
					//tests for collision
					GObject collider = getCollidingObject(ball.getX(), ball.getY());
					if(collider == paddle && vy > 0) {
						//if collides w/ paddle and vy is down than switch
						vy=-vy;
						vx *= rgen.nextDouble(0.95, 1.05);
						canHitBrick = true;
					} else if (collider != null && collider != paddle && collider != turnText && canHitBrick == true) {
						//if collides with something else that isn't null(bricks), switch vy and subtract from bricks counter
						vy=-vy;
						remove(collider);
						bricksLeft--;
						canHitBrick = false;
					}
					pause(16);
				}
				//if exited while loop b/c ball too low
				if(ball.getY()+BALL_RADIUS*2>=getHeight()) {
					remove(ball);
					
					turns--;
				}
				else {
					//if exited while loop because bricksLeft = 0
					turns = -2;	
				}
				remove(turnText);
			} //end while turns > 0
			//game ending versions
			if(turns == 0) {
				//turns ran down to zero
				removeAll();
				GLabel overTitle = new GLabel("GAME OVER", 100, 100);
				overTitle.setColor(new Color(0,0,0,0));
				overTitle.setFont("IMPACT-80");
				add(overTitle);
				overTitle = new GLabel("GAME OVER", (getWidth()-overTitle.getWidth())/2, 100);
				overTitle.setColor(Color.BLACK);
				overTitle.setFont("IMPACT-80");
				add(overTitle);
				for(int i = 0; i < NBRICKS_PER_ROW; i++) {
					for(int j = 0; j < NBRICK_ROWS; j++) {
						//sets x and y coordinates
						double x = BRICK_WIDTH*i+BRICK_SEP*i+((WIDTH-NBRICKS_PER_ROW*BRICK_WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP)/2);
						double y = 200 + BRICK_HEIGHT*j+BRICK_SEP*j;
						//draws the boxes
						GRect box = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
						box.setFilled(true);
						if (j < NBRICK_ROWS/5) {
							box.setColor(Color.RED);
						} else if (j < 2*NBRICK_ROWS/5) {
							boolean RowColor = rgen.nextBoolean(0.8);
							if(RowColor == true) {
								box.setColor(Color.RED);
							} else {
								box.setColor(Color.ORANGE);
							}
						} else if (j < 3*NBRICK_ROWS/5) {
							boolean RowColor = rgen.nextBoolean(0.6);
							if(RowColor == true) {
								box.setColor(Color.RED);
							} else {
								box.setColor(Color.YELLOW);
							}
						} else if (j < 4*NBRICK_ROWS/5) {
							boolean RowColor = rgen.nextBoolean(0.4);
							if(RowColor == true) {
								box.setColor(Color.RED);
							} else {
								box.setColor(Color.GREEN);
							}
						} else {
							boolean RowColor = rgen.nextBoolean(0.2);
							if(RowColor == true) {
								box.setColor(Color.RED);
							} else {
								box.setColor(Color.CYAN);
							}
						}
						add(box);
					}
				}
			} else {
				//turns set to -2
				removeAll();
				GLabel wonTitle = new GLabel("GAME WON!", 100, 100);
				wonTitle.setColor(new Color(0,0,0,0));
				wonTitle.setFont("IMPACT-80");
				add(wonTitle);
				wonTitle = new GLabel("GAME WON!", (getWidth()-wonTitle.getWidth())/2, 100);
				wonTitle.setColor(Color.BLACK);
				wonTitle.setFont("IMPACT-80");
				add(wonTitle);
				for(int i = 0; i < NBRICKS_PER_ROW; i++) {
					for(int j = 0; j < NBRICK_ROWS; j++) {
						//sets x and y coordinates
						double x = BRICK_WIDTH*i+BRICK_SEP*i+((WIDTH-NBRICKS_PER_ROW*BRICK_WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP)/2);
						double y = 200 + BRICK_HEIGHT*j+BRICK_SEP*j;
						//draws the boxes
						GRect box = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
						box.setFilled(true);
						if (j < NBRICK_ROWS/5) {
							box.setColor(Color.RED);
						} else if (j < 2*NBRICK_ROWS/5) {
							box.setColor(Color.ORANGE);
						} else if (j < 3*NBRICK_ROWS/5) {
							box.setColor(Color.YELLOW);
						} else if (j < 4*NBRICK_ROWS/5) {
							box.setColor(Color.GREEN);
						} else {
							box.setColor(Color.CYAN);
						}
						add(box);
					}
				}
			}
			waitForClick();
			removeAll();
			turns = NTURNS;
		}
	} //ends the run

	//METHODS AND MOUSE CONTROLS
	//moving the paddle
	@Override public void mouseMoved(MouseEvent e) {
		double x = e.getX() - paddle.getWidth() / 2.0;
		//makes sure it doesn't run over the edge
		if(x<0) {
			x=0;	
		} else if(x + paddle.getWidth() > getWidth()) {
			x=getWidth()-paddle.getWidth();
		}
		paddle.setLocation(x, getHeight()-PADDLE_Y_OFFSET);
	} //ends mouse moved

	//add in paddle
	private void addPaddle() {
		paddle = new GRect(0, getHeight()-PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	} //ends add paddle
	//detecting collision
	private GObject getCollidingObject(double x, double y) {
		GObject object = getElementAt(x, y);
		if (object != null) {
			return(object);
		}
		object = getElementAt(x+2*BALL_RADIUS, y);
		if(object != null) {
			return(object);
		}
		object = getElementAt(x, y+2*BALL_RADIUS);
		if(object != null) {
			return(object);
		}
		object = getElementAt(x+2*BALL_RADIUS, y+2*BALL_RADIUS);
		if(object != null) {
			return(object);
		} else {
			return(null);
		}
	}
}