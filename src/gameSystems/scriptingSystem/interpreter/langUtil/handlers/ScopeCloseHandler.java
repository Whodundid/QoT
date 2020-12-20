package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.enums.LineType;
import util.storageUtil.EArrayList;

public class ScopeCloseHandler extends ScriptHandler {
	
	public ScopeCloseHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleScopeClose(EArrayList<String> tokens) throws ScriptError {
		int scopeNum = script.lastScopes.size();
		
		parser.lastType = LineType.SCOPE_CLOSE;
		
		//this is a problem.
		if (scopeNum == 0) { throw new ScriptError("Invalid scope close!", lineNum); }
		
		//if the number of scopes is 1, then this is the script's closing scope
		if (scopeNum == 1) {
			script.openScriptScopes--;
			script.lastScopes.pop();
			script.stackFrames.pop();
		}
		//check for loop scopes
		else if (parser.lastType == LineType.LOOP) {
			script.openLoopScopes--;
			script.lastLoopLines.pop();
			script.lastScopes.pop();
		}
		//check for function scopes
		else if (parser.lastType == LineType.FUNCTIONDEF) {
			script.openFuncScopes--;
			script.lastFuncLines.pop();
			script.lastScopes.pop();
		}
		//open a generic new scope
		else {
			script.openScopes--;
			script.lastScopeLines.pop();
			script.lastScopes.pop();
		}
	}
	
}
