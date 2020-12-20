package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;

public class IndexOutOfBoundsError extends ScriptError {

	public IndexOutOfBoundsError(int index, int lineNumIn) {
		super("The index: '" + index + "' is out of bounds for the given list!", lineNumIn);
	}

}
