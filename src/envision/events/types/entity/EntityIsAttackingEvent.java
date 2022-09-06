package envision.events.types.entity;

import envision.events.EventType;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;

public class EntityIsAttackingEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	private final Entity entToAttack;
	
	public EntityIsAttackingEvent(GameWorld worldIn, Entity entIn, Entity entityToAttack) {
		super(EventType.ENTITY_IS_ATTACKING, true);
		world = worldIn;
		ent = entIn;
		entToAttack = entityToAttack;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	public Entity getEntityToAttack() { return entToAttack; }
	
}
