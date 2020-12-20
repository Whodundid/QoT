package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

public class NullFunctionError extends ScriptError {
	
	public NullFunctionError() {
		super("");
	}
	
	public NullFunctionError(String funcName) {
		super("The function '" + funcName + "' has not been declared within this scope!");
	}
	
	public NullFunctionError(String funcName, int lineNum) {
		super("The function '" + funcName + "' has not been declared within this scope!", lineNum);
	}

}