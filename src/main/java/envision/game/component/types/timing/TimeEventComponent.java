package envision.game.component.types.timing;

import java.util.UUID;

import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;

public abstract class TimeEventComponent extends EntityComponent {
	
	/** The time that this timer started counting on. -1 by default to indicate no started. */
	protected float startTime = -1;
	/** The internal current time that this timer is currently on. */
	protected float currentTime;
	/** The time on which this timer's last timer event was triggered on. */
	protected float lastEventTime;
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
	
	// outlines that children of this component must specify the 'onGametime' method
	public abstract void onGameTick(float deltaTime);
	
	//=========
	// Methods
	//=========
	
	/** Pauses this timer's counting. */
	public void pauseTimer() {
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
	
	public float getStartTime() { return startTime; }
	public float getCurrentTime() { return currentTime; }
	public float getLastEventTime() { return lastEventTime; }
	public boolean isPaused() { return isPaused; }
	
}
