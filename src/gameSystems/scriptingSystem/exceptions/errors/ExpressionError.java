package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

/** Error thrown when an expression cannot properly be generated. */
public class ExpressionError extends ScriptError {
	
	public ExpressionError(String message, int lineNum) {
		super(message, lineNum);
	}

}
