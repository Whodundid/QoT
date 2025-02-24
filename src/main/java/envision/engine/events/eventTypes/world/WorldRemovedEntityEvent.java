package envision.engine.events.eventTypes.world;

import envision.engine.events.EnvisionEventType;
import envision.engine.loader.built.game.Entity;
import envision.game.world.GameWorld;

public class WorldRemovedEntityEvent extends WorldEvent {

    private final GameWorld world;
    private final Entity ent;
    
    public WorldRemovedEntityEvent(GameWorld worldIn, Entity entIn) {
        super(EnvisionEventType.WORLD_REMOVED_ENTITY, true);
        world = worldIn;
        ent = entIn;
    }
    
    public GameWorld getWorld() { return world; }
    public Entity getEntity() { return ent; }
    
}
