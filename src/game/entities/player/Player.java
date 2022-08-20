package game.entities.player;

import assets.textures.entity.EntityTextures;
import game.entities.Entity;

public class Player extends Entity {
	
	private PlayerStats stats;
	
	public Player() { this("Player", 0, 0); }
	public Player(String nameIn) { this(nameIn, 0, 0); }
	public Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
		stats = new PlayerStats(this);
		
		//init(posX, posY, 48, 48);
		init(posX, posY, 40, 40);
		
		setMaxHealth(50);
		setHealth(50);
		setBaseMeleeDamage(2);
		
		setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
		//setCollisionBox(midX + 18, endY - 10, midX + 28, endY);
		sprite = EntityTextures.player;
	}
	
	//----------------
	// Player Getters
	//----------------
	
	public PlayerStats getStats() { return stats; }
	
	@Override
	public void onLivingUpdate() {
		//setHeadText(startX + " : " + startY);
	}
	
	@Override
	public int getObjectID() {
		return 0;
	}
	
}
