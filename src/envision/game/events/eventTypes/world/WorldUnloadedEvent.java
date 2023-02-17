package envision.game.events.eventTypes.world;

import envision.game.events.EventType;
import envision.game.world.GameWorld;

public class WorldUnloadedEvent extends WorldEvent {

	private final GameWorld world;
	
	public WorldUnloadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_UNLOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
