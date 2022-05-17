package engine.scripting.builder.events.script;

import engine.scripting.builder.ScriptEvent;
import world.GameWorld;

public class ScriptStartedEvent extends ScriptEvent {

	protected ScriptStartedEvent(GameWorld theWorld) {
		super(theWorld);
	}
	
	/*
	
	private EScript script;
	
	public ScriptStartedEvent(GameWorld theWorld, EScript theScript) {
		super(theWorld);
		script = theScript;
	}
	
	public EScript getScript() { return script; }
	
	*/
	
}
