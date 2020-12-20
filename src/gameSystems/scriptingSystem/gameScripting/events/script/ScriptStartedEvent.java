package gameSystems.scriptingSystem.gameScripting.events.script;

import gameSystems.mapSystem.GameWorld;
import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.gameScripting.ScriptEvent;

public class ScriptStartedEvent extends ScriptEvent {
	
	private EScript script;
	
	public ScriptStartedEvent(GameWorld theWorld, EScript theScript) {
		super(theWorld);
		script = theScript;
	}
	
	public EScript getScript() { return script; }
	
}
