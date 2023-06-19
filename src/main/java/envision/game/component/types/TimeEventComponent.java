package envision.game.component.types;

import java.util.UUID;

import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;
import envision.game.entities.ComponentBasedObject;

public abstract class TimeEventComponent extends EntityComponent {
	
	/** The tick that this timer started counting on. -1 by default to indicate no started. */
	protected int startTick = -1;
	/** The internal current tick that this timer is currently on. */
	protected int currentTick;
	/** The tick on which this timer's last timer event was triggered on. */
	protected int lastEventTick;
	/** Indicates whether or not this timing component is currently counting. */
	protected boolean isPaused = false;
	
	//==============
	// Constructors
	//==============
	
	protected TimeEventComponent(ComponentBasedObject theEntityIn) {
		this(theEntityIn, UUID.randomUUID().toString());
	}
	
	protected TimeEventComponent(ComponentBasedObject theEntityIn, String timerNameIn) {
		super(theEntityIn, ComponentType.TIMED_EVENT, timerNameIn, true, false);
	}
	
	//===========
	// Abstracts
	//===========
	
	// outlines that children of this component must specify the 'onGameTick' method
	public abstract void onGameTick(float deltaTime);
	
	/**
	 * Called whenever this timer has reached the end of one interval.
	 */
	public void onEvent() {
		theObject.onComponentEvent(this, componentID);
	}
	
	//=========
	// Methods
	//=========
	
	/** Pauses this timer's counting. */
	public void puaseTimer() {
		isPaused = true;
	}
	
	/** Resumes this timer's counting. */
	public void resumeTimer() {
		isPaused = false;
	}
	
	public void setPaused(boolean val) {
		isPaused = val;
	}
	
	//=========
	// Getters
	//=========
	
	public int getStartTick() { return startTick; }
	public int getCurrentTick() { return currentTick; }
	public int getLastEventTick() { return lastEventTick; }
	public boolean isPaused() { return isPaused; }
	
}
