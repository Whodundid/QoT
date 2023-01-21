package envisionEngine.events.types.world;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

public class WorldUnloadedEvent extends WorldEvent {

	private final GameWorld world;
	
	public WorldUnloadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_UNLOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
