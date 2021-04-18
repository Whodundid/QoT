package gameSystems.scriptingSystem.events.variable;

import gameSystems.mapSystem.GameWorld;
import gameSystems.scriptingSystem.ScriptEvent;

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
