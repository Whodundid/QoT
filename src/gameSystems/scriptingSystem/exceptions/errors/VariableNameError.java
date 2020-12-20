package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

/** Error thrown when attempting to declare/call a variable with an invalid name. */
public class VariableNameError extends ScriptError {

	public VariableNameError(String varName) {
		super("Invalid variable name '" + varName + "'!");
	}
	
	public VariableNameError(String varName, int lineNumIn) {
		super("Invalid variable name '" + varName + "'!", lineNumIn);
	}

}
