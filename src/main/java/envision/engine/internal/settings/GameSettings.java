package envision.engine.internal.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import envision.engine.loader.built.engine.ConfigSetting;
import envision.engine.loader.dtos.engine.ConfigurationSettingsDTO;
import eutil.datatypes.util.EList;

public class GameSettings {
    
    //========
    // Fields
    //========
    
    private final EList<ConfigSetting<?>> settings = EList.newList();
    private final Map<String, ConfigSetting<?>> settingsMap = new HashMap<>();    
    //==============
    // Constructors
    //==============
    
    public GameSettings() {
        // Nothing
    }
    
    public GameSettings(ConfigurationSettingsDTO dto) throws Exception {
        var dtoSettings = dto.settings();
        
        for (var dtoSetting : dtoSettings) {
            final ConfigSetting<?> setting = dtoSetting.fromDto();
            settings.add(setting);
            settingsMap.put(setting.getName(), setting);
        }
    }    
    //=========
    // Getters
    //=========
    
    public EList<ConfigSetting<?>> getConfigSettings() {
        return settings;
    }
    
    public ConfigSetting<?> getSettingByName(String name) {
        return settingsMap.get(name);
    }
    
    //============
    // Initialize
    //============
    
    public void initializeSettings(File installDir, boolean useInternalResources) {
        // ensure installation directory actually exists
        if (!installDir.exists()) {
            throw new RuntimeException(String.format("Failed to bind to installation directory '%s'!", installDir));
        }
        
        // initialize game directories using installation directory
        initializeGameDirectories(installDir, useInternalResources);
    }
    
    protected void initializeGameDirectories(File installDir, boolean useInternalResources) {
        
    }
    
}
