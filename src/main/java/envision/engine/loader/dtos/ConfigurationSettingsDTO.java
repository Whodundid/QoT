package envision.engine.loader.dtos;

import java.util.ArrayList;
import java.util.List;

import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.ConfigSettingDTO;
import eutil.datatypes.util.EList;

public record ConfigurationSettingsDTO(List<ConfigSettingDTO> settings) {
    
    //==============
    // Constructors
    //==============
    
    public ConfigurationSettingsDTO(ConfigSettingDTO... settings) {
        this(List.of(settings));
    }
    
    public ConfigurationSettingsDTO(EList<ConfigSettingDTO> setting) {
        this(setting.toArrayList());
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public static ConfigurationSettingsDTO fromConfigSettings(ConfigSetting<?>... settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.convertToDTO());
        return new ConfigurationSettingsDTO(l);
    }
    
    public static ConfigurationSettingsDTO fromConfigSettings(List<ConfigSetting<?>> settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.convertToDTO());
        return new ConfigurationSettingsDTO(l);
    }
    
    public static ConfigurationSettingsDTO fromConfigSettings(EList<ConfigSetting<?>> settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.convertToDTO());
        return new ConfigurationSettingsDTO(l);
    }
    
}
