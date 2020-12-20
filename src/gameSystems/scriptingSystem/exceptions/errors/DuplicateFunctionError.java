package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

public class DuplicateFunctionError extends ScriptError {

	public DuplicateFunctionError(String message) {
		super("Function: '" + message + "' already declared within current scope!");
	}
	
	public DuplicateFunctionError(String message, int lineNumIn) {
		super("Function: '" + message + "' already declared within current scope!", lineNumIn);
	}

}
