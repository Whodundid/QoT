package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class StringConfigSetting extends ConfigSetting<String> {

    public StringConfigSetting(String name, String description) {
        this(name, description, "");
    }
    
    public StringConfigSetting(String settingNameIn, String descriptionIn, String initialValue) {
        super(String.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public StringConfigSetting setValidOptions(String... argsIn) { validOptions.add(argsIn); return this; }
    @Override public StringConfigSetting setValidOptions(Collection<String> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public StringConfigSetting setRange(String min, String max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public StringConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public StringConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public StringConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public String getString() { return value; }
    public String getDefaultString() { return defaultValue; }
    
    public void setString(String val) { this.value = val; }
    public void setDefaultString(String val) { this.defaultValue = val; }
    
}
