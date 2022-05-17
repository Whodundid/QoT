package engine.scripting.builder.events.variable;

import engine.scripting.builder.ScriptEvent;
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
