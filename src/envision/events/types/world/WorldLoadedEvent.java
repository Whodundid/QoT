package envision.events.types.world;

import envision.events.EventType;
import envision.game.world.GameWorld;

public class WorldLoadedEvent extends WorldEvent {

	private final GameWorld world;
	
	protected WorldLoadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_LOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
