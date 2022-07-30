package game.entities;

import assets.textures.EntityTextures;
import game.PlayerStats;

public class Player extends Entity {
	
	private PlayerStats stats;
	
	public Player() { this("Player", 0, 0); }
	public Player(String nameIn) { this(nameIn, 0, 0); }
	public Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
		stats = new PlayerStats(this);
		
		init(posX, posY, 64, 64);
		
		setMaxHealth(20);
		setHealth(20);
		setBaseMeleeDamage(5);
		
		setCollisionBox(startX + 22, endY - 10, endX - 22, endY);
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
