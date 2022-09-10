package envision.events.types.entity;

import envision.events.EventType;
import envision.game.entity.Entity;
import envision.game.world.gameWorld.IGameWorld;
import envision.game.world.util.Region;

/**
 * A type of game event that is called whenever an entity enters a region
 * within a world.
 * 
 * @author Hunter Bragg
 */
public class EntityEnteredRegionEvent extends EntityEvent {
	
	//--------
	// Fields
	//--------
	
	/** The world that this took place in. */
	private final IGameWorld world;
	/** The Entity that's entering. */
	private final Entity ent;
	/** The region being entered. */
	private final Region region;
	/** The X and Y coordinates that the entity entered the region at. */
	private final int x, y;
	
	//--------------
	// Constructors
	//--------------
	
	public EntityEnteredRegionEvent(IGameWorld worldIn, Entity entIn, Region regionIn, int xIn, int yIn) {
		super(EventType.ENTITY_ENTERED_REGION, true);
		world = worldIn;
		ent = entIn;
		region = regionIn;
		x = xIn;
		y = yIn;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the world that this event took place in. */
	public IGameWorld getWorld() { return world; }
	/** Returns the entity that performed the action. */
	public Entity getEntity() { return ent; }
	/** Returns the region that this took place in. */
	public Region getRegion() { return region; }
	/** Returns the X coordinate of the world that the entity entered the region from. */
	public int getX() { return x; }
	/** Returns the Y coordinate of the world that the entity entered the region from. */
	public int getY() { return y; }
	
}
