
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Main class to run.
 */
public class Game implements Runnable {
	
	private static final String GAME_INSTRUCTIONS = "Welcome to Swipe Brick Breaker!\n\n" +
			"DESCRIPTION:\n" +
			"Swipe Brick Breaker is a version of the traditional brick breaker game.\n" + 
			"In Swipe Brick Breaker, rather than having one ball, a player maintains a series of balls, collected by\n" + 
			"gathering coins. There is also no paddle. Rather, in the beginning of each round, the player positions\n" + 
			"and clicks the mouse to set the angle of release of these balls. The round ends when all the balls\n" + 
			"return to the bottom wall, after bouncing around destroying the blocks. With each new round, the grid\n" + 
			"rows shifts down by one, and the top row gets populated with new blocks and coins. If the bottom row\n" + 
			"is not clear by the end of the round, it is game-over. Score increments with each round you get to.\n" +
			"The goal of the game is to destroy blocks to get to the furthest round you can before the blocks catch up to you!\n\n"+
			"HOW TO PLAY:\n"+
			"- Move your mouse cursor across the game-court region to position the ball(s) line of trajectory.\n"+
			"- Click your mouse once to release the ball(s) in the specified direction.\n"+
			"- Destroy as many blocks as you can and make sure they do not catch up to you.\n"+
			"- Collect coins along the way to increase the number of balls you have.\n"+
			"- Once the balls come back to the floor, repeat this process until the blocks catch up to you (game-over)!\n"+
			"- Once game-over, enter the requested details. If your score is in the top 3 scores, it will be saved!\n\n"+
			"NOTES:\n"+
			"- The game gets progressively harder. With every 5 rounds, the generated blocks become harder to defeat.\n"+
			"- If the SAME user gets the SAME highscore more than once, it will only show up once in the highscore board\n"+
			"   under that user. Example: If sahitpen scores 100 two different times in the game, the highscore board\n"+
			"   will only show SCORE 100 - sahitpen one time, not twice. This is to prevent hogging of the highscore board.\n"+ 
			"- The starting position of the balls on each round is determined by where the master-ball (the first ball in\n"+
			"   the list) lands.";
	
    public void run() {

        final JFrame frame = new JFrame("Swipe Brick Breaker");
        frame.setLocation(300, 300);
        
        // Create Reset button
        final JPanel control_panel = new JPanel();
        control_panel.setBorder(new EmptyBorder(50, 0, 0, 0));
        control_panel.setBackground(ColorPalate.GRAY);

        // Create Status panel
        final JPanel status_panel = new JPanel();
        status_panel.setBackground(ColorPalate.GRAY);
        final JLabel score = new JLabel("0");
        score.setForeground(Color.WHITE);
        score.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        status_panel.add(score);

        //Create highscore parser
        HighScoreParser parser = new HighScoreParser("files/highscores.txt");
        // Create Main playing area
        final PlayingArea court = new PlayingArea(score, parser);
        frame.add(court, BorderLayout.CENTER);
        
        //Add panels
        frame.add(control_panel, BorderLayout.SOUTH);
        frame.add(court, BorderLayout.CENTER);
        frame.add(status_panel, BorderLayout.NORTH);


        final JButton reset = new JButton("Reset");
        reset.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        
        final JButton instructions = new JButton("Instructions");
        instructions.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        instructions.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(frame, GAME_INSTRUCTIONS, "Instructions", JOptionPane.PLAIN_MESSAGE);
        	}
        });
        
        final JButton highscores = new JButton("Highscores"); 
        highscores.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        highscores.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String topScores = "";
        		TreeSet<Scorer> highscores = parser.getHighScores();
        		int size = highscores.size();
        		for (int i = 0; i < size; i++) {
        			topScores += i+1 + ") " +highscores.pollFirst().toString() +"\n";
        		}
        		if (topScores.equals("")){
        			topScores = "There are no scores yet! Play the game to add a score.";
        		}
        		JOptionPane.showMessageDialog(frame, topScores, "High Scores", JOptionPane.PLAIN_MESSAGE);
        	}
        });
        
        highscores.setBackground(ColorPalate.LIGHT_GRAY);
        highscores.setForeground(Color.WHITE);
        highscores.setOpaque(true);
        highscores.setBorderPainted(false);
        
        reset.setBackground(ColorPalate.LIGHT_GRAY);
        reset.setForeground(Color.WHITE);
        reset.setOpaque(true);
        reset.setBorderPainted(false);
        
        
        instructions.setBackground(ColorPalate.LIGHT_GRAY);
        instructions.setForeground(Color.WHITE);
        instructions.setOpaque(true);
        instructions.setBorderPainted(false);
         
        control_panel.add(instructions);
        control_panel.add(reset);
        control_panel.add(highscores);

        // Put the frame on the screen
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method that starts the game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
