package envision.game.component.types.timing;

import envision.game.component.ComponentBasedObject;

/**
 * A more specific type of TimedEventComponent that triggers a timed event on a fixed interval.
 * This component can optionally specify an initial delay (in ms) that define a specific duration
 * in ms that must pass before intervals are measured.
 * 
 * @author Hunter Bragg
 */
public class FixedTimeEventComponent extends TimeEventComponent {
	
	/** The interval (in ms) between each time event. Begins after the initial delay is over. */
	protected float timeInterval;
	/** A specified duration (in ms) that must pass before time intervals are measured. */
	protected float startDelay;
	
	//==============
	// Constructors
	//==============
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    An amount of ms in between each timer event
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, float interval) {
		this(theEntityIn, interval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    An amount of ms in between each timer event
	 * @param delay       An amount of ms to wait for until actual counting
	 *                    begins
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, float interval, float delay) {
		super(theEntityIn);
		
		timeInterval = interval;
		startDelay = delay;
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    An amount of ms in between each timer event
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, float interval) {
		this(theEntityIn, timerNameIn, interval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    An amount of ms in between each timer event
	 * @param delay       An amount of ms to wait for until actual counting
	 *                    begins
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, float interval, float delay) {
		super(theEntityIn, timerNameIn);
		
		timeInterval = interval;
		startDelay = delay;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onGameTick(float deltaTime) {
        // if paused, ignore
        if (isPaused) return;
        
        currentTime += deltaTime;
        
        if (currentTime - lastEventTime >= timeInterval) {
            lastEventTime = currentTime;
            onEvent();
        }
        
        // some roll-over logic to prevent overflows
        if (currentTime >= 1E9F) {
            currentTime = 0F;
            lastEventTime = 0F;
        }
	}
	
	//=========
	// Getters
	//=========
	
	public float getTimeInterval() { return timeInterval; }
	public float getStartDelay() { return startDelay; }
	
	//=========
	// Setters
	//=========
	
	public void setTimeInterval(float intervalIn) { timeInterval = intervalIn; }
	
	
}
