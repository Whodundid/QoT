package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class IntegerConfigSetting extends ConfigSetting<Integer> {

    public IntegerConfigSetting(String settingNameIn, String descriptionIn, int initialValue) {
        super(Integer.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public IntegerConfigSetting setValidOptions(Integer... argsIn) { validOptions.add(argsIn); return this; }
    @Override public IntegerConfigSetting setValidOptions(Collection<Integer> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public IntegerConfigSetting setRange(Integer min, Integer max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public IntegerConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public IntegerConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public IntegerConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public int getInt() { return val; }
    public int getDefaultInt() { return defaultVal; }
    
    public void setInt(int val) { this.val = val; }
    public void setDefaultInt(int val) { this.defaultVal = val; }
    
}
