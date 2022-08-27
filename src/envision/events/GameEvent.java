package envision.events;

/**
 * A generic type of event that occurs during runtime.
 * <p>
 * Certain types of events can be cancelled which will prevent their action
 * from occurring.
 * 
 * @author Hunter Bragg
 */
public abstract class GameEvent {
	
	//--------
	// Fields
	//--------
	
	/** The type of event this is. */
	private final EventType type;
	/** True if this event can actually be cancelled as not all can. */
	private final boolean canBeCancelled;
	/** True if this event has in fact been cancelled. */
	private boolean isCancelled = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected GameEvent(EventType typeIn, boolean canBeCancelledIn) {
		type = typeIn;
		canBeCancelled = canBeCancelledIn;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Marks this event as cancelled. If this event cannot actually be
	 * cancelled an error is thrown.
	 * 
	 * @param val boolean state
	 */
	public void setCancelled(boolean val) {
		if (val && !canBeCancelled) throw new IllegalStateException("Event '" + type + "' cannot be cancelled!");
		isCancelled = val;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns this event's type. */
	public EventType getType() { return type; }
	/** Returns true if this event has been cancelled. */
	public boolean isCancelled() { return isCancelled; }
	
}
