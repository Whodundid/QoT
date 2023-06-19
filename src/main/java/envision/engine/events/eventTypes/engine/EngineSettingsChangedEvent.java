package envision.engine.events.eventTypes.engine;

import envision.engine.events.EventType;
import envision.engine.settings.config.ConfigSetting;

public class EngineSettingsChangedEvent extends EngineEvent {
	
	/** TEMPORARY PLACEHOLDER UNTIL ACTUAL CONFIG TYPE IS CREATED! */
	private final ConfigSetting<?> setting;
	private final Object oldValue;
	
	public <E> EngineSettingsChangedEvent(ConfigSetting<E> settingIn, E oldValueIn) {
		super(EventType.ENGINE_LOADED_CONFIG, true);
		setting = settingIn;
		oldValue = oldValueIn;
	}
	
	public ConfigSetting<?> getSetting() { return setting; }
	
	public <E> E getOldValue() {
		var cType = setting.getClassType();
		return (E) cType.cast(oldValue);
	}
	
}