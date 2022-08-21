package engine.Events.types.world;

import engine.Events.EventType;
import game.entities.Entity;
import world.GameWorld;

public class WorldAddedEntityEvent extends WorldEvent {

	private final GameWorld world;
	private final Entity ent;
	
	protected WorldAddedEntityEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.WORLD_ADDED_ENTITY);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
