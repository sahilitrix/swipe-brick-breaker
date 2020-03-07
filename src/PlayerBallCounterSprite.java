
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

public class PlayerBallCounterSprite {
	
	private LinkedList<PlayerBallSprite> balls;
	private int maxX;

	public PlayerBallCounterSprite(LinkedList<PlayerBallSprite> balls) {
		this.balls = balls;
		this.maxX = balls.get(0).getMaxX();
	}

	public void draw(Graphics g) {
		String ballCount = "x" + balls.size();
		FontMetrics fm = g.getFontMetrics();
		int stringWidth = SwingUtilities.computeStringWidth(fm, ballCount);
		
		PlayerBallSprite firstBall = balls.get(0);
		int px = firstBall.getX() + firstBall.getWidth() + 5;
		int py = firstBall.getY() - 5;

		//make sure that the counter label doesn't go off the screen 
		if (px + stringWidth > this.maxX) {
			px = this.maxX - stringWidth;
			py = py - 5;
		}
		
		g.setColor(Color.WHITE);
		g.drawString(ballCount, px, py);
	}

}
