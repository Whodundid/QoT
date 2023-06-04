package envision.engine.events.eventTypes.world;

import envision.engine.events.EventType;
import envision.game.entities.Entity;
import envision.game.world.GameWorld;

public class WorldAddedEntityEvent extends WorldEvent {

	private final GameWorld world;
	private final Entity ent;
	
	public WorldAddedEntityEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.WORLD_ADDED_ENTITY, true);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
