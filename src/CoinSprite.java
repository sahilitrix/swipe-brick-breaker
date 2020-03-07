
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class CoinSprite extends CollisionSprite {
	
	private int diameter;

	public CoinSprite(int x, int y, int diameter, int boundingLength) {
		super(x, y, boundingLength, boundingLength);
		this.diameter = diameter;
	}

	@Override
	public void onCollision(GameSprite o) {
		setReadyToBeDestroyed(true);
	}
	
	@Override
	public void draw(Graphics g) {
		if (!isReadyToBeDestroyed()) {
			g.setColor(Color.WHITE);
			int startX = getX() + (int) Math.round(getWidth()/2.0 - diameter/2.0);
			int startY = getY() +(int) Math.round(getHeight()/2.0 - diameter/2.0);
			g.fillOval(startX, startY, diameter, diameter);
		}
	}
	
	public int getDiameter() {
		return diameter;
	}
}
