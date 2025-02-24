package qot.assets;

import envision.CurrentGame;
import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.SpriteSheet;

@Deprecated
public abstract class TextureLoader {
    
    protected static final String rDir = CurrentGame.getResourcesDir().toString();
    protected static final String tDir = rDir + "\\textures\\";
    
    public abstract void onRegister(TextureSystem sys);
    
    protected void reg(TextureSystem sys, GameTexture tex) {
        sys.reg(tex);
    }
    
    protected void reg(TextureSystem sys, String name, SpriteSheet sheet) {
        sys.reg(name, sheet);
    }
    
}
