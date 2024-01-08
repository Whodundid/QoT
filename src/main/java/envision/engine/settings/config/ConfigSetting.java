package envision.engine.settings.config;

import static eutil.datatypes.util.JavaDatatype.*;

import java.util.Collection;

import envision.engine.settings.config.setting_types.*;
import eutil.EUtil;
import eutil.datatypes.util.EList;
import eutil.datatypes.util.JavaDatatype;
import eutil.math.ENumUtil;

//Author: Hunter Bragg

public class ConfigSetting<T> {
	
	protected String settingName = "";
	protected String description = "";
	protected boolean requiresDev = false;
	protected T value = null;
	protected T defaultValue = null;
	protected JavaDatatype type;
	protected EList<T> validOptions = EList.newList();
	protected String className;
	protected Class<T> cType;
	protected boolean ignoreConfigRead = false;
	protected boolean ignoreConfigWrite = false;
	protected boolean hasRange = false;
	protected T minValue;
	protected T maxValue;
	
	//==============
	// Constructors
	//==============
	
	public ConfigSetting(String classNameIn, String settingNameIn, String descriptionIn) {
	    settingName = settingNameIn;
	    className = classNameIn;
	    try {
	        cType = (Class<T>) Class.forName(className);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
	    parseDataType();
	}
	
	public ConfigSetting(Class<T> cTypeIn, String settingNameIn, String descriptionIn, T initialValue) {
		settingName = settingNameIn;
		value = initialValue;
		defaultValue = initialValue;
		cType = cTypeIn;
		className = cType.getName();
		description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
		parseDataType();
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
	    return "{" + settingName + ":" + value + "}";
	}
	
	//=========
	// Methods
	//=========
	
	public ConfigSetting<T> reset() { set(defaultValue); return this; }
	
	public T toggle() {
		if (cType.isAssignableFrom(Boolean.class)) {
			Boolean b = (Boolean) cType.cast(value);
			b = !b;
			set(cType.cast(b));
		}
		return value;
	}
	
	public ConfigSettingDTO convertToDTO() {
	    return new ConfigSettingDTO(settingName, description, cType.getName(), String.valueOf(defaultValue));
	}
	
	//=========
	// Getters
	//=========
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return String.valueOf(value); }
	public String defAsString() { return String.valueOf(defaultValue); }
	public JavaDatatype getType() { return type; }
	public Class<T> getClassType() { return cType; }
	public T get() { return value; }
	public boolean getRequiresDev() { return requiresDev; }
	public EList<T> getValidOptions() { return validOptions; }
	public T getMinValue() { return minValue; }
	public T getMaxValue() { return maxValue; }
	public T getDefault() { return defaultValue; }
	public boolean getIgnoreConfigRead() { return ignoreConfigRead; }
	public boolean getIgnoreConfigWrite() { return ignoreConfigWrite; }
	public boolean hasRange() { return hasRange; }
	
	//==========
	// Setters
	//==========
	
    public T set(T valIn) {
        if (hasRange && cType.isAssignableFrom(Number.class)) {
            var obj = switch (type) {
            case BYTE -> ENumUtil.clamp((byte) valIn, (byte) minValue, (byte) maxValue);
            case SHORT -> ENumUtil.clamp((short) valIn, (short) minValue, (short) maxValue);
            case INT -> ENumUtil.clamp((int) valIn, (int) minValue, (int) maxValue);
            case LONG -> ENumUtil.clamp((long) valIn, (long) minValue, (long) maxValue);
            case FLOAT -> ENumUtil.clamp((float) valIn, (float) minValue, (float) maxValue);
            case DOUBLE -> ENumUtil.clamp((double) valIn, (double) minValue, (double) maxValue);
            default ->
                throw new RuntimeException("No idea how to cast '" + valIn + "' to a '" + cType + "'");
            };
            
            value = cType.cast(obj);
            return value;
        }
        
        value = valIn;
        return value;
	}
	
