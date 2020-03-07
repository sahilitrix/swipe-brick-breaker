
import java.awt.Dimension;
import java.awt.Graphics;

public abstract class GameSprite {
	
	private int width;
	private int height;
	private int xPos;
	private int yPos;
	
	public GameSprite(int x, int y, int w, int h) {
		xPos = x;
		yPos = y;
		width = w;
		height = h;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public void setX(int newX) {
		xPos = newX;
	}
	
	public void setY(int newY) {
		yPos = newY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Dimension size() {
		return new Dimension(width, height); 
	}
	
	public abstract void draw(Graphics g);
		
}
