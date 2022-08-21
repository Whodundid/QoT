package engine.Events.types.world;

import engine.Events.EventType;
import world.GameWorld;

public class WorldUnloadedEvent extends WorldEvent {

	private final GameWorld world;
	
	protected WorldUnloadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_UNLOADED);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
