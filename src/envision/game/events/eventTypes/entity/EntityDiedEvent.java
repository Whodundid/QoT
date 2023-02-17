package envision.game.events.eventTypes.entity;

import envision.game.events.EventType;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;

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
