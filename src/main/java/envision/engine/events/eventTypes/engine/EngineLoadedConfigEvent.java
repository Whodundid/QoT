package envision.engine.events.eventTypes.engine;

import envision.engine.events.EnvisionEventType;

public class EngineLoadedConfigEvent extends EngineEvent {
    
    /** TEMPORARY PLACEHOLDER UNTIL ACTUAL CONFIG TYPE IS CREATED! */
    private final Object config;
    
    public EngineLoadedConfigEvent(Object configIn) {
        super(EnvisionEventType.ENGINE_LOADED_CONFIG, true);
        config = configIn;
    }
    
    public Object getConfig() { return config; }
    
}
