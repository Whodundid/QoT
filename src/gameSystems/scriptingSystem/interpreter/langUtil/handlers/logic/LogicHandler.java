package gameSystems.scriptingSystem.interpreter.langUtil.handlers.logic;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ArgumentParser;
import gameSystems.scriptingSystem.interpreter.langUtil.ExpressionSolver;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.enums.Keyword;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class LogicHandler extends ScriptHandler {
	
	boolean hasIf = false;
	
	public LogicHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	/** Clears any set state flags on this LogicHandler. */
	public LogicHandler clearFlags() {
		hasIf = false;
		return this;
	}
	
	public void handleLogic(EArrayList<String> tokens) throws ScriptError {
		//check for impossibilities
		if (tokens.isEmpty()) { throw new ScriptError("Empty logic expression! This shouldn't be possible!", lineNum); }
		
		//check for if statement
		if (tokens.hasOne() && Keyword.IF.keyword.equals(tokens.getFirst())) {
			evaluateIf(ArgumentParser.isolateArgs(tokens));
		}
	}
	
	private boolean evaluateIf(EArrayList<String> expression) throws ScriptError {
		//check for empty expressions
		if (expression.isEmpty()) { throw new ScriptError("Empty logic expression!", lineNum); }
		
		hasIf = true;
		
		Object result = ExpressionSolver.evaluateExpression(script, expression, EDataType.BOOLEAN, lineNum).get();
		
		System.out.println(result);
		
		return false;
	}
	
}
