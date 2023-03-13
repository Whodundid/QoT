package envision.game.events.eventTypes.entity;

import envision.game.events.EventType;
import envision.game.objects.entities.Entity;
import envision.game.world.IGameWorld;
import envision.game.world.Region;

/**
 * A type of game event that is called whenever an entity leaves a region
 * within a world.
 * 
 * @author Hunter Bragg
 */
public class EntityExitedRegionEvent extends EntityEvent {
	
	//--------
	// Fields
	//--------
	
	/** The world that this took place in. */
	private final IGameWorld world;
	/** The Entity that's exiting. */
	private final Entity ent;
	/** The region being exited. */
	private final Region region;
	/** The X and Y coordinates that the entity exited the region at. */
	private final double x, y;
	
	//--------------
	// Constructors
	//--------------
	
	public EntityExitedRegionEvent(IGameWorld worldIn, Entity entIn, Region regionIn, double xIn, double yIn) {
		super(EventType.ENTITY_EXITED_REGION, true);
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
	/** Returns the X coordinate of the world that the entity exited the region from. */
	public double getX() { return x; }
	/** Returns the Y coordinate of the world that the entity exited the region from. */
	public double getY() { return y; }
	
}
