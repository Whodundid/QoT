package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

public class DuplicateVariableError extends ScriptError {

	public DuplicateVariableError(String message) {
		super("Function: '" + message + "' already declared within current scope!");
	}
	
	public DuplicateVariableError(String message, int lineNumIn) {
		super("Variable: '" + message + "' already declared within current scope!", lineNumIn);
	}
	
}
