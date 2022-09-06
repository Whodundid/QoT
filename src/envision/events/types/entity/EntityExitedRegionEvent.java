package envision.events.types.entity;

import envision.events.EventType;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class EntityExitedRegionEvent extends EntityEvent {
	
	private final GameWorld world;
	private final Entity ent;
	private final Region region;
	
	public EntityExitedRegionEvent(GameWorld worldIn, Entity entIn, Region regionIn) {
		super(EventType.ENTITY_EXITED_REGION, true);
		world = worldIn;
		ent = entIn;
		region = regionIn;
	}
	
	public GameWorld getWorld() { return world; }
	public Entity getEntity() { return ent; }
	public Region getRegion() { return region; }
	
}
