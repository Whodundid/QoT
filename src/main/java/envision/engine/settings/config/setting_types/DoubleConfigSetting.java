package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class DoubleConfigSetting extends ConfigSetting<Double> {

    public DoubleConfigSetting(String name, String description) {
        this(name, description, 0.0);
    }
    
    public DoubleConfigSetting(String settingNameIn, String descriptionIn, double initialValue) {
        super(Double.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public DoubleConfigSetting setValidOptions(Double... argsIn) { validOptions.add(argsIn); return this; }
    @Override public DoubleConfigSetting setValidOptions(Collection<Double> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public DoubleConfigSetting setRange(Double min, Double max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public DoubleConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public DoubleConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public DoubleConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public double getDouble() { return value; }
    public double getDefaultDouble() { return defaultValue; }
    
    public void setDouble(double val) { this.value = val; }
    public void setDefaultDouble(double val) { this.defaultValue = val; }
    
}
