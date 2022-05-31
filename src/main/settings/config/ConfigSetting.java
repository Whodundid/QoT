package main.settings.config;

import static eutil.datatypes.util.DataTypeUtil.*;

import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.DataTypeUtil;

import java.util.Collection;

//Author: Hunter Bragg

public class ConfigSetting<T> {
	
	private String settingName = "";
	private String description = "";
	private boolean requiresDev = false;
	private T val = null;
	private T defaultVal = null;
	private DataTypeUtil type;
	private EArrayList additionalArgs = new EArrayList();
	private final Class<T> cType;
	private boolean ignoreConfigRead = false;
	private boolean ignoreConfigWrite = false;
	
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
	
	//---------
	// Methods
	//---------
	
	public ConfigSetting reset() { set(defaultVal); return this; }
	
	public ConfigSetting toggle() {
		if (cType.isAssignableFrom(Boolean.class)) {
			Boolean b = (Boolean) cType.cast(val);
			b = !b;
			set(cType.cast(b));
		}
		return this;
	}
	
	//---------
	// Getters
	//---------
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return val != null ? val.toString() : "null"; }
	public String defAsString() { return defaultVal != null ? defaultVal.toString() : "null"; }
	public DataTypeUtil getType() { return type; }
	public Class<T> getClassType() { return cType; }
	public T get() { return val; }
	public boolean getRequiresDev() { return requiresDev; }
	public EArrayList getArgs() { return additionalArgs; }
	public T getDefault() { return defaultVal; }
	public boolean getIgnoreConfigRead() { return ignoreConfigRead; }
	public boolean getIgnoreConfigWrite() { return ignoreConfigWrite; }
	
	//---------
	// Setters
	//---------
	
	public ConfigSetting set(T valIn) { val = valIn; return this; }
	public ConfigSetting setArgs(Object... argsIn) { additionalArgs.add(argsIn); return this; }
	public ConfigSetting setArgs(Collection argsIn) { additionalArgs.addAll(argsIn); return this; }
	public ConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
	public ConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
	public ConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
	
	//------------------
	// Internal Methods
	//------------------
	
	private void parseDataType() {
		if (val == null) type = OBJECT;
		
		Class c = val.getClass();
		try {
			if (c.isAssignableFrom(Boolean.class)) 			type = BOOLEAN;
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
	
}
