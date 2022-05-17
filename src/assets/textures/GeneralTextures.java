package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class GeneralTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture logo;
	
	static {
		logo = new GameTexture("logo", "bin/textures/general/qot_logo.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(logo);
		
		textures.add(logo);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
