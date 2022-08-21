package envision.events.types.world;

import envision.events.EventType;
import envision.game.world.GameWorld;

public class WorldUnloadedEvent extends WorldEvent {

	private final GameWorld world;
	
	protected WorldUnloadedEvent(GameWorld worldIn) {
		super(EventType.WORLD_UNLOADED, true);
		world = worldIn;
	}
	
	public GameWorld getWorld() { return world; }
	
}
