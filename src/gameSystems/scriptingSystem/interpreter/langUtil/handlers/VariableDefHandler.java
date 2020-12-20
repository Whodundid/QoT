package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ArgumentParser;
import gameSystems.scriptingSystem.interpreter.langUtil.ExpressionSolver;
import gameSystems.scriptingSystem.interpreter.langUtil.FunctionRunner;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.VariableUtil;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptObject;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptBoolean;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptList;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptString;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptDouble;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptInt;
import util.EUtil;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class VariableDefHandler extends ScriptHandler {
	
	public VariableDefHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleVariableDef(EArrayList<String> tokens) throws ScriptError {
		if (tokens.size() > 0) {
			Keyword word = Keyword.getKeyword(tokens.getFirst());
			EDataType datatype = EUtil.nullApplyR(word, w -> w.getDataType(), null);
			tokens.removeFirst();
			
			createVariables(script, tokens, datatype, lineNum);
		}
		else { throw new ScriptError("Missing varaible defintion parameters!", lineNum); }
	}
	
	public static void createVariables(EScript script, EArrayList<String> tokens, EDataType typeIn, int lineNum) throws ScriptError {
		String name = "";
		boolean isEquals = false;
		boolean isStatic = false;
		int expStart = -1;
		
		//check to see if the type being created is a list
		if (typeIn == EDataType.ARRAY) { createArray(script, tokens, typeIn, lineNum); }
		else {
			int total = tokens.size();
			
			for (int i = 0; i < total; i++) {
				String s = tokens.get(i);
				
				if (isEquals) {
					
					ScriptFunction func = script.getFunction(s);
					if (func != null) {
						EArrayList<String> funcArgs = ArgumentParser.isolateArgs(tokens, i);
						EArrayList<EArrayList<String>> params = ArgumentParser.isolateParams(funcArgs);
						EArrayList<ScriptObject> funcParams = new EArrayList();
						
						for (EArrayList<String> p : params) {
							funcParams.add(ExpressionSolver.evaluate(script, p, lineNum));
						}
						
						//System.out.println("func args: " + funcArgs + " " + argEnd);
						
						ScriptObject funcResult = FunctionRunner.runFunction(script, func, funcParams);
						Object toAdd = funcResult.getObjectHash();
						
						if (funcResult instanceof ScriptVariable) { toAdd = ((ScriptVariable) funcResult).get(); }
						
						int argEnd = ArgumentParser.findArgEnd(tokens, i);
						
						tokens.removeFrom(i, argEnd + 1);
						total -= (argEnd - i);
						i -= 1;
						tokens.add(i + 1, String.valueOf(toAdd));
						
						//System.out.println("AFTER: " + tokens);
					}
					
					//System.out.println(i == tokens.size() - 1);
					if (",".equals(s) || i + 1 == tokens.size()) {
						
						EArrayList<String> expArgs = new EArrayList();
						int end = (i + 1 == tokens.size()) ? i + 1 : i;
						
						for (int j = expStart; j < end; j++) {
							//System.out.println("ADDING: " + tokens.get(j) + " " + j + " " + i);
							expArgs.addIf(!tokens.get(j).isEmpty(), tokens.get(j));
						}
						
						//System.out.println(expArgs + " " + end);
						
						ScriptVariable var = null;
						Object valueVar = ExpressionSolver.evaluateExpression(script, expArgs, typeIn, lineNum).get();
						String strVar = String.valueOf(valueVar);
						
						switch (typeIn) {
						case BOOLEAN: var = new ScriptBoolean(name, Boolean.valueOf(strVar)); break;
						case INT:
						case LONG: var = new ScriptInt(name, Integer.valueOf(strVar)); break;
						case DOUBLE: var = new ScriptDouble(name, Double.valueOf(strVar)); break;
						case STRING: var = new ScriptString(name, String.valueOf(strVar)); break;
						default: throw new ScriptError("Invalid variable datatype: " + typeIn + "!", lineNum);
						}
						
						if (isStatic) { script.addStaticVariable(var); }
						else { script.stackFrames.peek().store(var); }
						isEquals = false;
						
						//throw new ScriptError("Failed to create variable '" + name + "' in script '" + script.getName() + "'!", lineNum);
					}
				}
				else {
					isEquals = s.equals(Keyword.ASSIGN.keyword);
					
					//System.out.println("here " + s + " " + isEquals);
					if (!isEquals) { name = s; }
					else { expStart = i + 1; }
				}
			}
		}
	}
	
	private static void createArray(EScript script, EArrayList<String> tokens, EDataType typeIn, int lineNum) throws ScriptError {
		//System.out.println("array tokens: " + tokens);
		
		boolean isEquals = false;
		boolean isStatic = false;
		String name = "";
		EDataType listType = EDataType.OBJECT; //default to base object
		int numTypes = 0;
		int typeStart = -1;
		boolean hasArgs = false; //don't necessarily assume the list declaration has values
		int arrStart = -1;
		EArrayList<String> arrArgs = new EArrayList();
		
		for (int i = 0; i < tokens.size(); i++) {
			String s = tokens.get(i);
			
			//check for opening type braces
			if ("<".equals(s)) {
				if (numTypes++ == 0) { typeStart = i + 1; }
			}
			//check for closing type braces
			else if (">".equals(s)) {
				if (--numTypes == 0) {
					EArrayList<String> params = new EArrayList(tokens, typeStart, i);
					//System.out.println(params);
					listType = VariableUtil.findBaseType(params);
				}
			}
			
			if (isEquals) {
				//check for opening array values
				if ("[".equals(s)) {
					if (arrStart < 0) { arrStart = i + 1; }
				}
				//check for closing array values
				else if ("]".equals(s)) {
					if (arrStart < 0) { throw new ScriptError("Incomplete list declaration! Missing opening [ bracket.", lineNum); }
					arrArgs.addFrom(tokens, arrStart, i);
				}
			}
			else {
				isEquals = s.equals(Keyword.ASSIGN.keyword);
				if (!isEquals) { name = s; }
			}
		}
		
		//System.out.println(name + " " + listType + " " + arrArgs);
		
		//create the list and add the values (if any) to it
		ScriptList list = new ScriptList(name, listType);
		
		//int expStart = -1;
		int parens = 0;
		int argStart = 0;
		
		for (int i = 0; i < arrArgs.size(); i++) {
			String s = arrArgs.get(i);
			
			if ("(".equals(s)) { parens++; }
			else if (")".equals(s)) { parens--; }
			
			if (parens == 0 && ",".equals(s) || i + 1 == arrArgs.size()) {
				EArrayList<String> argExp = new EArrayList(arrArgs, argStart, (i + 1 == arrArgs.size()) ? i + 1 : i);
				
				Object val = ExpressionSolver.evaluate(script, argExp, listType, lineNum).get();
				list.add(val);
				
				argStart = i + 1;
			}
		}
		
		//System.out.println(list.getEArrayList());
		
		if (isStatic) { script.addStaticVariable(list); }
		else { script.stackFrames.peek().store(list); }
	}
	
}
