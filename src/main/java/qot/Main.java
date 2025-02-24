package qot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import envision.engine.loader.parser.EngineStructureFileParser;
import eutil.file.EFileUtil;
import eutil.sys.OSType;
import eutil.sys.TracingPrintStream;

public class Main {

    //-------------------------------------
    // Runner Settings
    //-------------------------------------
    // Modify these values for game launch
    //-------------------------------------
    
    /**
     * Runs the game's launcher instead of directly out of a dev environment.
     */
    public static final boolean RUN_LAUNCHER = true;
    
    /** Uses the internal jar resources instead of the install dir's. */
    public static final boolean USE_INTERNAL_RESOURCES = false;
    
    /**
     * Instructs the launcher/installer on whether or not the game's internal
     * resources should be extracted to the install dir.
     * <p>
     * Right now, there are not a ton of resources so loading times are still pretty
     * quick regardless, but this is likely to change in future builds as the game
     * develops.
     * <p>
     * NOTE: If 'USE_INTERNAL_RESOURCES' is false then this value NEEDS to be true
     * otherwise the game will break if not already installed!
     */
    public static final boolean EXTRACT_RESOURCES = false;
    
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    
    /**
     * Does not run QoT, instead runs an OpenGL testing environment instead.
     */
    public static final boolean RUN_OPEN_GL_TESTING_ENVIRONMENT = false;
    
    //======
    // Main
    //======
    
    public static void main(String[] args) throws Exception {
        // setup print tracing
        TracingPrintStream.enableTrace();
        TracingPrintStream.setTracePrimitives(true);
        TracingPrintStream.enableTracingEmptyLines(true);
        TracingPrintStream.disableTrace();
        
        //-------------------------------------------
        
//        String dir = "C:/Users/Hunter/AppData/Roaming/Quest of Thyrah/resources/textures/world/nature/grass";
//        String fileName = "grass.png";
//        File outFile = new File(dir, fileName);
//        
//        EList<TextureResourceDTO> textures = EList.newList();
//        for (int i = 0; i < 1; i++) {
//            String n = RandomNames.get();
//            textures.add(new TextureResourceDTO(n, outFile.getAbsolutePath(), "GL_NEAREST", "GL_NEAREST"));
//        }
//        
//        JsonReaderAndWriter<TextureResourceDTO> writer = new JsonReaderAndWriter<>(TextureResourceDTO.class);
//        File jsonOut = new File(dir, "grass.json");
//        writer.writeToFile(jsonOut, textures.toList());
        
        //------------------------------------------------------------------
        
        File gameDir = new File("C:/Users/Hunter/AppData/Roaming/Quest of Thyrah");
        File gameFile = new File(gameDir, "game.ini");
        
        Map<String, String> varMap = new HashMap<>();
        varMap.put("user.dir", EFileUtil.userDir().toString().replace(OSType.getSystemFileSeparator(), "/"));
        varMap.put("gameDir", gameDir.toString().replace(OSType.getSystemFileSeparator(), "/"));
        varMap.put("operatingSystem", OSType.getSystemOS().toString());
        
        EngineStructureFileParser parser = new EngineStructureFileParser(gameDir, gameFile, varMap);
        
        parser.parseRootFile();
        
//        Envision.loadGame("C:/Users/Hunter/AppData/Roaming/Quest of Thyrah");
//        
//        Envision.startEngine();
        
//        var settings = new LauncherSettings(QoT.instance());
//        settings.setResourceDirectoriesToExtract("sounds", "shaders", "textures", "menuWorlds");
//        
//        EnvisionGameLauncher.runLauncher(settings);
        
//        // if opting to use the launcher
//        if (RUN_LAUNCHER) {
//            QoTLauncher.runLauncher();
//        }
//        // otherwise if opting to run directly from a dev environment
//        else {
//            try {
//                // create fake launcher settings to bind resource paths
//                var settings = new LauncherSettings();
//                settings.INSTALL_DIR = EnvisionGameInstaller.getDefaultInstallDir();
//                
//                // creates the base install directory for settings/resources/worlds/etc.
//                if (!EnvisionGameInstaller.verifyActuallyInstalled(settings, settings.INSTALL_DIR)) {
//                    EnvisionGameInstaller.createInstallDir(settings.INSTALL_DIR, EXTRACT_RESOURCES);
//                }
//                
//                settings.USE_INTERNAL_RESOURCES_PATH = USE_INTERNAL_RESOURCES;
//                
//                //start the game with the fake settings
//                QoT.startGame(settings);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    
}
