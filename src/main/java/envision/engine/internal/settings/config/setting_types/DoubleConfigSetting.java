package envision.engine.internal.settings.config.setting_types;

import java.util.Collection;

import envision.engine.loader.built.engine.ConfigSetting;

public class DoubleConfigSetting extends ConfigSetting<Double> {

    public DoubleConfigSetting(String name, String description) {
        this(name, description, 0.0);
    }
    
    public DoubleConfigSetting(String settingNameIn, String descriptionIn, double initialValue) {
        super(Double.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public DoubleConfigSetting setValidOptions(Double... argsIn) { validOptions.addA(argsIn); return this; }
    @Override public DoubleConfigSetting setValidOptions(Collection<Double> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public DoubleConfigSetting setRange(Double min, Double max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public DoubleConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public DoubleConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public DoubleConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public int getInt() { return value.intValue(); }
    public double getDouble() { return value.doubleValue(); }
    public double getDefaultDouble() { return defaultValue; }
    
    public void setDouble(double val) { this.value = val; }
    public void setDefaultDouble(double val) { this.defaultValue = val; }
    
}
