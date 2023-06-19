package envision.game.component.types;

import envision.game.entities.ComponentBasedObject;

/**
 * A more specific type of TimedEventComponent that triggers a timed event on a fixed interval.
 * This component can optionally specify an initial delay (in ticks) that define a specific number
 * of ticks that must pass before intervals are measured.
 * 
 * @author Hunter Bragg
 */
public class FixedTimeEventComponent extends TimeEventComponent {
	
	/** The interval between each time event. Begins after the initial delay is over. */
	protected int tickInterval;
	/** A specified number of ticks that must pass before time intervals are measured. */
	protected int startDelay;
	
	//==============
	// Constructors
	//==============
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The number of ticks in-between each timer event
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, int interval) {
		this(theEntityIn, interval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The number of ticks in-between each timer event
	 * @param delay       A number of ticks to wait for until actual counting
	 *                    begins
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, int interval, int delay) {
		super(theEntityIn);
		
		tickInterval = interval;
		startDelay = delay;
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The number of ticks in-between each timer event
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, int interval) {
		this(theEntityIn, timerNameIn, interval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The number of ticks in-between each timer event
	 * @param delay       A number of ticks to wait for until actual counting
	 *                    begins
	 */
	public FixedTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, int interval, int delay) {
		super(theEntityIn, timerNameIn);
		
		tickInterval = interval;
		startDelay = delay;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onGameTick(float deltaTime) {
		currentTick++;
		
		// some roll-over logic to prevent overflows
		if (currentTick == (Integer.MAX_VALUE - tickInterval)) {
			currentTick = tickInterval;
			lastEventTick = 0;
		}
		
		// actually measure the number of ticks since the last tick event
		if ((currentTick - lastEventTick) >= tickInterval) {
			lastEventTick = currentTick;
			onEvent();
		}
	}
	
	//=========
	// Getters
	//=========
	
	public int getTickInterval() { return tickInterval; }
	public int getStartDelay() { return startDelay; }
	
	//=========
	// Setters
	//=========
	
	public void setTickInterval(int intervalIn) { tickInterval = intervalIn; }
	
	
}
