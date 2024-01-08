package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class EnumConfigSetting<T extends Enum> extends ConfigSetting<T> {

    public EnumConfigSetting(String enumClassName, String name, String description) {
        super(enumClassName, name, description);
    }
    
    public EnumConfigSetting(String settingNameIn, String descriptionIn, T initialValue) {
        super((Class<T>) Enum.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public EnumConfigSetting setValidOptions(T... argsIn) { validOptions.add(argsIn); return this; }
    @Override public EnumConfigSetting setValidOptions(Collection<T> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public EnumConfigSetting setRange(T min, T max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public EnumConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public EnumConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public EnumConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public T getEnum() { return value; }
    public T getDefaultEnum() { return defaultValue; }
    
    public void setEnum(T val) { this.value = val; }
    public void setDefaultEnum(T val) { this.defaultValue = val; }
    
}
