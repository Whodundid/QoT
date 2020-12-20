package gameSystems.scriptingSystem.gameScripting.events.variable;

import gameSystems.mapSystem.GameWorld;
import gameSystems.scriptingSystem.gameScripting.ScriptEvent;
import gameSystems.scriptingSystem.variables.ScriptVariable;

public abstract class VariableEvent extends ScriptEvent {

	ScriptVariable var;
	
	protected VariableEvent(GameWorld theWorld, ScriptVariable theVariable) {
		super(theWorld);
		var = theVariable;
	}
	
	public ScriptVariable getVariable() { return var; }
	
}
