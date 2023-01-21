package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.variable;

import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.ScriptEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;

public abstract class VariableEvent extends ScriptEvent {

	protected VariableEvent(GameWorld theWorld) {
		super(theWorld);
	}

	/*
	
	ScriptVariable var;
	
	protected VariableEvent(GameWorld theWorld, ScriptVariable theVariable) {
		super(theWorld);
		var = theVariable;
	}
	
	public ScriptVariable getVariable() { return var; }
	
	*/
	
}
