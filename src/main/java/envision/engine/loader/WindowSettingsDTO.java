package envision.engine.loader;

import eutil.strings.EToStringBuilder;

public class WindowSettingsDTO {
    
    //========
    // Fields
    //========
    
    private boolean fullscreen;
    private boolean vSync;
    private int windowWidth;
    private int windowHeight;
    
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
    
    //=========
    // Setters
    //=========
    
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }
    public void setvSync(boolean vSync) { this.vSync = vSync; }
    public void setWindowWidth(int windowWidth) { this.windowWidth = windowWidth; }
    public void setWindowHeight(int windowHeight) { this.windowHeight = windowHeight; }
    
}
