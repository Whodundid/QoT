package gameSystems.scriptingSystem.exceptions.errors;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.miscUtil.EDataType;

/** Error thrown when attempting to cast a variable to an incompatible datatype. */
public class VariableCastError extends ScriptError {

	public VariableCastError(ScriptVariable var, EDataType castType, int lineNumIn) {
		super("Variable '" + ((var != null) ? var.getName() : "null") + "' cannot be cast as type " + castType + "!", lineNumIn);
	}

}
