package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ExpressionSolver;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptObject;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class ReturnHandler extends ScriptHandler {
	
	public ReturnHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleReturn(EArrayList<String> tokens) throws ScriptError {
		if (script.openFuncScopes > 0) {
			ScriptFunction func = script.lastFuncs.peek();
			
			EDataType returnType = func.getReturnType();
			if (returnType == EDataType.VOID) { return; }
			
			tokens.removeFirst();
			ScriptObject result = ExpressionSolver.evaluate(script, tokens, lineNum);
			
			script.stackFrames.peek().setRAX(result);
		}
		else {
			script.killScript();
		}
	}
	
}
