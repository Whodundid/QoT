package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.StackFrame;
import gameSystems.scriptingSystem.util.enums.LineType;
import gameSystems.scriptingSystem.util.enums.ScopeType;
import util.storageUtil.EArrayList;

public class ScopeOpenHandler extends ScriptHandler {
	
	public ScopeOpenHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleScopeOpen(EArrayList<String> tokens) throws ScriptError {
		int scopeNum = script.lastScopes.size();
		
		parser.lastType = LineType.SCOPE_OPEN;
		
		//if the number of scopes is 0, then this is the script's opening scope
		if (scopeNum == 0) {
			script.openScriptScopes++;
			script.lastScopes.push(ScopeType.SCRIPT);
			script.stackFrames.push(new StackFrame(script.getCurrentLine()));
		}
		//check for loop scopes
		else if (parser.lastType == LineType.LOOP) {
			script.openLoopScopes++;
			script.lastLoopLines.push(lineNum);
			script.lastScopes.push(ScopeType.LOOP);
		}
		//check for function scopes
		else if (parser.lastType == LineType.FUNCTIONDEF) {
			script.openFuncScopes++;
			script.lastFuncLines.push(lineNum);
			script.lastScopes.push(ScopeType.FUNCTION);
		}
		//open a generic new scope
		else {
			script.openScopes++;
			script.lastScopeLines.push(lineNum);
			script.lastScopes.push(ScopeType.SCOPE);
		}
	}

}
