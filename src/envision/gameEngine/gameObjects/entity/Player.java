package envision.gameEngine.gameObjects.entity;

public abstract class Player extends Entity {

	protected PlayerStats stats;
	
	public Player(String nameIn) {
		super(nameIn);
		
		stats = new PlayerStats(this);
	}
	
	public PlayerStats getStats() { return stats; }
	
}
