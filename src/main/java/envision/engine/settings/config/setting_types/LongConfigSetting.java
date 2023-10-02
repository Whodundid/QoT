package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class LongConfigSetting extends ConfigSetting<Long> {

    public LongConfigSetting(String settingNameIn, String descriptionIn, long initialValue) {
        super(Long.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public LongConfigSetting setValidOptions(Long... argsIn) { validOptions.add(argsIn); return this; }
    @Override public LongConfigSetting setValidOptions(Collection<Long> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public LongConfigSetting setRange(Long min, Long max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public LongConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public LongConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public LongConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public long getLong() { return val; }
    public long getDefaultLong() { return defaultVal; }
    
    public void setLong(long val) { this.val = val; }
    public void setDefaultLong(long val) { this.defaultVal = val; }
    
}
