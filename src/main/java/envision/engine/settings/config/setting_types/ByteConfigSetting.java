package envision.engine.settings.config.setting_types;

import java.util.Collection;

import envision.engine.settings.config.ConfigSetting;

public class ByteConfigSetting extends ConfigSetting<Byte> {

    public ByteConfigSetting(String settingNameIn, String descriptionIn, byte initialValue) {
        super(Byte.class, settingNameIn, descriptionIn, initialValue);
    }
    
    @Override public ByteConfigSetting setValidOptions(Byte... argsIn) { validOptions.add(argsIn); return this; }
    @Override public ByteConfigSetting setValidOptions(Collection<Byte> argsIn) { validOptions.addAll(argsIn); return this; }
    @Override public ByteConfigSetting setRange(Byte min, Byte max) { minValue = min; maxValue = max; hasRange = true; return this; }
    @Override public ByteConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
    @Override public ByteConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
    @Override public ByteConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
    
    public byte getByte() { return val; }
    public byte getDefaultByte() { return defaultVal; }
    
    public void setByte(byte val) { this.val = val; }
    public void setDefaultByte(byte val) { this.defaultVal = val; }
    
}
