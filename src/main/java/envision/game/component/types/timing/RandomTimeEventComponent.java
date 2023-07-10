package envision.game.component.types.timing;

import envision.game.component.ComponentBasedObject;
import eutil.random.ERandomUtil;

/**
 * A more specific type of TimedEventComponent that triggers a timed event
 * on a randomly determined interval. This component can optionally specify
 * an initial delay (in ticks) that define a specific number of ticks that
 * must pass before intervals are measured.
 * <p>
 * The timer's current interval is reevaluated after each timer event and
 * is specified by a given min/max tick range.
 * 
 * @author Hunter Bragg
 */
public class RandomTimeEventComponent extends TimeEventComponent {
	
	/** The minimum interval (in ms) between each time event. Begins after the initial delay is over. */
	protected float minTimeInterval;
	/** The minimum interval (in ms) between each time event. Begins after the initial delay is over. */
	protected float maxTimeInterval;
	/** The current, randomly decided, interval (in ms) for this timer. */
	protected float currentInterval;
	/** A specified amount of ms that must pass before time intervals are measured. */
	protected float startDelay;
	
	//==============
	// Constructors
	//==============
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The amount of ms in between each timer event
	 */
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, float minInterval, float maxInterval) {
		this(theEntityIn, minInterval, maxInterval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The amount of ms in between each timer event
	 * @param delay       An amount of ms to wait for until actual counting begins
	 */
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, float minInterval, float maxInterval, float delay) {
		super(theEntityIn);
		
		minTimeInterval = minInterval;
		maxTimeInterval = maxInterval;
		startDelay = delay;
		
		currentInterval = generateRandomInterval();
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    An amount of ms in between each timer event
	 */
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, float minInterval, float maxInterval) {
		this(theEntityIn, timerNameIn, minInterval, maxInterval, 0);
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and a
	 * specified initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The amount of ms in between each timer event
	 * @param delay       An amount of ms to wait for until actual counting
	 *                    begins
	 */
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, float minInterval, float maxInterval, float delay) {
		super(theEntityIn, timerNameIn);
		
		minTimeInterval = minInterval;
		maxTimeInterval = maxInterval;
		startDelay = delay;
		
		currentInterval = generateRandomInterval();
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void onGameTick(float deltaTime) {
		// if paused, ignore
		if (isPaused) return;
		
		currentTime += deltaTime;
		
		if ((currentTime - lastEventTime) >= currentInterval) {
		    lastEventTime = currentTime;
		    currentInterval = generateRandomInterval();
		    onEvent();
		}
		
		// some roll-over logic to prevent overflows
		if (currentTime >= 1E9F) {
			currentTime = 0F;
			lastEventTime = 0F;
		}
	}
	
	//=========
	// Methods
	//=========
	
	public float generateRandomInterval() {
		return generateRandomInterval(minTimeInterval, maxTimeInterval);
	}
	
	public float generateRandomInterval(float min, float max) {
		return ERandomUtil.getRoll(min, max);
	}
	
	//=========
	// Getters
	//=========
	
	public float getMinTimeInterval() { return minTimeInterval; }
	public float getMaxTimeInterval() { return maxTimeInterval; }
	public float getCurrentInterval() { return currentInterval; }
	public float getStartDelay() { return startDelay; }
	
	//=========
	// Setters
	//=========
	
	public void setMinTimeInterval(float interval) { minTimeInterval = interval; }
	public void setMaxTimeInterval(float interval) { maxTimeInterval = interval; }
	public void setCurrentInterval(float interval) { currentInterval = interval; }
	
}
