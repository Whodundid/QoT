package qot.assets.textures.general;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
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
	noscreens = new GameTexture(textureDir, "noscreens.jpg"),
	hunt_stillwater = new GameTexture(textureDir, "hunt_stillwater.png"),
	hunt_lawson = new GameTexture(textureDir, "hunt_lawson.png"),
	hunt_desalle = new GameTexture(textureDir, "hunt_desalle.png")
	;
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, logo);
		reg(sys, noscreens);
		reg(sys, hunt_stillwater);
		reg(sys, hunt_lawson);
		reg(sys, hunt_desalle);
	}
	
}
