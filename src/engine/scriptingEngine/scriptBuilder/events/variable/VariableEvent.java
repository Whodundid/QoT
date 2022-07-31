package engine.scriptingEngine.scriptBuilder.events.variable;

import engine.scriptingEngine.scriptBuilder.ScriptEvent;
import world.GameWorld;

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
