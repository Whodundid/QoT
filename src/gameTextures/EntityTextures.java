package gameTextures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;

public class EntityTextures {
	
	public static final GameTexture goblin;
	public static final GameTexture player;
	public static final GameTexture whodundid;
	public static final GameTexture whodundidsbrother;
	public static final GameTexture tollboar;
	public static final GameTexture thyrah;
	
	static {
		goblin = new GameTexture("bin/textures/entities/goblin/goblin_base.png");
		player = new GameTexture("bin/textures/entities/player/player_base.png");
		whodundid = new GameTexture("bin/textures/entities/whodundid/whodundid_base.png");
		whodundidsbrother = new GameTexture("bin/textures/entities/whodundidsbrother/whodundidsbrother_base.png");
		tollboar = new GameTexture("bin/textures/entities/tollboar/tollboar_base.png");
		thyrah = new GameTexture("bin/textures/entities/thyrah/thyrah_base.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(goblin);
		systemIn.registerTexture(player);
		systemIn.registerTexture(whodundid);
		systemIn.registerTexture(whodundidsbrother);
		systemIn.registerTexture(tollboar);
		systemIn.registerTexture(thyrah);
	}
	
}
