package envision.engine.loader.dtos.engine;

import eutil.math.ENumUtil;
import eutil.strings.EToStringBuilder;

public record WindowSettingsDTO(boolean fullscreen, boolean vSync, int windowWidth, int windowHeight) {    
    //==============
    // Constructors
    //==============
    
    /** Clamps minimum resolution to 144p or (256 x 144) pixels. */
    public WindowSettingsDTO(boolean fullscreen, boolean vSync, int windowWidth, int windowHeight) {
        windowWidth = ENumUtil.clamp(windowWidth, 256, Integer.MAX_VALUE);
        windowHeight = ENumUtil.clamp(windowHeight, 144, Integer.MAX_VALUE);
        
        this.fullscreen = fullscreen;
        this.vSync = vSync;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        sb.a("fullscreen", fullscreen);
        sb.a("vSync", vSync);
        sb.a("windowWidth", windowWidth);
        sb.a("windowHeight", windowHeight);
        return sb.toString();
    }    
    //=========
    // Getters
    //=========
    
    public boolean isFullscreen() { return fullscreen; }
    public boolean isvSync() { return vSync; }
    public int getWindowWidth() { return windowWidth; }
    public int getWindowHeight() { return windowHeight; }
    
}
