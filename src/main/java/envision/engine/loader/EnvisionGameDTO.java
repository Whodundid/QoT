package envision.engine.loader;

import eutil.strings.EToStringBuilder;

public class EnvisionGameDTO {
    
    //========
    // Fields
    //========
    
    private String gameName;
    private String gameVersion;
    
    private String resourcesDir;
    private String scriptsDir;
    private String screenDir;
    private String savesDir;
    
    private String mainConfigFile;
    
    private String startScreen;
    private String mainMenuScreen;
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        sb.a("gameName", gameName);
        sb.a("gameVersion", gameVersion);
        sb.a("resourcesDir", resourcesDir);
        sb.a("scriptsDir", scriptsDir);
        sb.a("screenDir", screenDir);
        sb.a("savesDir", savesDir);
        sb.a("mainConfigFile", mainConfigFile);
        sb.a("startScreen", startScreen);
        sb.a("mainMenuScreen", mainMenuScreen);
        return sb.toString();
    }
    
    //=========
    // Getters
    //=========
    
    public String getGameName() { return gameName; }
    public String getGameVersion() { return gameVersion; }
    public String getResourcesDir() { return resourcesDir; }
    public String getScriptsDir() { return scriptsDir; }
    public String getScreenDir() { return screenDir; }
    public String getSavesDir() { return savesDir; }
    public String getMainConfigFile() { return mainConfigFile; }
    public String getStartScreen() { return startScreen; }
    public String getMainMenuScreen() { return mainMenuScreen; }

    //=========
    // Setters
    //=========
    
    public void setGameName(String gameName) { this.gameName = gameName; }
    public void setGameVersion(String gameVersion) { this.gameVersion = gameVersion; }
    public void setResourcesDir(String resourcesDir) { this.resourcesDir = resourcesDir; }
    public void setScriptsDir(String scriptsDir) { this.scriptsDir = scriptsDir; }
    public void setScreenDir(String screenDir) { this.screenDir = screenDir; }
    public void setSavesDir(String savesDir) { this.savesDir = savesDir; }
    public void setMainConfigFile(String mainConfigFile) { this.mainConfigFile = mainConfigFile; }
    public void setStartScreen(String startScreen) { this.startScreen = startScreen; }
    public void setMainMenuScreen(String mainMenuScreen) { this.mainMenuScreen = mainMenuScreen; }
    
}
