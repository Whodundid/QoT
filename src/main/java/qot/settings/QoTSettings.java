package qot.settings;

import static envision.engine.settings.config.ConfigSetting.*;

import java.io.File;

import envision.Envision;
import envision.engine.settings.MainConfigFile;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.setting_types.BooleanConfigSetting;
import envision.engine.settings.config.setting_types.IntegerConfigSetting;
import envision.engine.settings.config.setting_types.StringConfigSetting;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class QoTSettings {
    
    //-----------------------
    //       settings
    //-----------------------
    
    private QoTSettings() {}
    
    public static final BooleanConfigSetting
    
    animatedMainMenu    = boolSetting("animatedMainMenu", "Animated Main Menu", true),
    drawFPS             = boolSetting("drawFPS", "Draw FPS", false),
    fullscreen          = boolSetting("fullscreen", "Fullscreen", false),
    vsync               = boolSetting("vSync", "V-Sync", false),
    termLineNumbers     = boolSetting("termLineNumbers", "Show Terminal Line Numbers", false),
    closeHudWhenEmpty   = boolSetting("closeHudWhenEmpty", "Close Hud when Empty", true),
    batchRendering      = boolSetting("batchRendering", "Enable Batch Rendering", true);
    
    //-------------------------------------------------------------------------------
    
    public static final IntegerConfigSetting
    
    musicVolume         = intSetting("musicVolume", "Music Volume", 50),
    targetFPS           = intSetting("targetFPS", "Target FPS", 60),
    targetUPS           = intSetting("targetUPS", "Target UPS", 150),
    resolutionScale     = intSetting("resScale", "Resolution Scale", 1),
    termBackground      = intSetting("termBackground", "Terminal Background Color", 0xff000000),
    hoverTextColor      = intSetting("hoverTextColor", "Text Hover Color", EColors.aquamarine.intVal),
    termOpacity         = intSetting("termOpacity", "Terminal Opacity", 255).setRange(0, 255);
    
    //--------------------------------------------------------------------------------
    
    public static final StringConfigSetting
    
    lastEditorMap       = stringSetting("lastEditorMap", "Last Editor Map", ""),
    lastMap             = stringSetting("lastMap", "Last Map", ""),
    taskBarSide         = stringSetting("taskBarSide", "Task Bar Side", "top").setValidOptions("top", "left", "right", "bottom"),
    hudCloseMethod      = stringSetting("hudCloseMethod", "Hud Close Method", "hide").setValidOptions("hide", "close", "close all");
    
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
        settings.add(drawFPS);
        settings.add(fullscreen);
        settings.add(vsync);
        settings.add(termLineNumbers);
        settings.add(closeHudWhenEmpty);
        settings.add(batchRendering);
        
        // INTEGERS
        settings.add(musicVolume);
        settings.add(targetFPS);
        settings.add(targetUPS);
        settings.add(resolutionScale);
        settings.add(termBackground);
        settings.add(hoverTextColor);
        settings.add(termOpacity);
        
        // STRINGS
        settings.add(lastEditorMap);
        settings.add(lastMap);
        settings.add(taskBarSide);
        settings.add(hudCloseMethod);
    }
    
    public static EList<ConfigSetting<?>> getSettings() { return settings; }
    
    //--------------
    // initializers
    //--------------
    
    public static void init(File installDir, boolean useInternalResources) {
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
    
    public static File getResourcesDir() { return resourcesDir; }
    public static File getLocalGameDir() { return localGameDir; }
    public static File getSavesDir() { return savesDir; }
    public static File getEditorWorldsDir() { return editorWorldsDir; }
    public static File getMenuWorldsDir() { return menuWorldsDir; }
    
    public static MainConfigFile getGameConfig() { return mainConfig; }
    
}
