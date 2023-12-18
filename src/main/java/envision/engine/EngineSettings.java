package envision.engine;

import static envision.engine.settings.config.ConfigSetting.*;
import static envision.launcher.EnvisionGameInstaller.*;

import java.io.File;
import java.nio.file.Files;

import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.setting_types.BooleanConfigSetting;
import envision.engine.settings.config.setting_types.IntegerConfigSetting;
import envision.engine.settings.config.setting_types.StringConfigSetting;
import envision.engine.terminal.terminalUtil.ESystemInfo;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.sys.OSType;

public class EngineSettings {
    
    //==============
    // Constructors
    //==============
    
    private EngineSettings() {}
    
    //========
    // Fields
    //========
    
    public static final String DEFAULT_WINDOWS_INSTALL_DIR = "\\AppData\\Roaming";
    public static final String DEFAULT_LINUX_INSTALL_DIR = "/opt";
    
    public static final File ENGINE_DIR = new File(getEnginePlatformDirectory(), "Envision Engine");
    public static final File RESOURCES_DIR = new File(ENGINE_DIR, "resources");
    public static final File EDITOR_WORLDS_DIR = new File(ENGINE_DIR, "editorWorlds");
    public static final File ENGINE_CONFIG_FILE = new File(ENGINE_DIR, "Enivision Config.ini");
    
    public static final EngineConfig ENGINE_CONFIG = new EngineConfig(ENGINE_CONFIG_FILE);
    
    private static final EList<ConfigSetting<?>> settings = EList.newList();
    
    //-------------------------------------------------------------------------------
    
    public static final BooleanConfigSetting
    
    drawFPS             = boolSetting("drawFPS", "Draw FPS", false),
    fullscreen          = boolSetting("fullscreen", "Fullscreen", false),
    vsync               = boolSetting("vSync", "V-Sync", false),
    termLineNumbers     = boolSetting("termLineNumbers", "Show Terminal Line Numbers", false),
    batchRendering      = boolSetting("batchRendering", "Enable Batch Rendering", true),
    closeHudWhenEmpty   = boolSetting("closeHudWhenEmpty", "Close Hud when Empty", true);
    
    //-------------------------------------------------------------------------------
    
    public static final IntegerConfigSetting
    
    targetFPS           = intSetting("targetFPS", "Target FPS", 60),
    targetUPS           = intSetting("targetUPS", "Target UPS", 150),
    resolutionScale     = intSetting("resScale", "Resolution Scale", 1),
    termBackground      = intSetting("termBackground", "Terminal Background Color", 0xff000000),
    hoverTextColor      = intSetting("hoverTextColor", "Text Hover Color", EColors.aquamarine.intVal),
    termOpacity         = intSetting("termOpacity", "Terminal Opacity", 255).setRange(0, 255);
    
    //public static final IntegerConfigSetting
    
    // ;
    
    //--------------------------------------------------------------------------------
    
    public static final StringConfigSetting
    
    lastEditorMap       = stringSetting("lastEditorMap", "Last Editor Map", ""),
    lastMap             = stringSetting("lastMap", "Last Map", ""),
    taskBarSide         = stringSetting("taskBarSide", "Task Bar Side", "top").setValidOptions("top", "left", "right", "bottom"),
    hudCloseMethod      = stringSetting("hudCloseMethod", "Hud Close Method", "hide").setValidOptions("hide", "close", "close all");
    
    //===============================================================================
    //===============================================================================
    
    //=======
    // Setup
    //=======
    
    static {
        try {
            // create directories
            if (!ENGINE_DIR.exists()) Files.createDirectory(ENGINE_DIR.toPath());
            if (!RESOURCES_DIR.exists()) Files.createDirectory(RESOURCES_DIR.toPath());
            if (!EDITOR_WORLDS_DIR.exists()) Files.createDirectory(EDITOR_WORLDS_DIR.toPath());
            
            if (!verifyDir("font", RESOURCES_DIR)) extractDataToDir("font", RESOURCES_DIR);
            if (!verifyDir("textures", RESOURCES_DIR)) extractDataToDir("textures", RESOURCES_DIR);
            
            // BOOLEANS
            settings.add(drawFPS);
            settings.add(fullscreen);
            settings.add(vsync);
            settings.add(termLineNumbers);
            settings.add(closeHudWhenEmpty);
            settings.add(batchRendering);
            
            // INTEGERS
            settings.add(targetFPS);
            settings.add(targetUPS);
            settings.add(resolutionScale);
            settings.add(termBackground);
            settings.add(hoverTextColor);
            settings.add(termOpacity);
            
            // STRINGS
            settings.add(taskBarSide);
            settings.add(hudCloseMethod);
            
            // prepare the engine's config file
            if (!ENGINE_CONFIG.tryLoad()) {
                throw new RuntimeException("Failed to load Envision Config!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static final String getEnginePlatformDirectory() {
        //determine user OS and get their home directory
        OSType os = ESystemInfo.getOS();
        String homeDir = System.getProperty("user.home");
        
        String dir = null;
        
        switch (os) {
        case WINDOWS: dir = homeDir + DEFAULT_WINDOWS_INSTALL_DIR; break;
        case MAC:
        case LINUX:
        case SOLARIS: //no idea how to handle yet
        default:
            //not sure how to setup config for other os's except windows
        }
        
        return dir;
    }
    
    //=========
    // Getters
    //=========
    
    public static EList<ConfigSetting<?>> getSettings() { return settings; }
    
}
