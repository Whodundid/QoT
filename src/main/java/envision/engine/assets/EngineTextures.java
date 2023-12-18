package envision.engine.assets;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;

public class EngineTextures extends EngineTextureLoader {
    
    //====================
    // Singleton Instance
    //====================
    
    private static final EngineTextures t = new EngineTextures();
    public static EngineTextures instance() { return t; }
    
    // Hide constructor
    private EngineTextures() {}
        
    //===============================
    
    private static final String textureDir = rDir + "\\textures\\envision\\";
    
    public static CursorTextures cursorTextures = CursorTextures.instance();
    public static EditorTextures editorTextures = EditorTextures.instance();
    public static TaskBarTextures taskBarTextures = TaskBarTextures.instance();
    public static WindowTextures windowTextures = WindowTextures.instance();
    public static WindowBuilderTextures windowBuilderTextures = WindowBuilderTextures.instance();
    
    //==========
    // Textures
    //==========
    
    public static final GameTexture
    
    noscreens = new GameTexture(textureDir, "noscreens.jpg")
    
    ;
    
    //-----------
    // Overrides
    //-----------
    
    @Override
    public void onRegister(TextureSystem sys) {
        reg(sys, noscreens);
        
        cursorTextures.onRegister(sys);
        editorTextures.onRegister(sys);
        taskBarTextures.onRegister(sys);
        windowTextures.onRegister(sys);
        windowBuilderTextures.onRegister(sys);
    }
    
}
