package envisionEngine.events.types.world;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

public class WorldLoadedEvent extends WorldEvent {

	private final GameWorld world;
	
	public WorldLoadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_LOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
