package envision.engine.loader;

import java.io.File;

import envision.engine.events.GameEvent;
import envision.engine.events.IEventListener;
import envision.engine.loader.dtos.EnvisionGameDTO;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.screens.GameScreen;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import envision.engine.terminal.TerminalCommandHandler;
import envision.launcher.EnvisionGameLauncher;
import envision.launcher.LauncherSettings;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class EnvisionGame implements IEventListener {
	
    //========
    // Fields
    //========
    
    private LoadedGameDirectory gameDirectory;
    private EnvisionGameDTO dto;
    
    private String gameName;
    private String gameVersion;
    
    private File resourcesDirectory;
    private File installationDirectory;
    private File savedGamesDirectory;
    private File configFile;
    
    private GameSettings gameSettings;
    private EnvisionConfigFile gameConfig;
    private AbstractWorldCreator worldCreator;
    
    private GameScreen startScreen;
    private GameScreen mainMenuScreen;
    private GameScreen newGameScreen;
    
    //==============
    // Constructors
    //==============
    
    public EnvisionGame() {}
    
    public EnvisionGame(LoadedGameDirectory dirIn) throws Exception {
        gameDirectory = dirIn;
        dto = gameDirectory.getGameDTO();
        initializeFromDTO();
    }
    
    protected void initializeFromDTO() throws Exception {
        gameName = dto.getGameName();
        gameVersion = dto.getGameVersion();
        
        installationDirectory = gameDirectory.getDirectory();
        resourcesDirectory = new File(installationDirectory, dto.getResourcesDir());
        savedGamesDirectory = new File(installationDirectory, dto.getSavesDir());
        
        String configName = dto.getMainConfigFile();
        if (configName != null) {
            configFile = new File(installationDirectory, dto.getMainConfigFile());
        }
        
        gameSettings = new GameSettings(gameDirectory.getConfigurationSettings());
        gameConfig = new GameConfig(this);
        gameConfig.tryLoad();
    }
    
    //=========
    // Getters
    //=========
    
    public String getGameName() { return gameName; }
    public String getGameVersionString() { return gameVersion; }
    public File getMainConfigFile() { return configFile; }
    
    public GameSettings getGameSettings() { return gameSettings; }
    public EnvisionConfigFile getGameConfig() { return gameConfig; }
    public AbstractWorldCreator getWorldCreator() { return worldCreator; }
    
    public EList<ConfigSetting<?>> getConfigSettings() { return gameSettings.getConfigSettings(); }
    
    public File getResourcesDirectory() { return resourcesDirectory; }
    public File getInstallationDirectory() { return installationDirectory; }
    public File getSavedGamesDirectory() { return savedGamesDirectory; }
    public File getEditorWorldsDirectory() { return new File(installationDirectory, "editorWorlds"); }
    
    public String getResourcesPath() { return EStringUtil.toString(resourcesDirectory); }
    public String getInstallationPath() { return EStringUtil.toString(installationDirectory); }
    public String getSavedGamesPath() { return EStringUtil.toString(savedGamesDirectory); }
    
    public EnvisionGameLauncher createGameLauncher(LauncherSettings settings) {
        return null;
    }
    
    /** Returns this game's starting screen. */
    public GameScreen getStartScreen() {
        return startScreen;
    }
    
    /** Returns this game's main menu screen. */
    public GameScreen getMainMenuScreen() {
        return null;
    }
    
    /** Returns this game's new game screen. */
    public GameScreen getNewGameScreen() {
        return null;
    }
    
    //=========
    // Setters
    //=========
    
    
    
    //=========
    // Methods
    //=========
    
    public void onRenderTick() {}
    public void onGameTick() {}
	
    public void onMouseInput(int action, int mX, int mY, int button, int change) {}
    public void onMousePress(int mX, int mY, int button) {}
    public void onMouseRelease(int mX, int mY, int button) {}
	
    public void onKeyInput(int action, char typedChar, int keyCode) {}
    public void onKeyPress(char typedChar, int keyCode) {}
    public void onKeyRelease(char typedChar, int keyCode) {}
	
    public void onWindowResized() {}
	
    public void onEvent(GameEvent e) {}
	
    public void onTerminalLoad(TerminalCommandHandler handler) {}
	
    public void onPreEngineLoad() {}
    public void onPostEngineLoad() {}
    public void onPreEngineUnload() {}
    public void onPostEngineUnload() {}
	
    public void onPreGameUnload() {}
    public void onPostGameUnload() {}
	
    public void onRegisterInternalTextures(TextureSystem textureSystem) {}
	
}
