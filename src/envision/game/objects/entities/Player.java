package envision.game.objects.entities;

public abstract class Player extends Entity {

	protected PlayerStats stats;
	
	public Player(String nameIn) {
		super(nameIn);
		
		stats = new PlayerStats(this);
	}
	
	public PlayerStats getStats() { return stats; }
	
	public void onMousePress(int mXIn, int mYIn, int button) {}
	
}