    /**
     * Sets the default value and immediately applies it as this config
     * setting's value.
     * 
     * @param value The default value to set
     * 
     * @return This config setting
     */
    public ConfigSetting<T> setDefaultAndApply(T value) {
        defaultValue = value;
        this.value = defaultValue;
        return this;
    }
    
	public ConfigSetting<T> setValidOptions(T... argsIn) { validOptions.add(argsIn); return this; }
	public ConfigSetting<T> setValidOptions(Collection<T> argsIn) { validOptions.addAll(argsIn); return this; }
	public ConfigSetting<T> setRange(T min, T max) { minValue = min; maxValue = max; hasRange = true; return this; }
	public ConfigSetting<T> setDevSetting(boolean val) { requiresDev = val; return this; }
	public ConfigSetting<T> setDefaultValue(T value) { defaultValue = value; return this; }
	public ConfigSetting<T> setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
	public ConfigSetting<T> setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
	
	//==================
	// Internal Methods
	//==================
	
	private void parseDataType() {
		if (value == null) {
		    type = OBJECT;
		    return;
		}
		
		Class<?> c = value.getClass();
		try {
			if (c.isAssignableFrom(Boolean.class))           type = BOOLEAN;
			else if (c.isAssignableFrom(Character.class))    type = CHAR;
			else if (c.isAssignableFrom(Byte.class))         type = BYTE;
			else if (c.isAssignableFrom(Short.class))        type = SHORT;
			else if (c.isAssignableFrom(Integer.class))      type = INT;
			else if (c.isAssignableFrom(Long.class))         type = LONG;
			else if (c.isAssignableFrom(Float.class))        type = FLOAT;
			else if (c.isAssignableFrom(Double.class))       type = DOUBLE;
			else if (c.isAssignableFrom(String.class))       type = STRING;
			else if (c.isAssignableFrom(Enum.class))         type = ENUM;
			else                                             type = OBJECT;
		}
		catch (ClassCastException e) {
			e.printStackTrace();
			type = OBJECT;
		}
	}
	
	//================
	// Static Methods
	//================
	
	public static void reset(ConfigSetting<?> setting, ConfigSetting<?>... additional) {
		EUtil.filterNullForEachA(s -> s.reset(), EUtil.add(setting, additional));
	}
	
	//================
	// Static Helpers
	//================
	
	public static BooleanConfigSetting boolSetting(String internalName, String displayName, boolean defaultValue) {
		return new BooleanConfigSetting(internalName, displayName, defaultValue);
	}
	public static CharConfigSetting charSetting(String internalName, String displayName, char defaultValue) {
		return new CharConfigSetting(internalName, displayName, defaultValue);
	}
	public static ByteConfigSetting byteSetting(String internalName, String displayName, byte defaultValue) {
		return new ByteConfigSetting(internalName, displayName, defaultValue);
	}
	public static ShortConfigSetting shortSetting(String internalName, String displayName, short defaultValue) {
		return new ShortConfigSetting(internalName, displayName, defaultValue);
	}
	public static IntegerConfigSetting intSetting(String internalName, String displayName, int defaultValue) {
		return new IntegerConfigSetting(internalName, displayName, defaultValue);
	}
	public static LongConfigSetting longSetting(String internalName, String displayName, long defaultValue) {
		return new LongConfigSetting(internalName, displayName, defaultValue);
	}
	public static FloatConfigSetting floatSetting(String internalName, String displayName, float defaultValue) {
		return new FloatConfigSetting(internalName, displayName, defaultValue);
	}
	public static DoubleConfigSetting doubleSetting(String internalName, String displayName, double defaultValue) {
		return new DoubleConfigSetting(internalName, displayName, defaultValue);
	}
	public static StringConfigSetting stringSetting(String internalName, String displayName, String defaultValue) {
		return new StringConfigSetting(internalName, displayName, defaultValue);
	}
	public static <T extends Enum> EnumConfigSetting<T> enumSetting(String internalName, String displayName, T defaultValue) {
		return new EnumConfigSetting(internalName, displayName, defaultValue);
	}
	
}
