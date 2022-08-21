package engine.Events.types.entity;

import engine.Events.EventType;
import game.entities.Entity;
import world.GameWorld;

public class EntityDiedEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	
	public EntityDiedEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.ENTITY_SPAWNED);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
