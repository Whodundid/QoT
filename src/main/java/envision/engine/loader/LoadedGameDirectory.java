package envision.engine.loader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import envision.engine.loader.dtos.ConfigurationSettingsDTO;
import envision.engine.loader.dtos.EngineSettingsDTO;
import envision.engine.loader.dtos.EnvisionGameDTO;
import envision.engine.loader.dtos.WindowSettingsDTO;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;

public class LoadedGameDirectory {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private EnvisionGameDTO envisionGame;
    private EngineSettingsDTO engineSettings;
    private WindowSettingsDTO windowSettings;
    private ConfigurationSettingsDTO configurationSettings;
    
    private File gameDir;
    private File gameLoadFile;
    private Map<String, File> gameDirectory = new HashMap<>();
    
    public LoadedGameDirectory(File gameDirIn) {
        gameDir = gameDirIn;
        
        exploreDirectory();
        parseGameFile();
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
        case "EnvisionGame":
            envisionGame = parseEnvisionGame(value);
            break;
        case "EngineSettings":
            engineSettings = parseEngineSettings(value);
            break;
        case "WindowSettings":
            windowSettings = parseWindowSettings(value);
            break;
        case "ConfigurationSettings":
            configurationSettings = parseConfigurationSettings(value);
            break;
        default:
            throw new RuntimeException("Unrecognized Envision load file element: '" + elementType + "'");
        }
    }
    
    private EnvisionGameDTO parseEnvisionGame(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), EnvisionGameDTO.class);
    }

    private EngineSettingsDTO parseEngineSettings(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), EngineSettingsDTO.class);
    }

    private WindowSettingsDTO parseWindowSettings(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), WindowSettingsDTO.class);
    }
    
    private ConfigurationSettingsDTO parseConfigurationSettings(Object value) throws Exception {
        return mapper.readValue(String.valueOf(value), ConfigurationSettingsDTO.class);
    }
    
    //=========
    // Getters
    //=========
    
    public File getDirectory() { return gameDir; }
    public File getGameLoadFile() { return gameLoadFile; }
    
    public EnvisionGameDTO getGameDTO() { return envisionGame; }
    public EngineSettingsDTO getEngineSettingsDTO() { return engineSettings; }
    public WindowSettingsDTO getWindowSettingsDTO() { return windowSettings; }
    public ConfigurationSettingsDTO getConfigurationSettings() { return configurationSettings; }
    
    public EList<File> getAdditionalFiles() {
        return EList.wrap(List.copyOf(gameDirectory.values()));
    }
    
    public File getAdditionalFileByName(String name) { return gameDirectory.get(name); }
    
}
