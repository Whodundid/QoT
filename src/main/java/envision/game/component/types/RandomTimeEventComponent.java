package envision.game.component.types;

import envision.game.entities.ComponentBasedObject;
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
	
	/** The minimum interval between each time event. Begins after the initial delay is over. */
	protected int minTickInterval;
	/** The minimum interval between each time event. Begins after the initial delay is over. */
	protected int maxTickInterval;
	/** The current, randomly decided, interval for this timer. */
	protected int currentInterval;
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
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, int minInterval, int maxInterval) {
		this(theEntityIn, minInterval, maxInterval, 0);
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
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, int minInterval, int maxInterval, int delay) {
		super(theEntityIn);
		
		minTickInterval = minInterval;
		maxTickInterval = maxInterval;
		startDelay = delay;
		
		currentInterval = generateRandomInterval();
	}
	
	/**
	 * Creates a new FixedTimedEventComponent with the given interval and zero
	 * initial delay.
	 * 
	 * @param theEntityIn The entity that this component is attached to
	 * @param interval    The number of ticks in-between each timer event
	 */
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, int minInterval, int maxInterval) {
		this(theEntityIn, timerNameIn, minInterval, maxInterval, 0);
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
	public RandomTimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn, int minInterval, int maxInterval, int delay) {
		super(theEntityIn, timerNameIn);
		
		minTickInterval = minInterval;
		maxTickInterval = maxInterval;
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
		
		currentTick++;
		
		// some roll-over logic to prevent overflows
		if (currentTick == (Integer.MAX_VALUE - currentInterval)) {
			currentTick = currentInterval;
			lastEventTick = 0;
		}
		
		// actually measure the number of ticks since the last tick event
		if ((currentTick - lastEventTick) >= currentInterval) {
			lastEventTick = currentTick;
			currentInterval = generateRandomInterval();
			onEvent();
		}
	}
	
	//=========
	// Methods
	//=========
	
	public int generateRandomInterval() {
		return generateRandomInterval(minTickInterval, maxTickInterval);
	}
	
	public int generateRandomInterval(int min, int max) {
		return ERandomUtil.getRoll(min, max);
	}
	
	//=========
	// Getters
	//=========
	
	public int getMinTickInterval() { return minTickInterval; }
	public int getMaxTickInterval() { return maxTickInterval; }
	public int getCurrentInterval() { return currentInterval; }
	public int getStartDelay() { return startDelay; }
	
	//=========
	// Setters
	//=========
	
	public void setMinTickInterval(int interval) { minTickInterval = interval; }
	public void setMaxTickInterval(int interval) { maxTickInterval = interval; }
	public void setCurrentInterval(int interval) { currentInterval = interval; }
	
}
