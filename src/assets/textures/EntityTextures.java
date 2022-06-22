package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class EntityTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture goblin;
	public static final GameTexture player;
	public static final GameTexture whodundid;
	public static final GameTexture whodundidsbrother;
	public static final GameTexture trollboar;
	public static final GameTexture thyrah;
	
	private static final String base = "resources/textures/entities/";
	
	static {
		goblin = new GameTexture("goblin", base, "goblin/goblin_base.png");
		player = new GameTexture("player", base, "player/player_base.png");
		whodundid = new GameTexture("whodundid", base, "whodundid/whodundid_base.png");
		whodundidsbrother = new GameTexture("who_brother", base, "whodundidsbrother/whodundidsbrother_base.png");
		trollboar = new GameTexture("trollboar", base, "tollboar/tollboar_base.png");
		thyrah = new GameTexture("thyrah", base, "thyrah/thyrah_base.png");
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
