
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class NumBlockSprite extends CollisionSprite {
	
	private int health;
	private Color color;

	public NumBlockSprite(int x, int y, int sideLength, JLabel score) {
		super(x, y, sideLength, sideLength);
		int currScore = Integer.parseInt(score.getText());
		int difficulty = 0;
		if (currScore >= 20) {
			difficulty = 20;
		} else if (currScore >= 15) {
			difficulty = 15;
		} else if (currScore >= 10) {
			difficulty = 10;
		} else if (currScore >= 5) {
			difficulty = 5;
		} else {
			difficulty = 1;
		}
		health =  (int) (Math.random() * 10 + difficulty);
		color = ColorPalate.randomColor();
	}
	
	@Override 
	public void onCollision(GameSprite o) {
		if (health > 0) {
			health -= 1;
		}
		if (health == 0) {
			setReadyToBeDestroyed(true);
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	@Override 
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		FontMetrics fm = g.getFontMetrics();
		int stringWidth = SwingUtilities.computeStringWidth(fm, health+"");

		int centerX = (int) Math.round(getX() + getWidth()/2.0 - stringWidth/2.0);
		int centerY = getY() + (int) Math.round(getHeight()/2.0 + (fm.getHeight()/4.0));

		g.setColor(Color.BLACK);
		g.drawString(health + "", centerX, centerY);
	}
}
