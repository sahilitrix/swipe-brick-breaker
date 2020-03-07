import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class PreviewLine extends JComponent {

	private int x1, x2, y1, y2;
	
	public PreviewLine() {
		this.x1 = 0;
		this.y1 = 0;
		this.x2 = 0;
		this.y2 = 0;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		if (x2 != 0 && y2 != 0) {
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	public void setX1(int newX) {
		this.x1 = newX;
	}
	
	public void setY1(int newY) {
		this.y1 = newY;
	}
	
	public void setX2(int newX) {
		this.x2 = newX;
	}
	
	public void setY2(int newY) {
		this.y2 = newY;
	}
	
	public int getX2() {
		return x2;
	}
	
	public int getY2() {
		return y2;
	}
}
