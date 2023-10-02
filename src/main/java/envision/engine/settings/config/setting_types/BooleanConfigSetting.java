package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class BooleanConfigSetting extends ConfigSetting<Boolean> {

    public BooleanConfigSetting(String settingNameIn, String descriptionIn, boolean initialValue) {
        super(Boolean.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public BooleanConfigSetting setValidOptions(Boolean... argsIn) { validOptions.add(argsIn); return this; }
    @Override public BooleanConfigSetting setValidOptions(Collection<Boolean> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public BooleanConfigSetting setRange(Boolean min, Boolean max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public BooleanConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public BooleanConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public BooleanConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public boolean getBoolean() { return val; }
    public boolean getDefaultBoolean() { return defaultVal; }
    
    public void setBoolean(boolean val) { this.val = val; }
    public void setDefaultBoolean(boolean val) { this.defaultVal = val; }
    
}
