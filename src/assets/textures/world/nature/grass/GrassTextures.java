package assets.textures.world.nature.grass;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

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
	
	bush_0			= new GameTexture(textureDir, "bush_0.png"),
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
		reg(sys, bush_0);
		reg(sys, dark_grass);
		
		reg(sys, grass);
		reg(sys, biome_grass);
		reg(sys, dry_grass);
		reg(sys, leafy_grass);
		reg(sys, light_grass);
	}
	
}

