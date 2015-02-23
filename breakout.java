/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

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
  private static final int PADDLE_Y_OFFSET = 30;

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
  private static int NTURNS = 3;
  
/* Defines delay */
  private static final int DELAY = 10;
  
/* Private instance variable */
  private GRect paddle;

/* Private instance variable */
  private GOval ball; 
  
/* Sets X location of paddle */
  private int paddleX;
  
/* Sets Y location of paddle */
  private int paddleY;

  
/* Private instance variables - Generate Random Number */
  private RandomGenerator rgen = RandomGenerator.getInstance();
  
/* Declare velocity of ball */
  private double vx;
  private double vy = 3.0;
  
/* Create brick */
  private GRect[] bricks = new GRect[100];

/* Brick counter */
  private int brickCounter = 100;
  
/* Score */
  private int score;
  
/* Sets scoreBoard*/
  GLabel scoreBoard;
  
  
/* Method: run() */
/** Runs the Breakout program. */
  public void run () {
    setup();
    addMouseListeners();
    playGame();
    
    if (NTURNS == 0){
      GLabel printGameOver = new GLabel ("Game Over");
      printGameOver.setFont("Helvetica-24");
      double x = WIDTH / 2 - printGameOver.getWidth() / 2;
      double y = HEIGHT / 2;
      add(printGameOver, x, y);
    } else if (brickCounter == 0){
      GLabel gameOverWin = new GLabel ("YOU WIN!");
      gameOverWin.setFont("Helvetica-24");
      double x = WIDTH / 2 - gameOverWin.getWidth() / 2;
      double y = HEIGHT / 2;
      add(gameOverWin, x, y);
    }
  }
  
/* Sets up the start of the game*/
  public void setup() {
    addBricks();
    addPaddle();
    addBall();
    addScoreBoard();
  }
  
/* * Creates initial base of bricks */
  public void addBricks() {
    int rowWidth = NBRICKS_PER_ROW * BRICK_WIDTH + (NBRICKS_PER_ROW * BRICK_SEP);
    int horizontalMidPoint = getWidth() / 2;
    int startingPointX = horizontalMidPoint - rowWidth / 2;
    int startingPointY = BRICK_Y_OFFSET;
    int rowsRemaining = NBRICK_ROWS;
    int counter = 0;
    
    while (rowsRemaining > 0) {
      for (int i = 0; i < NBRICKS_PER_ROW; i++){
        bricks[counter] = new GRect(startingPointX, startingPointY, BRICK_WIDTH, BRICK_HEIGHT);
        startingPointX += BRICK_WIDTH + BRICK_SEP;
        bricks[counter].setFilled(true);
        if (counter < NBRICKS_PER_ROW * 2) {
          bricks[counter].setFillColor(Color.RED);
          add (bricks[counter]);
        } else if (counter < NBRICKS_PER_ROW * 4) {
          bricks[counter].setFillColor(Color.ORANGE);
          add (bricks[counter]);
        } else if (counter < NBRICKS_PER_ROW * 6) {
          bricks[counter].setFillColor(Color.YELLOW);
          add (bricks[counter]);
        } else if (counter < NBRICKS_PER_ROW * 8){
          bricks[counter].setFillColor(Color.GREEN);
          add(bricks[counter]);
        } else {
          bricks[counter].setFillColor(Color.CYAN);
          add(bricks[counter]);
        }
        counter ++;
      }
      rowsRemaining --;
      rowWidth = NBRICKS_PER_ROW * BRICK_WIDTH + (NBRICKS_PER_ROW * BRICK_SEP);
      startingPointX = horizontalMidPoint - rowWidth / 2;
      startingPointY = startingPointY + BRICK_HEIGHT + BRICK_SEP;
      
    }
  }

  
