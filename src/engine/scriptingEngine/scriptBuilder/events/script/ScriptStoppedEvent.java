package engine.scriptingEngine.scriptBuilder.events.script;

import engine.scriptingEngine.scriptBuilder.ScriptEvent;
import world.GameWorld;

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