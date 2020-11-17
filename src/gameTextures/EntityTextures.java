package gameTextures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;

public class EntityTextures {
	
	public static final GameTexture goblin;
	public static final GameTexture player;
	public static final GameTexture whodundid;
	
	static {
		goblin = new GameTexture("bin/textures/entities/goblin/goblin_base.png");
		player = new GameTexture("bin/textures/entities/player/player_base.png");
		whodundid = new GameTexture("bin/textures/entities/whodundid/whodundid_base.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(goblin);
		systemIn.registerTexture(player);
		systemIn.registerTexture(whodundid);
	}
	
}
