package assets.textures.world.nature.water;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class WaterTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WaterTextures t = new WaterTextures();
	public static WaterTextures instance() { return t; }
	
	// Hide constructor
	private WaterTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\nature\\water\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	water = new GameTexture(textureDir, "water.png"),
	dark_water = new GameTexture(textureDir, "dark_water.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, water);
		reg(sys, dark_water);
	}
	
}
