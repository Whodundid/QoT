package envision.engine.loader.dtos.engine;

import java.util.ArrayList;
import java.util.List;

import envision.engine.loader.built.engine.ConfigSetting;
import eutil.datatypes.util.EList;
import eutil.strings.EToStringBuilder;

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
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        EToStringBuilder sb = new EToStringBuilder(this);
        for (var s : settings) {
            sb.a(s.name(), s.defaultValue());
        }
        return sb.toString();
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public static ConfigurationSettingsDTO fromConfigSettings(ConfigSetting<?>... settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.toDto());
        return new ConfigurationSettingsDTO(l);
    }
    
    public static ConfigurationSettingsDTO fromConfigSettings(List<ConfigSetting<?>> settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.toDto());
        return new ConfigurationSettingsDTO(l);
    }
    
    public static ConfigurationSettingsDTO fromConfigSettings(EList<ConfigSetting<?>> settings) {
        List<ConfigSettingDTO> l = new ArrayList<>();
        for (ConfigSetting<?> s : settings) l.add(s.toDto());
        return new ConfigurationSettingsDTO(l);
    }
    
}
