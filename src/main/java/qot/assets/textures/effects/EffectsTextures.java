package qot.assets.textures.effects;

import envision.engine.registry.types.SpriteSheet;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;

public class EffectsTextures extends TextureLoader {
    
    //====================
    // Singleton Instance
    //====================
    
    private static final EffectsTextures t = new EffectsTextures();
    public static EffectsTextures instance() { return t; }
    
    // Hide constructor
    private EffectsTextures() {}
        
    //===============================
    
    private static final String textureDir = tDir + "effects\\";
    
    //==========
    // Textures
    //==========
    
    public static final GameTexture
    
    static_effect = new GameTexture(textureDir, "static.png"),
    explosion_effect = new GameTexture(textureDir, "Explosion25.png"),
    fire_effect = new GameTexture(textureDir, "Fire01.png")
    ;
    
    public static final SpriteSheet
    
    static_effect_spritesheet = new SpriteSheet(static_effect, 64, 64, 16, 0),
    explosion_effect_spritesheet = new SpriteSheet(explosion_effect, 128, 128, 16, 0),
    fire_effect_spritesheet = new SpriteSheet(fire_effect, 128, 256, 32, 0);
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onRegister(TextureSystem sys) {
        reg(sys, static_effect);
        reg(sys, explosion_effect);
        reg(sys, fire_effect);
        
        reg(sys, "static-effect-spritesheet", static_effect_spritesheet);
        reg(sys, "explosion-effect-spritesheet", explosion_effect_spritesheet);
        reg(sys, "fire-effect-spritesheet", fire_effect_spritesheet);
    }
    
}
