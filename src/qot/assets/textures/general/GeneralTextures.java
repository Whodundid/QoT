package qot.assets.textures.general;

import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.renderEngine.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class GeneralTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final GeneralTextures t = new GeneralTextures();
	public static GeneralTextures instance() { return t; }
	
	// Hide constructor
	private GeneralTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\general\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	logo = new GameTexture(textureDir, "qot_logo.png"),
	noscreens = new GameTexture(textureDir, "noscreens.jpg");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, logo);
		reg(sys, noscreens);
	}
	
}
