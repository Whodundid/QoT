package gameSystems.scriptingSystem.gameScripting;

import gameSystems.mapSystem.GameWorld;

public abstract class ScriptEvent {
	
	protected GameWorld world;
	
	protected ScriptEvent(GameWorld theWorld) {
		world = theWorld;
	}
	
	public GameWorld getWorld() { return world; }
	
}
