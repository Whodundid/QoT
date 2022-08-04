package assets.textures.world.nature.rock;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

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
	rocky_stone		= new GameTexture(textureDir, "rocky_stone.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, stone);
		reg(sys, rocky_stone);
	}
	
}

