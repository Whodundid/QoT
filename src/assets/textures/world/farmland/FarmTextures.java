package assets.textures.world.farmland;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class FarmTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final FarmTextures t = new FarmTextures();
	public static FarmTextures instance() { return t; }
	
	// Hide constructor
	private FarmTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\farmland\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	farm_0 = new GameTexture(textureDir, "farm0.png"),
	farm_1 = new GameTexture(textureDir, "farm1.png"),
	farm_2 = new GameTexture(textureDir, "farm2.png"),
	farm_3 = new GameTexture(textureDir, "farm3.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, farm_0);
		reg(sys, farm_1);
		reg(sys, farm_2);
		reg(sys, farm_3);
	}
	
}
