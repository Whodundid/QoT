package envision.game.events.eventTypes.world;

import envision.game.events.EventType;
import envision.game.world.GameWorld;

public class WorldLoadedEvent extends WorldEvent {

	private final GameWorld world;
	
	public WorldLoadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_LOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
