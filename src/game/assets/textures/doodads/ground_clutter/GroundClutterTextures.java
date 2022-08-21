package game.assets.textures.doodads.ground_clutter;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;

public class GroundClutterTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final GroundClutterTextures t = new GroundClutterTextures();
	public static GroundClutterTextures instance() { return t; }
	
	// Hide constructor
	private GroundClutterTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "doodads\\ground_clutter\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	stone_0 = new GameTexture(textureDir, "stone_0.png"),
	stone_1 = new GameTexture(textureDir, "stone_1.png"),
	stone_2 = new GameTexture(textureDir, "stone_2.png"),
	stone_3 = new GameTexture(textureDir, "stone_3.png"),
	stone_4 = new GameTexture(textureDir, "stone_4.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stone_0);
		reg(sys, stone_1);
		reg(sys, stone_2);
		reg(sys, stone_3);
		reg(sys, stone_4);
	}
	
}
