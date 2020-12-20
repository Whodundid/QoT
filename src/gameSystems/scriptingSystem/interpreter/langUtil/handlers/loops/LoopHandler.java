package gameSystems.scriptingSystem.interpreter.langUtil.handlers.loops;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import util.storageUtil.EArrayList;

public class LoopHandler extends ScriptHandler {
	
	public LoopHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleLoop(EArrayList<String> tokens) throws ScriptError {
		
	}
	
}
