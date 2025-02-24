package envision.engine.events.eventTypes.world;

import envision.engine.events.EnvisionEventType;
import envision.game.world.GameWorld;

public class WorldLoadedEvent extends WorldEvent {

    private final GameWorld world;
    
    public WorldLoadedEvent(GameWorld worldIn) {
        super(EnvisionEventType.WORLD_LOADED, true);
        world = worldIn;
    }
    
    public GameWorld getWorld() { return world; }
    
}
