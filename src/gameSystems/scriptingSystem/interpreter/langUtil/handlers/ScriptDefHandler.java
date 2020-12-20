package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.enums.Keyword;
import util.storageUtil.EArrayList;

public class ScriptDefHandler extends ScriptHandler {
	
	public ScriptDefHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleScriptDef(EArrayList<String> tokens) throws ScriptError {
		boolean foundDefinition = false;
		String scriptName = null;
		
		for (int i = 0; i < tokens.size(); i++) {
			String s = tokens.get(i);
			
			if (s.equals(Keyword.SCRIPT.keyword)) {
				if (foundDefinition) { throw new ScriptError("Invalid double script definition!", lineNum); }
				foundDefinition = true;
			}
			else {
				scriptName = s;
			}
		}
		
		if (!foundDefinition || scriptName == null) {
			throw new ScriptError("Cannot properly identify script within file!");
		}
		
		script.setName(scriptName);
	}
	
}
