package game.assets.textures.world.nature.rock;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;

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
