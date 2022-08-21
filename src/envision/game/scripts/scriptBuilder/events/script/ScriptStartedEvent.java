package envision.game.scripts.scriptBuilder.events.script;

import envision.game.scripts.scriptBuilder.ScriptEvent;
import envision.game.world.GameWorld;

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
