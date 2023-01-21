package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder;

import envisionEngine.gameEngine.world.gameWorld.GameWorld;

public abstract class ScriptEvent {
	
	protected GameWorld world;
	
	protected ScriptEvent(GameWorld theWorld) {
		world = theWorld;
	}
	
	public GameWorld getWorld() { return world; }
	
}
