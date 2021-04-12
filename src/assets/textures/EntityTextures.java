package assets.textures;

import eutil.EUtil;
import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
import storageUtil.EArrayList;

public class EntityTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture goblin;
	public static final GameTexture player;
	public static final GameTexture whodundid;
	public static final GameTexture whodundidsbrother;
	public static final GameTexture trollboar;
	public static final GameTexture thyrah;
	
	static {
		goblin = new GameTexture("goblin", "bin/textures/entities/goblin/goblin_base.png");
		player = new GameTexture("player", "bin/textures/entities/player/player_base.png");
		whodundid = new GameTexture("whodundid", "bin/textures/entities/whodundid/whodundid_base.png");
		whodundidsbrother = new GameTexture("who_brother", "bin/textures/entities/whodundidsbrother/whodundidsbrother_base.png");
		trollboar = new GameTexture("trollboar", "bin/textures/entities/tollboar/tollboar_base.png");
		thyrah = new GameTexture("thyrah", "bin/textures/entities/thyrah/thyrah_base.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(goblin);
		systemIn.registerTexture(player);
		systemIn.registerTexture(whodundid);
		systemIn.registerTexture(whodundidsbrother);
		systemIn.registerTexture(trollboar);
		systemIn.registerTexture(thyrah);
		
		textures.add(goblin);
		textures.add(player);
		textures.add(whodundid);
		textures.add(whodundidsbrother);
		textures.add(trollboar);
		textures.add(thyrah);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
