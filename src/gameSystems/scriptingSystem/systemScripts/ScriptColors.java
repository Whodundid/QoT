package gameSystems.scriptingSystem.systemScripts;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.miscUtil.EDataType;
import util.renderUtil.EColors;

public class ScriptColors extends EScript {

	public ScriptColors() throws ScriptError {
		super("Colors");
		
		//add colors as global variables
		for (EColors c : EColors.values()) {
			staticVariables.add(new ScriptVariable(c.name, EDataType.OBJECT, c));
		}
		
		//add color functions
		functions.add(new GetInt());
		functions.add(new GetName());
		functions.add(new Rainbow());
	}
	
	//getInt method -- returns the integer equivalent of a Color
	private class GetInt extends ScriptFunction {
		protected GetInt() { super(null, EDataType.INT, "getInt", 1); }
	}
	
	//getName method -- returns the name of a Color
	private class GetName extends ScriptFunction {
		protected GetName() { super(null, EDataType.STRING, "getName", 1); }
	}
	
	//rainbow method -- returns the current color at time of being called
	private class Rainbow extends ScriptFunction {
		protected Rainbow() { super(null, EDataType.INT, "rainbow", 1); }
	}

}
