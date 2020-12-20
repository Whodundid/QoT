package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.ArithmeticError;
import gameSystems.scriptingSystem.exceptions.errors.VariableNameError;
import gameSystems.scriptingSystem.interpreter.langUtil.ArgumentParser;
import gameSystems.scriptingSystem.interpreter.langUtil.ExpressionSolver;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptObject;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptList;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class VariableHandler extends ScriptHandler {
	
	public VariableHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleVariable(EArrayList<String> tokens) throws ScriptError {
		if (tokens.size() > 0) {
			String theVar = tokens.get(0);
			Keyword keyword = (tokens.size() > 1) ? Keyword.getKeyword(tokens.get(1)) : null;
			
			//System.out.println(tokens);
			
			//check for improper names
			if (!Keyword.isAllowedName(theVar)) { throw new VariableNameError(theVar, lineNum); }
			ScriptObject var = script.getVar(theVar);
			
			//determine what kind of variable this is
			if (var instanceof ScriptVariable) { variable(keyword, (ScriptVariable) var, tokens); }
			else if (var != null) {
				if (var.getName().equals("System")) {
					StorageBox<Object, EDataType> result = script.getSystem().handleFunction(tokens.get(1), tokens);
					//System.out.println(result);
				}
				else { object(var, tokens); }
			}
			else { throw new ScriptError("Variable '" + theVar + "' has not been decalred within this scope!", lineNum); }
		}
	}
	
	private void variable(Keyword keyword, ScriptVariable var, EArrayList<String> tokens) throws ScriptError {
		if (keyword.isOperator()) {
			switch (keyword) {
			case ASSIGN:
			case SHIFT_RIGHT:
			case SHIFT_LEFT:
				Object val = ExpressionSolver.evaluateExpression(script, tokens, var.getDataType(), lineNum).get();
				if (val != null) { var.set(val); }
				break;
			default:
			}
		}
		else if (keyword.isArithmeticOperator()) {
			Object val = ExpressionSolver.evaluateExpression(script, tokens, var.getDataType(), lineNum).get();
			if (val != null) { var.set(val); }
		}
		else { throw new ScriptError("Invalid variable expression keyword " + keyword + "!", lineNum); }
	}
	
	public void object(ScriptObject var, EArrayList<String> tokens) throws ScriptError {
		switch (var.getDataType()) {
		case ARRAY: array((ScriptList) var, tokens.removeFrom(0, 1)); break;
		default: break;
		}
	}
	
	public void array(ScriptList list, EArrayList<String> tokens) throws ScriptError {
		System.out.println("array: " + tokens);
		
		if (tokens.isNotEmpty()) {
			int start = -1;
			int end = -1;
			int brackets = 0;
			int index = -1;
			
			if (tokens.size() >= 2) {
				//determine what action on the array is going to be performed
				if (".".equals(tokens.get(0))) {
					//the length value isn't a valid start for a statement -- throw error
					if ("length".equals(tokens.get(1))) {
						//tokens.replaceFrom(0, 2, list.size() + "");
						throw new ScriptError("Invalid statement declaration! Cannot begin a statement with an array's length!", lineNum);
					}
					else {
						handleArrayFunction(list, tokens);
					}
				}
				else if ("[".equals(tokens.get(0))) {
					if (brackets++ == 0) { start = 1; tokens.removeFirst(); } 
				}
			}
			
			if (brackets > 0) {
				//determine the index in the array
				for (int i = 0; i < tokens.size(); i++) {
					String s = tokens.get(i);
					
					if ("[".equals(s)) { brackets++; }
					else if ("]".equals(s) && --brackets == 0) {
						//isolate any potential expression, grab the array index, then remove the arguments from the tokens list
						EArrayList<String> exp = new EArrayList(tokens, 0, i);
						index = (int) ExpressionSolver.evaluate(script, exp, EDataType.INT, lineNum).get();
						tokens.removeFrom(0, i + 1);
					}
				}
				
				//next try to determine the action that follows, if any
				Keyword theAction = null;
				int expStart = -1;
				
				for (int i = 0; i < tokens.size(); i++) {
					String s = tokens.get(i);
					
					Keyword word = Keyword.getKeyword(s);
					if (theAction == null && word != null) {
						if (word.isArithmeticOperator()) {
							//if the action is to increment or decrement, make sure there aren't any other args
							if (word == Keyword.INCREMENT || word == Keyword.DECREMENT) {
								if (i + 1 < tokens.size()) { throw new ArithmeticError("Invalid arithmetic operation in list expression!", lineNum); }
							}
							theAction = word;
							expStart = i + 1;
							break;
						}
						else { throw new ScriptError("Invalid operation '" + word + "' on given array value!", lineNum); }
					}
				}
				
				if (expStart < 0) { throw new ScriptError("Incomplete right-side expression argument!", lineNum); }
				
				EArrayList<String> exp = new EArrayList(tokens, expStart);
				Object val = ExpressionSolver.evaluate(script, exp, list.getListType(), lineNum).get();
				
				if (list.getListType().isNumber()) {
					Double curVal = null;
					
					switch (list.getListType()) {
					case INT: curVal = (double) ((int) list.get(index)); break;
					case LONG: curVal = (double) ((long) list.get(index)); break;
					case DOUBLE: curVal = (double) list.get(index); break;
					case STRING:
					case OBJECT:
					case ARRAY: throw new ScriptError("Invalid list cast exception! This really shouldn't happen!", lineNum);
					default: break;
					}
					
					switch (theAction) {
					case ASSIGN: list.set(index, val); break;
					case INCREMENT: list.set(index, EDataType.castTo((curVal + 1), list.getListType())); break;
					case DECREMENT: list.set(index, EDataType.castTo((curVal - 1), list.getListType())); break;
					case ADD_ASSIGN: list.set(index, EDataType.castTo((curVal + (double) val), list.getListType())); break;
					case SUBTRACT_ASSIGN: list.set(index, EDataType.castTo((curVal - (double) val), list.getListType())); break;
					case MULTIPLY_ASSIGN: list.set(index, EDataType.castTo((curVal * (double) val), list.getListType())); break;
					case DIVIDE_ASSIGN: list.set(index, EDataType.castTo((curVal / (double) val), list.getListType())); break;
					case MODULUS_ASSIGN: list.set(index, EDataType.castTo((curVal % (double) val), list.getListType())); break;
					case BITWISE_AND_ASSIGN: list.set(index, EDataType.castTo((curVal.intValue() & (int) val), list.getListType())); break;
					case BITWISE_OR_ASSIGN: list.set(index, EDataType.castTo((curVal.intValue() | (int) val), list.getListType())); break;
					case BITWISE_XOR_ASSIGN: list.set(index, EDataType.castTo((curVal.intValue() ^ (int) val), list.getListType())); break;
					case SHIFT_LEFT_ASSIGN: list.set(index, EDataType.castTo((curVal.intValue() << (int) val), list.getListType())); break;
					case SHIFT_RIGHT_ASSIGN: list.set(index, EDataType.castTo((curVal.intValue() >> (int) val), list.getListType())); break;
					default: break;
					}
				}
				else if (list.getListType() == EDataType.STRING) {
					String curVal = (String) list.get(index);
					
					switch (theAction) {
					case ASSIGN: list.set(index, val); break;
					case ADD_ASSIGN: list.set(index, curVal + (String) val); break;
					default: throw new ScriptError("Invalid string arithmetic exception! Cannot perform the operation '" + theAction + "' on strings!", lineNum);
					}
				}
				else if (theAction == Keyword.ASSIGN) {
					list.set(index, val);
				}
				else { throw new ArithmeticError("Invalid arithmetic operation!", lineNum); }
				
				
				System.out.println("INDEX '" + index + "' NEW VAL : " + list.get(index));
			}
		}
		else { throw new ScriptError("Incomplete statement declaration!", lineNum); }

	}
	
	private void handleArrayFunction(ScriptList list, EArrayList<String> args) throws ScriptError {
		int total = args.size();
		
		ArgumentParser.isolateArgs(args);
		
		for (int i = 0; i < total; i++) {
			if (i + 1 < args.size() && ".".equals(args.get(i))) {
				String input = args.get(i + 1);
				EArrayList<String> functionArgs = new EArrayList();
				
				if (i + 3 < args.size() && "(".equals(args.get(i + 2))) {
					int funcStart = i + 3;
					int numParens = 1;
					int end = -1;
					EArrayList<String> expArgs = new EArrayList();
					
					for (int j = funcStart; j < args.size(); j++) {
						String s1 = args.get(j);
						if ("(".equals(s1)) { numParens++; }
						if (")".equals(s1)) {
							if (--numParens == 0) { end = j; }
						}
					}
					
					System.out.println(funcStart + " " + end);
					
					expArgs.addFrom(args, funcStart, end);
					
					args.removeFrom(funcStart - 3, end + 1);
					total -= expArgs.size() + 4;
					System.out.println(args + " " + i + " " + total + " " + expArgs);
					
					//now process each argument as an expression and compile the results into the args to be passed
					
				}
				else {
					args.removeFrom(i, i + 3);
					total -= 2;
				}
				
				System.out.println("funcArgs: " + functionArgs);
				total -= functionArgs.size();
				i -= functionArgs.size();
				
				//HERE
				System.out.println("the input: " + input + " : " + functionArgs);
				StorageBox<Object, EDataType> result = list.handleFunction(input, functionArgs);
				break;
			}
		}
		
		
	}
	
}
