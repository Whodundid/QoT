package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

public class DoodadTextures {

	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture pine_tree;
	public static final GameTexture player_spawn;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\doodads\\";
	
	static {
		pine_tree = new GameTexture("pine", textureDir, "tree_pine_0.png");
		player_spawn = new GameTexture("spawn", textureDir, "player_start.png");
	}
	
	public static void registerTextures(TextureSystem ts) {
		textures.add(ts.registerTexture(pine_tree));
		textures.add(ts.registerTexture(player_spawn));
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
