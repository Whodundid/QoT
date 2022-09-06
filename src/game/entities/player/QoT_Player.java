package game.entities.player;

import envision.game.entity.Player;
import envision.game.entity.PlayerStats;
import game.assets.textures.entity.EntityTextures;

public class QoT_Player extends Player {
	
	private PlayerStats stats;
	
	public QoT_Player() { this("Player", 0, 0); }
	public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
	public QoT_Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
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
	
	@Override
	public void onLivingUpdate() {
		//setHeadText(startX + " : " + startY);
	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
