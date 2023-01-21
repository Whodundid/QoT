package envisionEngine.events.types.entity;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

public class EntityDiedEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	
	public EntityDiedEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.ENTITY_DIED, true);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
