
public abstract class CollisionSprite extends GameSprite {

	private boolean readyToBeDestroyed;
	
	public CollisionSprite(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.readyToBeDestroyed = false;
	}
	
	public boolean isReadyToBeDestroyed() {
		return readyToBeDestroyed;
	}
	
	public void setReadyToBeDestroyed(boolean status) {
		readyToBeDestroyed = status;
	}

	public abstract void onCollision(GameSprite o);
}
