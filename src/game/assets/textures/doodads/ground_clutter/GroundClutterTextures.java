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
	
	stones = new StoneClutter();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stones);
	}
	
}
