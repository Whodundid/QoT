package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

/** Error thrown when attempting to declare/call a variable with an invalid name. */
public class ScriptNameError extends ScriptError {

	public ScriptNameError(String scriptName, int lineNumIn) {
		super("Invalid script name '" + scriptName + "'!", lineNumIn);
	}

}