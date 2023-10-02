package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class FloatConfigSetting extends ConfigSetting<Float> {

    public FloatConfigSetting(String settingNameIn, String descriptionIn, float initialValue) {
        super(Float.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public FloatConfigSetting setValidOptions(Float... argsIn) { validOptions.add(argsIn); return this; }
    @Override public FloatConfigSetting setValidOptions(Collection<Float> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public FloatConfigSetting setRange(Float min, Float max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public FloatConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public FloatConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public FloatConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public float getFloat() { return val; }
    public float getDefaultFloat() { return defaultVal; }
    
    public void setFloat(float val) { this.val = val; }
    public void setDefaultFloat(float val) { this.defaultVal = val; }
    
}