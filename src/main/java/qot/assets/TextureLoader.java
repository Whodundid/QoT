package qot.assets;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.resourceLoaders.SpriteSheet;
import qot.settings.QoTSettings;

public abstract class TextureLoader {
	
	protected static final String rDir = QoTSettings.getResourcesDir().toString();
	protected static final String tDir = rDir + "\\textures\\";
	
	public abstract void onRegister(TextureSystem sys);
	
	protected void reg(TextureSystem sys, GameTexture tex) {
		sys.reg(tex);
	}
	
	protected void reg(TextureSystem sys, String name, SpriteSheet sheet) {
	    sys.reg(name, sheet);
	}
	
}
