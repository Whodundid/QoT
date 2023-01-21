package envision.events.types.entity;

import envision.events.EventType;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.world.gameWorld.GameWorld;

public class EntitySpawnedEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	
	public EntitySpawnedEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.ENTITY_SPAWNED, true);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
