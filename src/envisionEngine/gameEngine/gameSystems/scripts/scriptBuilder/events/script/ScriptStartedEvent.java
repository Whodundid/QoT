package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.script;

import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.ScriptEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

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
