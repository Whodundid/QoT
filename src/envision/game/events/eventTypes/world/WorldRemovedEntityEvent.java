package envision.game.events.eventTypes.world;

import envision.game.events.EventType;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;

public class WorldRemovedEntityEvent extends WorldEvent {

	private final GameWorld world;
	private final Entity ent;
	
	public WorldRemovedEntityEvent(GameWorld worldIn, Entity entIn) {
		super(EventType.WORLD_REMOVED_ENTITY, true);
		world = worldIn;
		ent = entIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	
}
