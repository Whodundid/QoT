package engine.scriptingEngine.scriptBuilder;

import world.GameWorld;

public abstract class ScriptEvent {
	
	protected GameWorld world;
	
	protected ScriptEvent(GameWorld theWorld) {
		world = theWorld;
	}
	
	public GameWorld getWorld() { return world; }
	
}
