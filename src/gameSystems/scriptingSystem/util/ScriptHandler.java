package gameSystems.scriptingSystem.util;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.interpreter.ScriptInterpreter;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;

public abstract class ScriptHandler {

	protected ScriptRunner runner;
	protected ScriptInterpreter interpreter;
	protected ScriptLineParser parser;
	protected EScript script;
	protected String line;
	protected int lineNum;
	
	protected ScriptHandler(ScriptLineParser parserIn) {
		parser = parserIn;
		
		if (parser != null) {
			script = parser.getScript();
			runner = script.getRunner();
			interpreter = script.getInterpreter();
			line = parser.getLine();
			lineNum = parser.getLineNumber();
		}
	}
	
}
