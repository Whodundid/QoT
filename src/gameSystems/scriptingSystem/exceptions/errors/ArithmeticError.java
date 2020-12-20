package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

/** An error thrown during script execution if there is a math or arithmetic expression related error thrown. */
public class ArithmeticError extends ScriptError {

	public ArithmeticError(String message, int lineNum) {
		super(message, lineNum);
	}

}
