package envision.game.entities.player;

import envision.game.entities.Entity;

public abstract class Player extends Entity {

	protected EntityStats stats;
	
	public Player(String nameIn) {
		super(nameIn);
		
		stats = new EntityStats(this);
	}
	
	public EntityStats getStats() { return stats; }
	
}
