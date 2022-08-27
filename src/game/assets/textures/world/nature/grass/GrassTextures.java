package game.assets.textures.world.nature.grass;

import envision.renderEngine.textureSystem.GameTexture;
import envision.renderEngine.textureSystem.TextureSystem;
import game.assets.TextureLoader;

public class GrassTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final GrassTextures t = new GrassTextures();
	public static GrassTextures instance() { return t; }
	
	// Hide constructor
	private GrassTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "world\\nature\\grass\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	dark_grass		= new GameTexture(textureDir, "dark_grass.png"),
	
	grass			= new Grass(),
	biome_grass		= new BiomeGrass(),
	dry_grass		= new DryGrass(),
	leafy_grass		= new LeafyGrass(),
	light_grass		= new LightGrass();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, dark_grass);
		
		reg(sys, grass);
		reg(sys, biome_grass);
		reg(sys, dry_grass);
		reg(sys, leafy_grass);
		reg(sys, light_grass);
	}
	
}
