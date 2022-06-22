package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class GeneralTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture logo;
	public static final GameTexture noscreens;
	
	private static final String base = "resources/textures/general/";
	
	static {
		logo = new GameTexture("logo", base, "qot_logo.png");
		noscreens = new GameTexture("noscreens", base, "noscreens.jpg");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(logo);
		systemIn.registerTexture(noscreens);
		
		textures.add(logo);
		textures.add(noscreens);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
