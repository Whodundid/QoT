package envision.engine.scripting.scriptBuilder.events.script;

import envision.engine.scripting.scriptBuilder.ScriptEvent;
import envision.game.world.GameWorld;

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
