package envision.engine.assets;

import envision.engine.EngineSettings;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.resourceLoaders.SpriteSheet;

public abstract class EngineTextureLoader {
	
	protected static final String rDir = EngineSettings.RESOURCES_DIR.toString();
	protected static final String tDir = rDir + "/textures/";
	
	public abstract void onRegister(TextureSystem sys);
	
	protected void reg(TextureSystem sys, GameTexture tex) {
		sys.reg(tex);
	}
	
	protected void reg(TextureSystem sys, String name, SpriteSheet sheet) {
	    sys.reg(name, sheet);
	}
	
}
