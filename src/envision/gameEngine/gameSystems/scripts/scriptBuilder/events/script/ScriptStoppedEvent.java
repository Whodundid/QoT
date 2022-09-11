package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.script;

import envision.gameEngine.gameSystems.scripts.scriptBuilder.ScriptEvent;
import envision.gameEngine.world.gameWorld.GameWorld;

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
