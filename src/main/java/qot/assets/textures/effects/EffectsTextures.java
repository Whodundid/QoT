package qot.assets.textures.effects;

import envision.engine.registry.types.SpriteSheet;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class EffectsTextures extends TextureLoader {
	
	//====================
	// Singleton Instance
	//====================
	
	private static final EffectsTextures t = new EffectsTextures();
	public static EffectsTextures instance() { return t; }
	
	// Hide constructor
	private EffectsTextures() {}
		
	//===============================
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\effects\\";
	
	//==========
	// Textures
	//==========
	
	public static final GameTexture
	
	static_effect = new GameTexture(textureDir, "static.png")
	;
	
	public static final SpriteSheet
	
	static_effect_spritesheet = new SpriteSheet(static_effect, 64, 64, 16, 0);
	
	//===========
    // Overrides
    //===========
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, static_effect);
		
		reg(sys, "static-effect-spritesheet", static_effect_spritesheet);
	}
	
}
