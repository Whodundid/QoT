package qot.assets.textures.world.nature.water;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.resourceLoaders.SpriteSheet;
import qot.assets.TextureLoader;

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
	dark_water = new GameTexture(textureDir, "dark_water.png"),
	blue_water = new GameTexture(textureDir, "water-sheet.png");
	
	public static final SpriteSheet
	
	blue_water_sheet = new SpriteSheet(blue_water, 32, 32, 5, 0);
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, water);
		reg(sys, dark_water);
		reg(sys, blue_water);
		
		reg(sys, "blue_water_sheet", blue_water_sheet);
	}
	
}
