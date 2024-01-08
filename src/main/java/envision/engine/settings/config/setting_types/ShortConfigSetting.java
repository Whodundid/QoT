package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class ShortConfigSetting extends ConfigSetting<Short> {

    public ShortConfigSetting(String name, String description) {
        this(name, description, (short) 0);
    }
    
    public ShortConfigSetting(String settingNameIn, String descriptionIn, short initialValue) {
        super(Short.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public ShortConfigSetting setValidOptions(Short... argsIn) { validOptions.add(argsIn); return this; }
    @Override public ShortConfigSetting setValidOptions(Collection<Short> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public ShortConfigSetting setRange(Short min, Short max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public ShortConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public ShortConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public ShortConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public short getShort() { return value; }
    public short getDefaultShort() { return defaultValue; }
    
    public void setShort(short val) { this.value = val; }
    public void setDefaultShort(short val) { this.defaultValue = val; }
    
}
