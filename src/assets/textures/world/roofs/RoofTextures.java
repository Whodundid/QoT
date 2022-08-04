package assets.textures.world.roofs;

import assets.TextureLoader;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class RoofTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final RoofTextures t = new RoofTextures();
	public static RoofTextures instance() { return t; }
	
	// Hide constructor
	private RoofTextures() {}
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	gray_roof = new GrayRoof();
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, gray_roof);
	}
	
}
