package qot.assets.textures.general;

import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.built.game.GameTexture;
import qot.assets.TextureLoader;

public class GeneralTextures extends TextureLoader {
    
    //====================
    // Singleton Instance
    //====================
    
    private static final GeneralTextures t = new GeneralTextures();
    public static GeneralTextures instance() { return t; }
    
    // Hide constructor
    private GeneralTextures() {}
        
    //===============================
    
    private static final String textureDir = tDir + "general\\";
    
    //==========
    // Textures
    //==========
    
    public static final GameTexture
    
    logo = new GameTexture(textureDir, "qot_logo.png"),
    hunt_stillwater = new GameTexture(textureDir, "hunt_stillwater.png"),
    hunt_lawson = new GameTexture(textureDir, "hunt_lawson.png"),
    hunt_desalle = new GameTexture(textureDir, "hunt_desalle.png")
    ;

    //==========
    // Overrides
    //==========
    
    @Override
    public void onRegister(TextureSystem sys) {
        reg(sys, logo);
        reg(sys, hunt_stillwater);
        reg(sys, hunt_lawson);
        reg(sys, hunt_desalle);
    }
    
}
