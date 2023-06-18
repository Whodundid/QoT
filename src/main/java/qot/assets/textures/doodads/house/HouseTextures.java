package qot.assets.textures.doodads.house;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class HouseTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final HouseTextures t = new HouseTextures();
	public static HouseTextures instance() { return t; }
	
	// Hide constructor
	private HouseTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "doodads\\house\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	anvil = new GameTexture(textureDir, "anvil.png"),
	barrel = new GameTexture(textureDir, "barrel.png"),
	chair = new GameTexture(textureDir, "chair.png"),
	counter_food = new GameTexture(textureDir, "count_food.png"),
	counter = new GameTexture(textureDir, "counter.png"),
	crate = new GameTexture(textureDir, "crate.png"),
	oven = new GameTexture(textureDir, "oven.png"),
	sink = new GameTexture(textureDir, "sink.png"),
	stool = new GameTexture(textureDir, "stool.png"),
	wall_mounted_pick = new GameTexture(textureDir, "wall_mounted_pick.png"),
	wall_mounted_tools = new GameTexture(textureDir, "wall_mounted_tools.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, anvil);
		reg(sys, barrel);
		reg(sys, chair);
		reg(sys, counter_food);
		reg(sys, counter);
		reg(sys, crate);
		reg(sys, oven);
		reg(sys, sink);
		reg(sys, stool);
		reg(sys, wall_mounted_pick);
		reg(sys, wall_mounted_tools);
	}
	
}
