package qot.assets.textures.world.nature.grass;

import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.SpriteSheet;
import qot.assets.TextureLoader;

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
    
    grassSheetTexture = new GameTexture(textureDir, "grass.png");
    
    public static final SpriteSheet
    
    grassSheet = new SpriteSheet(grassSheetTexture, 32, 32, 16, 0)
    
    ;

    //==========
    // Overrides
    //==========
    
    @Override
    public void onRegister(TextureSystem sys) {
        reg(sys, grassSheetTexture);
        reg(sys, "grass_sheet", grassSheet);
    }
    
}

