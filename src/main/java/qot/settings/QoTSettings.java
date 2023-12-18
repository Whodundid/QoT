package qot.settings;

import static envision.engine.settings.config.ConfigSetting.*;

import java.io.File;

import envision.Envision;
import envision.engine.settings.MainConfigFile;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.setting_types.BooleanConfigSetting;
import envision.engine.settings.config.setting_types.IntegerConfigSetting;
import envision.game.EnvisionGameSettings;
import eutil.datatypes.util.EList;

public class QoTSettings extends EnvisionGameSettings {
    
    //=================
    // Static Instance
    //=================
    
    private static QoTSettings instance;
    
    private QoTSettings() {}
    public static QoTSettings instance() {
        if (instance == null) instance = new QoTSettings();
        return instance;
    }
    
    //-----------------------
    //       settings
    //-----------------------
    
    public static final BooleanConfigSetting
    
    animatedMainMenu    = boolSetting("animatedMainMenu", "Animated Main Menu", true),
    camreaEdgeLocking   = boolSetting("cameraEdgeLocking", "Enable Camera Edge Locking", true);
    
    //-------------------------------------------------------------------------------
    
    public static final IntegerConfigSetting
    
    musicVolume         = intSetting("musicVolume", "Music Volume", 30),
    sfxVolume           = intSetting("sfxVolume", "SFX Volume", 50);
    
    //--------------------------------------------------------------------------------
    
    //public static final StringConfigSetting
    
    // ;
    
    //===============================================================================
    //===============================================================================
    
    
    
    private static File localGameDir;
    private static File savesDir;
    private static File editorWorldsDir;
    private static File menuWorldsDir;
    private static File resourcesDir;
    
    /** The primary configuration file for the game. */
    private static MainConfigFile mainConfig;
    
    // holder
    private static final EList<ConfigSetting<?>> settings = EList.newList();
    
    static {
        // BOOLEANS
        settings.add(animatedMainMenu);
        settings.add(camreaEdgeLocking);
        
        // INTEGERS
        settings.add(musicVolume);
        
        // STRINGS
        // ;
    }
    
    @Override
    public EList<ConfigSetting<?>> getConfigSettings() {
        return settings;
    }
    
    //==============
    // initializers
    //==============
    
    @Override
    protected void initializeGameDirectories(File installDir, boolean useInternalResources) {
        //ensure installation directory actually exists
        if (!installDir.exists()) {
            throw new RuntimeException(String.format("Failed to bind to installation directory '%s'!", installDir));
        }
        
        //initialize game directories using installation directory
        initDirectories(installDir, useInternalResources);
        
        //establish game main config
        mainConfig = new MainConfigFile(new File(installDir, "MainConfig.ini"));
        mainConfig.tryLoad();
    }
    
    private static boolean initDirectories(File dirPath, boolean useInternalResources) {
        localGameDir = dirPath;
        
        //maps resources to internal jar resources instead of the installation directory
        if (useInternalResources) {
            resourcesDir = new File("resources\\");
        }
        else {
            resourcesDir = new File(dirPath, "resources");
            
            //error if resources dir does not exist
            if (!resourcesDir.exists()) {
                throw new RuntimeException(
                    String.format("Failed to bind to resources in installation dir '%s'!", dirPath));
            }
        }
        
        //create mappings to game directories
        savesDir = new File(localGameDir, "saves");
        editorWorldsDir = new File(localGameDir, "editorWorlds");
        menuWorldsDir = new File(localGameDir, "menuWorlds");
        
        if (!savesDir.exists()) {
            Envision.warn("Profiles directory not found! Attempting to create new one!");
            savesDir.mkdirs();
        }
        
        if (!editorWorldsDir.exists()) {
            Envision.warn("Editor worlds directory not found! Attempting to create new one!");
            editorWorldsDir.mkdirs();
        }
        
        if (!menuWorldsDir.exists()) {
            Envision.warn("Menu worlds directory not found! Attempting to create new one!");
            menuWorldsDir.mkdirs();
        }
        
        return true;
    }
    
    public static boolean saveConfig() {
        return mainConfig.trySave();
    }
    
    @Override public File getInstallationDirectory() { return localGameDir; }
    @Override public File getResourcesDirectory() { return resourcesDir; }
    @Override public File getSavedGamesDirectory() { return savesDir; }
    
    public static File getResourcesDir() { return resourcesDir; }
    public static File getLocalGameDir() { return localGameDir; }
    public static File getSavesDir() { return savesDir; }
    
    public static File getEditorWorldsDir() { return editorWorldsDir; }
    public static File getMenuWorldsDir() { return menuWorldsDir; }
    
    public static MainConfigFile getGameConfig() { return mainConfig; }
    
}
