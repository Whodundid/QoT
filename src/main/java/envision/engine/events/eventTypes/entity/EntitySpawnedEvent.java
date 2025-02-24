package envision.engine.events.eventTypes.entity;

import envision.engine.events.EnvisionEventType;
import envision.engine.loader.built.game.Entity;
import envision.game.world.GameWorld;

public class EntitySpawnedEvent extends EntityEvent {
    
    private final GameWorld world;
    private final Entity ent;
    
    public EntitySpawnedEvent(GameWorld worldIn, Entity entIn) {
        super(EnvisionEventType.ENTITY_SPAWNED, true);
        world = worldIn;
        ent = entIn;
    }
    
    public GameWorld getWorld() { return world; }
    public Entity getEntity() { return ent; }
    
}
