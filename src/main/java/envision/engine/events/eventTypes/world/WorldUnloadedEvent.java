package envision.engine.events.eventTypes.world;

import envision.engine.events.EventType;
import envision.game.world.GameWorld;

public class WorldUnloadedEvent extends WorldEvent {

	private final GameWorld world;
	
	public WorldUnloadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_UNLOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
