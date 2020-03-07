import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import java.util.*;

public class PlayingArea extends JPanel {
	
    // Update interval for timer, in milliseconds
	private static final int INTERVAL = 10;
	//speed of the ball
	private static final double SPEED = 7;
    private static final int BALL_RADIUS = 12;

	private BlockGrid grid;
	private LinkedList<PlayerBallSprite> balls;
	private PreviewLine line;
	private PlayerBallCounterSprite ballCounter;
	private HighScoreParser parser;
    private JLabel score; // Current user score
    public boolean playing = false; // whether the game is running 
    public boolean launched = false; // whether the ball has been launched  
    private Timer timer;
	private int ballsCount; //counter for the total number of balls collected

	public PlayingArea(JLabel score, HighScoreParser parser) {
        this.score = score;
        this.parser = parser;
        
		reset();
	    // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(ColorPalate.DARK_GRAY);

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        //draw preview line
        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		if (line.getY2() >= balls.getFirst().getY() - 15) {
        			return;
        		}
        		if (!launched) {
        			//set overall release here
        			launched = true;
        			//sets release of each ball with small delay
        			setLaunchReadyWithDelay();
        			
        			//calculate vx and vy based on mouse release position
        			double angle = Math.atan2(line.getY2() - balls.getFirst().getY(), line.getX2() - balls.getFirst().getX());
        			double vx = Math.round(SPEED*Math.cos(angle));
        			double vy = Math.round(SPEED*Math.sin(angle));
    			
        			for (PlayerBallSprite ball : balls) {        				
        				ball.setActive(true);
            			ball.setVx((int)vx);
            			ball.setVy((int)vy);
        			}
        		}
        	}
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
        	public void mouseMoved(MouseEvent e) {
        		if (!launched) {
        			int mouseX = (int) e.getPoint().getX();
        			int mouseY = (int) e.getPoint().getY();
        			line.setX2(mouseX);
        			line.setY2(mouseY);
            		repaint();
        		}
            }
        });    
		timer.start();

	}
	
	private void setLaunchReadyWithDelay() {
		java.util.Timer launchTimer = new java.util.Timer();
		launchTimer.schedule(new TimerTask() {
			ArrayList<PlayerBallSprite> temp = new ArrayList<PlayerBallSprite>(balls);
			Iterator<PlayerBallSprite> iter = temp.iterator();
			PlayerBallSprite ball = iter.next();
			@Override
			public void run() {
				ball.setLaunchReady(true);
				if (iter.hasNext()) {
					ball = iter.next();
				} else {
					launchTimer.cancel();
				}
			}
		}, 0, 100);
	}
		
	public int getBallCount() {
		return ballsCount;
	}
	
	public void collideWithCoin()	{
		ballsCount += 1;
	}
	
	public void collideWithBlock(PlayerBallSprite ball, NumBlockSprite block) {
		ball.bounce(ball.hitObjDirection(block));
	}
	
	private void launch(PlayerBallSprite ball) {
		//check if the game is currently running (not game-over) and if the ball is active
		if (playing && ball.isActive()) {
			
			ball.move();
			ball.bounce(ball.hitWallDirection());
			
			for (GameSprite[] row : grid.getGrid()) {
				for (int i = 0; i < row.length; i++) {
					GameSprite sprite = row[i];
					if (ball.collidesWith(sprite)) {
						//check if collision with num block 
						if (sprite instanceof NumBlockSprite) {
							NumBlockSprite block = (NumBlockSprite) sprite;
							//reduce health of num block
							block.onCollision(ball);
							//bounce the player ball
							collideWithBlock(ball, block);
							//replace block with empty sprite if health becomes 0
							if (block.isReadyToBeDestroyed()) {
								row[i] = new EmptySprite(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
							}
						} else if (sprite instanceof CoinSprite) {
							CoinSprite coin = (CoinSprite) sprite;
							//destroy coin
							coin.onCollision(ball);
							//increase the number of balls
							collideWithCoin();
							//replace coin with empty sprite
							row[i] = new EmptySprite(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
						}
					}
				}
			}
			
			//stop the ball from moving if it hits the floor
			if (ball.hitsFloor()) {
				ball.setActive(false);
				ball.setLaunchReady(false);
			}
			
			//check if any balls are still active
			//if so exit this function
			//if all balls are inactive, the code afterwards gets run
			for (PlayerBallSprite b : balls) {
				if (b.isActive()) {
					repaint();
					return;
				} 
			}
			
			//set launched back to false
		    launched = false;
			//go to the next round
			grid.nextRound();
				
		    //add the newly collected balls to the balls data structure 
			int numberBallsToAdd = ballsCount - balls.size();
			while (numberBallsToAdd > 0) {
				//add ball to player ball list
				int px = balls.getFirst().getX();
				int py = balls.getLast().getY();
				balls.add(new PlayerBallSprite(px, py, BALL_RADIUS, getWidth(), getHeight()));
				numberBallsToAdd--;
			}
			
			//set all the balls positions to match the first balls position
			for (PlayerBallSprite b : balls) {
				b.setX(balls.getFirst().getX());
			}
				
			repaint();
			
			//check game-over condition
			if (grid.isGameOver()) {
				playing = false;
				
				//show user score
				int gameScore = Integer.parseInt(score.getText());

				String first = makeValidName(JOptionPane.showInputDialog(this, "Enter your first name", "Save Score", JOptionPane.QUESTION_MESSAGE));
				String last = makeValidName(JOptionPane.showInputDialog(this, "Enter your last name", "Save Score", JOptionPane.QUESTION_MESSAGE));
				String pennkey = makeValidName(JOptionPane.showInputDialog(this, "Enter your pennkey", "Save Score", JOptionPane.QUESTION_MESSAGE));
								
				if (first != null && last != null && pennkey != null) {
					TreeSet<Scorer> currentScores = parser.getHighScores();
					Scorer newScorer = new Scorer(first, last, pennkey, gameScore);
					if (currentScores.contains(newScorer)) {
						JOptionPane.showMessageDialog(this, "You already achieved this score before, so it is already in the Highscores list!", "Nice Job But...!", JOptionPane.PLAIN_MESSAGE);
					} else if (parser.writeHighScore(newScorer)) {
						JOptionPane.showMessageDialog(this, "Your score was saved as a highscore! Use the Highscores menu button to view your score.", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Sorry, your score was not a highscore. Better luck next time!", "Play Again", JOptionPane.PLAIN_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(this, "One or more requested inputs was not given. Score could not be saved.");
				}
				reset();
			}
		}
	}
	
	//trims the  whitespace  and removes any /t characters
	private String makeValidName(String n) {
		if (n != null) {
			n = n.trim();
			n = n.replace("\t", "");
			if (n.equals("")) {
				return null;
			}
		}
		return n;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);  
		grid.paintComponent(g);
		for (PlayerBallSprite ball : balls) {
			ball.draw(g);
		}
		//only draw preview line if the balls are not already launched
		if (!launched) {
			//draw preview line
			int lineX1 = balls.getFirst().getX() + balls.getFirst().getWidth()/2;
			int lineY1 = balls.getFirst().getY() + balls.getFirst().getHeight()/2;;
			line.setX1(lineX1);
			line.setY1(lineY1);
			//make sure that the preview line can't be draw perfectly horizontal 
			//to prevent the ball from bouncing in an infinite loop
			if (line.getY2() < balls.getFirst().getY() - 15) {
				line.paintComponent(g);
			}
			//draw ball count label
			ballCounter.draw(g);
		}
	}

	@Override
    public Dimension getPreferredSize() {
	    return new Dimension(grid.getWidth(), grid.getHeight()+2); //the 2 acts as slight padding
	}
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {

        score.setText("0");
;    	grid = new BlockGrid(score);

		//x,y position of initial ball
		int px = grid.getWidth()/2-BALL_RADIUS/2;
		int py = grid.getHeight() - BALL_RADIUS;
		balls = new LinkedList<PlayerBallSprite>();
		PlayerBallSprite ball = new PlayerBallSprite(px, py, BALL_RADIUS, getWidth(), getHeight());
		balls.add(ball);  
		
		ballsCount = 1;
		
		ballCounter = new PlayerBallCounterSprite(balls);
		line = new PreviewLine();
		
        playing = true;
        launched = false;
        
        
		timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent a) {
					ArrayList<PlayerBallSprite> temp = new ArrayList<PlayerBallSprite>(balls);
					
					for (PlayerBallSprite ball : temp) {
						if (ball.isLaunchReady()) {
							launch(ball);
						}
					}
				
			}
		});   
        // Make sure that this component has the keyboard focus
        repaint();
        requestFocusInWindow();
    }

}
