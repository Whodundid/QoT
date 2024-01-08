package envision.engine.settings.config;

import envision.engine.settings.config.setting_types.BooleanConfigSetting;
import envision.engine.settings.config.setting_types.ByteConfigSetting;
import envision.engine.settings.config.setting_types.CharConfigSetting;
import envision.engine.settings.config.setting_types.DoubleConfigSetting;
import envision.engine.settings.config.setting_types.EnumConfigSetting;
import envision.engine.settings.config.setting_types.FloatConfigSetting;
import envision.engine.settings.config.setting_types.IntegerConfigSetting;
import envision.engine.settings.config.setting_types.LongConfigSetting;
import envision.engine.settings.config.setting_types.ShortConfigSetting;
import eutil.EUtil;

public record ConfigSettingDTO(String name, String description, String datatype, String defaultValue)
{
    
    //==============
    // Constructors
    //==============
    
    public ConfigSettingDTO(String name, String description, Class<?> datatype, Object defaultValue) {
        this(name, description, datatype.getName(), String.valueOf(defaultValue));
    }
    
    //=========
    // Methods
    //=========
    
    public ConfigSetting<?> convertToConfigSetting() throws Exception {
        Class<?> c = Class.forName(datatype);
        
        ConfigSetting setting = null;
        
        if (c == boolean.class || c == Boolean.class) {
            setting = new BooleanConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Boolean.parseBoolean(defaultValue));
        }
        else if (c == byte.class || c == Byte.class) {
            setting = new ByteConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Byte.parseByte(defaultValue));
        }
        else if (c == char.class || c == Character.class) {
            setting = new CharConfigSetting(name, description);
            if (defaultValue != null && !defaultValue.isEmpty()) setting.setDefaultAndApply(defaultValue.charAt(0));
        }
        else if (c == double.class || c == Double.class) {
            setting = new DoubleConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Double.parseDouble(defaultValue));
        }
        else if (c == float.class || c == Float.class) {
            setting = new FloatConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Float.parseFloat(defaultValue));
        }
        else if (c == int.class || c == Integer.class) {
            setting = new IntegerConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Integer.parseInt(defaultValue));
        }
        else if (c == long.class || c == Long.class) {
            setting = new LongConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Long.parseLong(defaultValue));
        }
        else if (c == short.class || c == Short.class) {
            setting = new ShortConfigSetting(name, description);
            if (defaultValue != null) setting.setDefaultAndApply(Short.parseShort(defaultValue));
        }
        else if (c == String.class) {
            setting = new ShortConfigSetting(name, description);
            setting.setDefaultAndApply(defaultValue);
        }
        else if (c == Enum.class) {
            setting = new EnumConfigSetting(c.getName(), name, description);
            
            Object[] enumValues = c.getEnumConstants();
            String[] strValues = new String[enumValues.length];
            for (int i = 0; i < enumValues.length; i++) {
                strValues[i] = String.valueOf(enumValues[i]);
            }
            
            if (defaultValue != null) {
                int index = EUtil.indexOf(strValues, defaultValue);
                if (index < 0) {
                    throw new IllegalArgumentException("The enum: " + c + " does not have a value for: " + defaultValue);
                }
                Enum<?> enumValue = (Enum<?>) enumValues[index];
                setting.setDefaultAndApply(enumValue);
            }
        }
        else {
            throw new IllegalArgumentException("Invalid class type: '" + datatype + "' provided!");
        }
        
        return setting;
    }
    
}
