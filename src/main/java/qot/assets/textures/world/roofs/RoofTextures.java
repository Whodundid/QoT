package qot.assets.textures.world.roofs;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

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