package envision.game.entities.player;

import envision.game.entities.Entity;

public abstract class Player extends Entity {

	protected PlayerStats stats;
	
	public Player(String nameIn) {
		super(nameIn);
		
		stats = new PlayerStats(this);
	}
	
	public PlayerStats getStats() { return stats; }
	
}
