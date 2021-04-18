package gameSystems.scriptingSystem.events.script;

import gameSystems.mapSystem.GameWorld;
import gameSystems.scriptingSystem.ScriptEvent;

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
