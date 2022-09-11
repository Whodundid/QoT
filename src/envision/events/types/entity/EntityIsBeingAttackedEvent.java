package envision.events.types.entity;

import envision.events.EventType;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.world.gameWorld.GameWorld;

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
