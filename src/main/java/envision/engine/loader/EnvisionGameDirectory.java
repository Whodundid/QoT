package envision.engine.loader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;

public class EnvisionGameDirectory {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private EngineSettingsDTO engineSettings;
    private WindowSettingsDTO windowSettings;
    private EnvisionGameDTO envisionGame;
    
    private File gameDir;
    private File resourcesDir;
    private File scriptsDir;
    private File gameLoadFile;
    private Map<String, File> gameDirectory = new HashMap<>();
    
    public EnvisionGameDirectory(File gameDirIn) {
        gameDir = gameDirIn;
        
        exploreDirectory();
        parseGameFile();
        
        System.out.println(engineSettings);
        System.out.println(windowSettings);
        System.out.println(envisionGame);
    }
    
    private void exploreDirectory() {
        gameLoadFile = EFileUtil.findFile(gameDir, "envision_game.json");
        
        final var files = gameDir.listFiles();
        
        for (File f : files) {
            String fName = f.getName();
            gameDirectory.put(fName, f);
        }
    }
    
    private void parseGameFile() {
        if (gameLoadFile == null) throw new NullPointerException("There is no game file!");
        
        var parser = new JSONParser();
        try (var reader = new FileReader(gameLoadFile)) {
            JSONObject loadFile = (JSONObject) parser.parse(reader);
            
            var keys = loadFile.keySet();
            
            for (Object key : keys) {
                String elementType = String.valueOf(key);
                Object value = loadFile.get(key);
                
                try {
                    parseElement(elementType, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void parseElement(String elementType, Object value) throws Exception {
        if (elementType == null) return;
        
        switch (elementType) {
        case "EngineSettings":
            engineSettings = parseEngineSettings(value);
            break;
        case "WindowSettings":
            windowSettings = parseWindowSettings(value);
            break;
        case "EnvisionGame":
            envisionGame = parseEnvisionGame(value);
            break;
        default:
            throw new RuntimeException("Unrecognized Envision load file element: '" + elementType + "'");
        }
    }
    
    private EngineSettingsDTO parseEngineSettings(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), EngineSettingsDTO.class);
    }

    private WindowSettingsDTO parseWindowSettings(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), WindowSettingsDTO.class);
    }
    
    private EnvisionGameDTO parseEnvisionGame(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), EnvisionGameDTO.class);
    }
    
    //=========
    // Getters
    //=========
    
    public File getResourcesDir() { return resourcesDir; }
    public File getScriptsDir() { return scriptsDir; }
    public File getGameLoadFile() { return gameLoadFile; }
    
    public EList<File> getAdditionalFiles() {
        return EList.wrap(List.copyOf(gameDirectory.values()));
    }
    
    public File getAdditionalFileByName(String name) { return gameDirectory.get(name); }
    
}
