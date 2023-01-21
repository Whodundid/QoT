package envisionEngine.events.types.engine;

import envisionEngine.events.EventType;

public class EngineLoadedConfigEvent extends EngineEvent {
	
	/** TEMPORARY PLACEHOLDER UNTIL ACTUAL CONFIG TYPE IS CREATED! */
	private final Object config;
	
	public EngineLoadedConfigEvent(Object configIn) {
		super(EventType.ENGINE_LOADED_CONFIG, true);
		config = configIn;
	}
	
	public Object getConfig() { return config; }
	
}