/* Creates paddle */
  public void addPaddle() {
    paddleX = getWidth() / 2 - PADDLE_WIDTH / 2 ;
    paddleY = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
    
    paddle = new GRect (paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
    paddle.setFilled(true);
    paddle.setColor(Color.BLACK);
    add(paddle);
  }

/* Follow mouse */
  public void mouseMoved(MouseEvent e) {
    if (e.getX() >= PADDLE_WIDTH /2 && e.getX() <= getWidth() - PADDLE_WIDTH /2 ) {
    paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
    }
  }

/* Adds ball */
  public void addBall() {
    ball = new GOval (getWidth() / 2 - BALL_RADIUS, getHeight() / 2 + BALL_RADIUS / 2, BALL_RADIUS, BALL_RADIUS);
    ball.setFilled(true);
    ball.setColor(Color.BLACK);
    add(ball);
  }
  
/* Adds Scoreboard */
  public void addScoreBoard() {
    scoreBoard = new GLabel ("Score: " +score);
    scoreBoard.setFont("Helvetica-24");
    double x = 0;
    double y = scoreBoard.getHeight();
    add(scoreBoard, x, y);
  }
  
/* Play Game */
  public void playGame(){
    // Set x coordinate
    vx = rgen.nextDouble(1.0, 3.0);
    if (rgen.nextBoolean(0.5)) vx = -vx;
    
    while (!gameOver()) {
      moveBall();
      checkForCollision();
      pause(DELAY);
    }
    
  }

/* Move ball */
  public void moveBall() {
    //set direction
    ball.move(vx,vy);
    
    
    //make ball bounce off the bottom
    if (ball.getY() > getHeight() - BALL_RADIUS * 2) {
      remove(ball);
      NTURNS --;
      pause(200);
      if (NTURNS > 0) {
        addBall();
        vy = 3.0;
      }
      else {
        return;
      }
        
    } 
    
    //make ball bounce off the right side
    if (ball.getX() > getWidth() - BALL_RADIUS * 2) {
      vx = -vx;
    }
    
    //make ball bounce off the left side
    if (ball.getX() < 0) {
      vx = -vx;
    }
    
    //make ball bounce off the top
    if (ball.getY() < 0){
      vy = -vy;
    }
  }
  
/* Check for collision */
  public void checkForCollision() {
    getCollidingObject();
  }

/* Checks to see if the ball collides with either the paddle or the ball */
  
  public void getCollidingObject() {
    GObject collLeft = getElementAt(ball.getX() - BALL_RADIUS, ball.getY());
    GObject collRight = getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
    GObject collTop = getElementAt(ball.getX(), ball.getY() - BALL_RADIUS);
    GObject collBottom = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
    
    // If ball hits paddle
    if (collBottom == paddle && vy > 0){
      vy = -vy;
      vy = vy - 0.25;
      return;
    }
    
    // If ball hits brick
    for (int i = 0; i < 100; i++) { 
      if (collTop == bricks [i]){
        vy = -vy;
        remove(bricks[i]);
        brickCounter--;
        score++;
        remove(scoreBoard);
        scoreBoard = new GLabel ("Score: " +score);
        scoreBoard.setFont("Helvetica-24");
        double x = 0;
        double y = scoreBoard.getHeight();
        add(scoreBoard, x, y);
      }
      else if (collBottom == bricks [i]){
        vy = -vy;
        remove(bricks[i]);
        brickCounter--;
        score++;
        remove(scoreBoard);
        scoreBoard = new GLabel ("Score: " +score);
        scoreBoard.setFont("Helvetica-24");
        double x = 0;
        double y = scoreBoard.getHeight();
        add(scoreBoard, x, y);
      }
      else if (collLeft == bricks[i]) {
        vx = -vx;
        remove(bricks[i]);
        brickCounter--;
        score++;
        remove(scoreBoard);
        scoreBoard = new GLabel ("Score: " +score);
        scoreBoard.setFont("Helvetica-24");
        double x = 0;
        double y = scoreBoard.getHeight();
        add(scoreBoard, x, y);
      } 
      else if (collRight == bricks [i]){
        vx = -vx;
        remove(bricks[i]);
        brickCounter--;
        score++;
        remove(scoreBoard);
        scoreBoard = new GLabel ("Score: " +score);
        scoreBoard.setFont("Helvetica-24");
        double x = 0;
        double y = scoreBoard.getHeight();
        add(scoreBoard, x, y);
      }
    }
  }
  
 
/* Game over method */
  public boolean gameOver() {
    if (NTURNS == 0 || brickCounter == 0) {
      return true;
    } 
    else {
      return false;
    }
  }
  
}

