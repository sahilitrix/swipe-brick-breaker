
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class PlayerBallSprite extends GameSprite {
	
	private int vx;
	private int vy;
	private int maxX;
	private int maxY;
	private boolean isActive; //whether the ball is currently moving
	private boolean launchReady; //whether the ball is ready to be launched 
	
	public PlayerBallSprite(int x, int y, int diameter, int courtWidth, int courtHeight) {
		super(x, y, diameter, diameter);
		this.maxX = courtWidth - diameter;
		this.maxY = courtHeight - diameter;
		this.isActive = true;
		this.launchReady = false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(getX(), getY(), getWidth(), getWidth());
	}
	
	public void move() {
		setX(getX() + this.vx);
		setY(getY() + this.vy);
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean status) {
		isActive = status;
	}
	
	  /**
     * Update the velocity of the object in response to hitting an obstacle in the given direction.
     * If the direction is null, this method has no effect on the object.
     *
     * @param d The direction in which this object hit an obstacle
     */
    public void bounce(Direction d) {
        if (d == null) return;
        
        switch (d) {
        case UP:
            this.vy = Math.abs(this.vy);
            break;  
        case DOWN:
            this.vy = -Math.abs(this.vy);
            break;
        case LEFT:
            this.vx = Math.abs(this.vx);
            break;
        case RIGHT:
            this.vx = -Math.abs(this.vx);
            break;
        }
    }
    
    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWallDirection() {
        if (getX() + this.vx < 0) {
            return Direction.LEFT;
        } else if (getX() + this.vx > this.maxX) {
            return Direction.RIGHT;
        }
        if (getY() + this.vy < 0) {
            return Direction.UP;
        } 
        else {
            return null;
        }
    }
    
    /**
     * 
     * return the direction of the other object in relation to this game object.
     * 
     * @param that The other object
     * @return Direction of impending object, null if all clear.
     */
    public Direction hitObjDirection(GameSprite o) {
        double dx = o.getX() + o.getWidth() / 2 - (getX() + getWidth() / 2);
        double dy = o.getY() + o.getHeight() / 2 - (getY() + getHeight() / 2);

        double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy *dy)));
        double diagTheta = Math.atan2(getHeight() / 2, getWidth() / 2);

        if (theta <= diagTheta) {
            return Direction.RIGHT;
        } else if (theta > diagTheta && theta <= Math.PI - diagTheta) {
            // Coordinate system for GUIs is switched
            if (dy > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        } else {
            return Direction.LEFT;
        }
    }
    
    public boolean collidesWith(GameSprite o) {
    	 return (getX() + getWidth() >= o.getX()
    	            && getY() + getHeight() >= o.getY()
    	            && o.getX() + o.getWidth() >= getX() 
    	            && o.getY() + o.getHeight() >= getY());
    }
    
    //checks if the ball hits the floor
    //returns true only if the ball hits the floor while coming downward
    public boolean hitsFloor() {
    	return getY() + this.vy >= this.maxY 
    			&& getY() < this.maxY;
    }
	
	public void setVx(int vx) {
		this.vx = vx;
	}
	
	public void setVy(int vy) {
		this.vy = vy;
	}
	
	public int getVx() {
		return this.vx;
	}
	
	public int getVy() {
		return this.vy;
	}
	
	public int getMaxX() {
		return this.maxX;
	}
	
	public int getMaxY(){
		return this.maxY;
	}
	public void setLaunchReady(boolean launchReady) {
		this.launchReady = launchReady;
	}
	
	public boolean isLaunchReady() {
		return this.launchReady;
	}
	
}
