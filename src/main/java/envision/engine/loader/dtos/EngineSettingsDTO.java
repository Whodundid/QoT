package envision.engine.loader.dtos;

import eutil.strings.EToStringBuilder;

public class EngineSettingsDTO {
    
    //========
    // Fields
    //========
    
    private boolean debugMode;
    private String version;
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        sb.a("debugMode", debugMode);
        sb.a("version", version);
        return sb.toString();
    }
    
    //=========
    // Getters
    //=========
    
    public boolean isDebugMode() { return debugMode; }
    public String getVersion() { return version; }
    
    //=========
    // Setters
    //=========
    
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }
    public void setVersion(String version) { this.version = version; }
    
}
