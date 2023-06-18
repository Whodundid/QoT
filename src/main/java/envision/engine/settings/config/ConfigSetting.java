package envision.engine.settings.config;

import static eutil.datatypes.util.JavaDatatype.*;

import java.util.Collection;

import eutil.EUtil;
import eutil.datatypes.util.EList;
import eutil.datatypes.util.JavaDatatype;
import eutil.math.ENumUtil;

//Author: Hunter Bragg

public class ConfigSetting<T> {
	
	private String settingName = "";
	private String description = "";
	private boolean requiresDev = false;
	private T val = null;
	private T defaultVal = null;
	private JavaDatatype type;
	private EList<T> validOptions = EList.newList();
	private final Class<T> cType;
	private boolean ignoreConfigRead = false;
	private boolean ignoreConfigWrite = false;
	private boolean hasRange = false;
	private T minValue;
	private T maxValue;
	
	//--------------
	// Constructors
	//--------------
	
	public ConfigSetting(Class<T> cTypeIn, String settingNameIn, String descriptionIn, T initialValue) {
		settingName = settingNameIn;
		val = initialValue;
		defaultVal = initialValue;
		cType = cTypeIn;
		description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
		parseDataType();
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
	    return "{" + settingName + ":" + val + "}";
	}
	
	//---------
	// Methods
	//---------
	
	public ConfigSetting reset() { set(defaultVal); return this; }
	
	public T toggle() {
		if (cType.isAssignableFrom(Boolean.class)) {
			Boolean b = (Boolean) cType.cast(val);
			b = !b;
			set(cType.cast(b));
		}
		return val;
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return String.valueOf(val); }
	public String defAsString() { return String.valueOf(defaultVal); }
	public JavaDatatype getType() { return type; }
	public Class<T> getClassType() { return cType; }
	public T get() { return val; }
	public boolean getRequiresDev() { return requiresDev; }
	public EList<T> getValidOptions() { return validOptions; }
	public T getMinValue() { return minValue; }
	public T getMaxValue() { return maxValue; }
	public T getDefault() { return defaultVal; }
	public boolean getIgnoreConfigRead() { return ignoreConfigRead; }
	public boolean getIgnoreConfigWrite() { return ignoreConfigWrite; }
	public boolean hasRange() { return hasRange; }
	
	//---------
	// Setters
	//---------
	
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
            
            return val = (T) cType.cast(obj);
        }
        
        return val = valIn;
	}
	
	public ConfigSetting<T> setValidOptions(T... argsIn) { validOptions.add(argsIn); return this; }
	public ConfigSetting<T> setValidOptions(Collection<T> argsIn) { validOptions.addAll(argsIn); return this; }
	public ConfigSetting<T> setRange(T min, T max) { minValue = min; maxValue = max; hasRange = true; return this; }
	public ConfigSetting<T> setDevSetting(boolean val) { requiresDev = val; return this; }
	public ConfigSetting<T> setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
	public ConfigSetting<T> setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
	
	//------------------
	// Internal Methods
	//------------------
	
	private void parseDataType() {
		if (val == null) type = OBJECT;
		
		Class c = val.getClass();
		try {
			if (c.isAssignableFrom(Boolean.class)) 			type = BOOLEAN;
			else if (c.isAssignableFrom(Character.class))   type = CHAR;
			else if (c.isAssignableFrom(Byte.class)) 		type = BYTE;
			else if (c.isAssignableFrom(Short.class)) 		type = SHORT;
			else if (c.isAssignableFrom(Integer.class)) 	type = INT;
			else if (c.isAssignableFrom(Long.class)) 		type = LONG;
			else if (c.isAssignableFrom(Float.class)) 		type = FLOAT;
			else if (c.isAssignableFrom(Double.class)) 		type = DOUBLE;
			else if (c.isAssignableFrom(String.class))		type = STRING;
			else if (c.isAssignableFrom(Enum.class)) 		type = ENUM;
			else if (c != null) 							type = OBJECT;
		}
		catch (ClassCastException e) {
			e.printStackTrace();
			type = OBJECT;
		}
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static void reset(ConfigSetting setting, ConfigSetting... additional) {
		EUtil.filterNullForEachA(s -> s.reset(), EUtil.add(setting, additional));
	}
	
	// Static Helpers
	
	public static ConfigSetting<Boolean> boolSetting(String internalName, String displayName, Boolean defaultValue) {
		return new ConfigSetting(Boolean.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Character> charSetting(String internalName, String displayName, Character defaultValue) {
		return new ConfigSetting(Character.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Byte> byteSetting(String internalName, String displayName, Byte defaultValue) {
		return new ConfigSetting(Byte.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Short> shortSetting(String internalName, String displayName, Short defaultValue) {
		return new ConfigSetting(Short.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Integer> intSetting(String internalName, String displayName, Integer defaultValue) {
		return new ConfigSetting(Integer.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Long> longSetting(String internalName, String displayName, Long defaultValue) {
		return new ConfigSetting(Long.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Float> floatSetting(String internalName, String displayName, Float defaultValue) {
		return new ConfigSetting(Integer.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<Double> doubleSetting(String internalName, String displayName, Double defaultValue) {
		return new ConfigSetting(Integer.class, internalName, displayName, defaultValue);
	}
	public static ConfigSetting<String> stringSetting(String internalName, String displayName, String defaultValue) {
		return new ConfigSetting(String.class, internalName, displayName, defaultValue);
	}
	public static <T extends Enum> ConfigSetting<T> enumSetting(String internalName, String displayName, T defaultValue) {
		return new ConfigSetting(Enum.class, internalName, displayName, defaultValue);
	}
	
}
