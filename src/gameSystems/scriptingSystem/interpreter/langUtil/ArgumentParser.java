package gameSystems.scriptingSystem.interpreter.langUtil;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.util.ParsedFunction;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBoxHolder;

public class ArgumentParser {

	public static StorageBoxHolder<EDataType, String> getFunctionArguments(EArrayList<String> tokens) throws ScriptError {
		StorageBoxHolder<EDataType, String> args = new StorageBoxHolder();
		
		EArrayList<String> isolatedArgs = isolateArgs(tokens);
		EArrayList<EArrayList<String>> isolatedParams = isolateParams(isolatedArgs);
		
		for (EArrayList<String> param : isolatedParams) {
			if (param.size() != 2) { throw new ScriptError("Invalid function parameter: '" + param + "'!"); }
			
			EDataType type = Keyword.getScriptDataType(EDataType.getDataType(param.get(0)));
			String name = param.get(1);
			
			args.add(type, name);
		}
		
		return args;
	}
	
	public static EArrayList<String> isolateArgs(EArrayList<String> tokens) throws ScriptError { return isolateArgs(tokens, 0); }
	public static EArrayList<String> isolateArgs(EArrayList<String> tokens, int startPos) throws ScriptError {
		int start = -1;
		int num = 0;
		int end = -1;
		
		for (int i = startPos; i < tokens.size(); i++) {
			String s = tokens.get(i);
			
			if ("(".equals(s)) {
				if (num++ == 0) { start = i + 1; }
			}
			else if (")".equals(s)) {
				if (--num == 0) {
					end = i;
					break;
				}
			}
		}
		
		return new EArrayList(tokens, start, end);
	}
	
	public static EArrayList<EArrayList<String>> isolateParams(EArrayList<String> tokens) throws ScriptError {
		EArrayList<EArrayList<String>> params = new EArrayList();
		
		int start = 0;
		int num = 0;
		
		for (int i = 0; i < tokens.size(); i++) {
			String s = tokens.get(i);
			
			if ("(".equals(s)) { num++; }
			else if (")".equals(s)) { num--; }
			
			if (",".equals(s) && num == 0) {
				if (i - start > 0) {
					params.add(new EArrayList(tokens, start, i));
				}
				start = i + 1;
			}
			else if (i == tokens.size() - 1) {
				params.add(new EArrayList(tokens, start, i + 1));
			}
		}
		
		return params;
	}
	
	public static EArrayList<Object> evaluateFunctionArgs(EScript scriptIn, ScriptFunction func, EArrayList<String> tokens, int lineNum) throws ScriptError {
		EArrayList<Object> params = new EArrayList();
		EArrayList<String> isolatedArgs = isolateArgs(tokens);
		EArrayList<EArrayList<String>> isolatedParams = isolateParams(isolatedArgs);
		
		for (int i = 0; i < isolatedParams.size(); i++) {
			EArrayList<String> param = isolatedParams.get(i);
			
			if (param.isNotEmpty()) {
				params.add(ExpressionSolver.evaluate(scriptIn, param, func.getParamTypes().get(i), lineNum).get());
			}
		}
		
		System.out.println(params);
		
		return params;
	}
	
	public static ParsedFunction parseFunctionDef(EArrayList<String> tokens) throws ScriptError {
		ParsedFunction func = null;
		
		//this isn't a function declaration if there aren't at least 4 tokens
		if (tokens.size() >= 4) {
			boolean isFunc = false;
			boolean hasOpen = false;
			int num = 0;
			
			for (int i = 0; i < tokens.size(); i++) {
				String s = tokens.get(i);
				
				if ("(".equals(s)) {
					hasOpen = true;
					num++;
				}
				else if (")".equals(s)) {
					if (!hasOpen) {
						isFunc = false;
						break;
					}
					
					if (--num == 0) {
						if (!tokens.atEnd(i)) { throw new ScriptError("Improper function declaration!"); }
						isFunc = true;
					}
				}
			}
			
			//if it's not a function, return
			if (!isFunc) { return null; }
			
			func = new ParsedFunction();
			
			func.returnType = Keyword.getScriptDataType(EDataType.getDataType(tokens.get(0)));
			func.name = tokens.get(1);
			func.parameters = getFunctionArguments(tokens);
			
			return func;
		}
		
		return null;
	}
	
	public static int findArgEnd(EArrayList<String> tokens) { return findArgEnd(tokens, 0); }
	public static int findArgEnd(EArrayList<String> tokens, int startPos) {
		int num = 0;
		
		for (int i = startPos; i < tokens.size(); i++) {
			String s = tokens.get(i);
			
			if ("(".equals(s)) { num++; }
			else if (")".equals(s)) {
				if (--num == 0) { return i; }
			}
		}
		
		return -1;
	}
	
}
