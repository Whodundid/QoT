package game.assets.textures.doodads.bushes;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;

public class BushTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final BushTextures t = new BushTextures();
	public static BushTextures instance() { return t; }
	
	// Hide constructor
	private BushTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "doodads/bushes/";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	bush_0 = new GameTexture(textureDir, "stone_0.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, bush_0);
	}
	
}
