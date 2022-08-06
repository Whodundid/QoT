package assets.textures.world.nature.sand;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class SandTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final SandTextures t = new SandTextures();
	public static SandTextures instance() { return t; }
	
	// Hide constructor
	private SandTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\nature\\sand\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	sand			= new GameTexture(textureDir, "sand.png"),
	red_sand		= new GameTexture(textureDir, "red_sand.png"),
	
	coarse_sand		= new Texture_CoarseSand();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, sand);
		reg(sys, red_sand);
		
		reg(sys, coarse_sand);
	}
	
}