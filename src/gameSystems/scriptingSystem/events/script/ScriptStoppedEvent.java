package gameSystems.scriptingSystem.events.script;

import gameSystems.mapSystem.GameWorld;
import gameSystems.scriptingSystem.ScriptEvent;

public class ScriptStoppedEvent extends ScriptEvent {

	protected ScriptStoppedEvent(GameWorld theWorld) {
		super(theWorld);
	}
	
	/*
	
	private EScript script;
	
	public ScriptStoppedEvent(GameWorld theWorld, EScript theScript) {
		super(theWorld);
		script = theScript;
	}
	
	public EScript getScript() { return script; }
	
	*/
	
}
