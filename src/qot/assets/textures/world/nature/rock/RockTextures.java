package qot.assets.textures.world.nature.rock;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class RockTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final RockTextures t = new RockTextures();
	public static RockTextures instance() { return t; }
	
	// Hide constructor
	private RockTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\nature\\rock\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	stone			= new GameTexture(textureDir, "stone.png"),
	rocky_stone		= new GameTexture(textureDir, "rocky_stone.png"),
	rough_rocky     = new GameTexture(textureDir, "rough_rocky.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stone);
		reg(sys, rocky_stone);
		reg(sys, rough_rocky);
	}
	
}

