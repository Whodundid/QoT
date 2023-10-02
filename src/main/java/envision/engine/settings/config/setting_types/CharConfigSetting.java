package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class CharConfigSetting extends ConfigSetting<Character> {

    public CharConfigSetting(String settingNameIn, String descriptionIn, char initialValue) {
        super(Character.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public CharConfigSetting setValidOptions(Character... argsIn) { validOptions.add(argsIn); return this; }
    @Override public CharConfigSetting setValidOptions(Collection<Character> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public CharConfigSetting setRange(Character min, Character max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public CharConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public CharConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public CharConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public char getChar() { return val; }
    public char getDefaultChar() { return defaultVal; }
    
    public void setChar(char val) { this.val = val; }
    public void setDefaultChar(char val) { this.defaultVal = val; }
    
}
