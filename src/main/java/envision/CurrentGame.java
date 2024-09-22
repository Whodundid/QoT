package envision;

import java.io.File;

import envision.engine.loader.AbstractWorldCreator;
import envision.engine.loader.GameSettings;
import envision.engine.screens.GameScreen;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import eutil.datatypes.util.EList;

public final class CurrentGame {
    
    //==============
    // Constructors
    //==============
    
    /** Hide Constructor. */
    private CurrentGame() {}
    
    //================
    // Static Methods
    //================
    
    public static GameScreen getStartScreen() { return Envision.currentGameInstance.getStartScreen(); }
    public static GameScreen getMainMenuScreen() { return Envision.currentGameInstance.getMainMenuScreen(); }
    public static GameScreen getNewGameScreen() { return Envision.currentGameInstance.getNewGameScreen(); }
    public static GameSettings getGameSettings() { return Envision.currentGameInstance.getGameSettings(); }
    public static EList<ConfigSetting<?>> getConfigSettings() { return getGameSettings().getConfigSettings(); }
    public static EnvisionConfigFile getGameConfig() { return Envision.currentGameInstance.getGameConfig(); }
    public static boolean reloadGameConfig() { var c = getGameConfig(); return (c != null) && c.loadConfig(); }
    public static boolean saveGameConfig() { var c = getGameConfig(); return (c != null) && c.saveConfig(); }
    public static File getResourcesDir() { return Envision.currentGameInstance.getResourcesDirectory(); }
    public static File getEditorWorldsDir() { return Envision.currentGameInstance.getEditorWorldsDirectory(); }
    public static File getInstallDir() { return Envision.currentGameInstance.getInstallationDirectory(); }
    public static File getSavedGamesDir() { return Envision.currentGameInstance.getSavedGamesDirectory(); }
    public static AbstractWorldCreator getGameWorldCreator() { return Envision.currentGameInstance.getWorldCreator(); }
    
    public static ConfigSetting<?> getCofingSetting(String name) { return getGameSettings().getSettingByName(name); }
    
}
