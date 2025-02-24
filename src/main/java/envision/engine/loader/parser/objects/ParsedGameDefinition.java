package envision.engine.loader.parser.objects;

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import envision.engine.loader.dtos.engine.ConfigurationSettingsDTO;
import envision.engine.loader.dtos.engine.EngineSettingsDTO;
import envision.engine.loader.dtos.engine.EnvisionGameDTO;
import envision.engine.loader.dtos.engine.WindowSettingsDTO;

public class ParsedGameDefinition {
    
    //===============
    // Static Fields
    //===============
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    //========
    // Fields
    //========
    
    private final File gameDefJsonFile;
    private EnvisionGameDTO envisionGame;
    private EngineSettingsDTO engineSettings;
    private WindowSettingsDTO windowSettings;
    private ConfigurationSettingsDTO configurationSettings;
    
    //==============
    // Constructors
    //==============
    
    public ParsedGameDefinition(File gameDefJsonFile) {
        this.gameDefJsonFile = gameDefJsonFile;
        
        parseGameFile();
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void parseGameFile() {
        if (gameDefJsonFile == null) throw new NullPointerException("There is no game file!");
        
        var parser = new JSONParser();
        try (var reader = new FileReader(gameDefJsonFile)) {
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
    
    public EnvisionGameDTO getGameDTO() { return envisionGame; }
    public EngineSettingsDTO getEngineSettingsDTO() { return engineSettings; }
    public WindowSettingsDTO getWindowSettingsDTO() { return windowSettings; }
    public ConfigurationSettingsDTO getConfigurationSettings() { return configurationSettings; }
    
}
