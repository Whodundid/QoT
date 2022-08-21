package engine.Events.types.world;

import engine.Events.EventType;
import world.GameWorld;

public class WorldLoadedEvent extends WorldEvent {

	private final GameWorld world;
	
	protected WorldLoadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_LOADED);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
