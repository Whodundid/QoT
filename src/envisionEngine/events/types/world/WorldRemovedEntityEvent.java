package envisionEngine.events.types.world;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

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
