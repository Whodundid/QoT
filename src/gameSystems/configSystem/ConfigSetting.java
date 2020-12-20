package gameSystems.configSystem;

import java.util.Collection;
import util.EUtil;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ConfigSetting<Val> {
	
	private String settingName = "";
	private String description = "";
	private boolean requiresDev = false;
	private Val val = null;
	private Val defaultVal = null;
	private EDataType type;
	private EArrayList additionalArgs = new EArrayList();
	private final Class<Val> cType;
	private boolean ignoreConfigRead = false;
	private boolean ignoreConfigWrite = false;
	
	//-----------------------------
	//AppConfigSetting Constructors
	//-----------------------------
	
	public ConfigSetting(Class<Val> cTypeIn, String settingNameIn, String descriptionIn, Val initialValue) {
		settingName = settingNameIn;
		val = initialValue;
		defaultVal = initialValue;
		cType = cTypeIn;
		description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
		parseDataType();
	}
	
	//-----------------------
	// ConfigSetting Methods
	//-----------------------
	
	public ConfigSetting reset() { set(defaultVal); return this; }
	
	public ConfigSetting toggle() {
		if (cType.isAssignableFrom(Boolean.class)) {
			Boolean b = (Boolean) cType.cast(val);
			b = !b;
			set(cType.cast(b));
		}
		return this;
	}
	
	//-----------------------
	// ConfigSetting Getters
	//-----------------------
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return val != null ? val.toString() : "null"; }
	public String defAsString() { return defaultVal != null ? defaultVal.toString() : "null"; }
	public EDataType getType() { return type; }
	public Class<Val> getClassType() { return cType; }
	public Val get() { return val; }
	public boolean getRequiresDev() { return requiresDev; }
	public EArrayList getArgs() { return additionalArgs; }
	public Val getDefault() { return defaultVal; }
	public boolean getIgnoreConfigRead() { return ignoreConfigRead; }
	public boolean getIgnoreConfigWrite() { return ignoreConfigWrite; }
	
	//-----------------------
	// ConfigSetting Setters
	//-----------------------
	
	public ConfigSetting set(Val valIn) { val = valIn; return this; }
	public ConfigSetting setArgs(Object... argsIn) { additionalArgs.add(argsIn); return this; }
	public ConfigSetting setArgs(Collection argsIn) { additionalArgs.addAll(argsIn); return this; }
	public ConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
	public ConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
	public ConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
	
	//--------------------------------
	// ConfigSetting Internal Methods
	//--------------------------------
	
	private void parseDataType() {
		if (val != null) {
			Class c = val.getClass();
			try {
				if (c.isAssignableFrom(Boolean.class)) { type = EDataType.BOOLEAN; }
				else if (c.isAssignableFrom(Byte.class)) { type = EDataType.BYTE; }
				else if (c.isAssignableFrom(Short.class)) { type = EDataType.SHORT; }
				else if (c.isAssignableFrom(Integer.class)) { type = EDataType.INT; }
				else if (c.isAssignableFrom(Long.class)) { type = EDataType.LONG; }
				else if (c.isAssignableFrom(Float.class)) { type = EDataType.FLOAT; }
				else if (c.isAssignableFrom(Double.class)) { type = EDataType.DOUBLE; }
				else if (c.isAssignableFrom(String.class)) { type = EDataType.STRING; }
				else if (c.isAssignableFrom(Enum.class)) { type = EDataType.ENUM; }
				else if (c != null) {
					type = EDataType.OBJECT;
				}
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				type = EDataType.OBJECT;
			}
		}
		else { type = EDataType.OBJECT; }
	}
	
	//------------------------------
	// ConfigSetting Static Methods
	//------------------------------
	
	public static void reset(ConfigSetting setting, ConfigSetting... additional) { EUtil.filterNullForEachA(s -> s.reset(), EUtil.add(setting, additional)); }
	
}
