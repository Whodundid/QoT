package envision.game.events.eventTypes.entity;

import envision.game.events.EventType;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;

public class EntityIsBeingAttackedEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	private final Entity attackingEnt;
	
	public EntityIsBeingAttackedEvent(GameWorld worldIn, Entity entIn, Entity attackingEntity) {
		super(EventType.ENTITY_IS_BEING_ATTACKED, true);
		world = worldIn;
		ent = entIn;
		attackingEnt = attackingEntity;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	public Entity getAttackingEntity() { return attackingEnt; }
	
}
