package qot.assets;

import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.renderEngine.textureSystem.TextureSystem;
import qot.settings.QoTSettings;

public abstract class TextureLoader {
	
	protected static final String rDir = QoTSettings.getResourcesDir().toString();
	protected static final String tDir = rDir + "\\textures\\";
	
	public abstract void onRegister(TextureSystem sys);
	
	protected void reg(TextureSystem sys, GameTexture tex) {
		sys.reg(tex);
	}
	
}
