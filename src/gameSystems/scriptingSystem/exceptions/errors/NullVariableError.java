package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

/** An error thrown if null is returned for a variable call. */
public class NullVariableError extends ScriptError {
	
	public NullVariableError() {
		super("");
	}
	
	public NullVariableError(String varName) {
		super("Variable '" + varName + "' has not been declared within this scope!");
	}
	
	public NullVariableError(String varName, int lineNum) {
		super("Variable '" + varName + "' has not been declared within this scope!", lineNum);
	}

}
