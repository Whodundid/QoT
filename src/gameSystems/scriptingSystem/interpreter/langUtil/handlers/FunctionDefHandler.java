package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ArgumentParser;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ParsedFunction;
import gameSystems.scriptingSystem.util.ScriptHandler;
import gameSystems.scriptingSystem.util.enums.LineType;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import util.storageUtil.EArrayList;

public class FunctionDefHandler extends ScriptHandler {
	
	public ScriptFunction curFunc = null;
	
	public FunctionDefHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void addToFunction(EArrayList<String> tokens) throws ScriptError {
		if (tokens.hasOnlyOne() && tokens.getFirst().equals("}")) {
			parser.lastType = LineType.SCOPE_CLOSE;
			parser.lastLine = lineNum;
			
			script.lastFuncLines.pop();
			parser.addToFunc = false;
		}
		else if (!"{".equals(tokens.getFirst())) {
			curFunc.addActionLine(lineNum, tokens);
		}
	}
	
	public void handleFunctionDef(EArrayList<String> tokens) throws ScriptError {
		ParsedFunction func = ArgumentParser.parseFunctionDef(tokens);
		
		curFunc = new ScriptFunction(script.getCurFun(), func.returnType, func.name, func.parameters, script.getFuncDepth());
		
		parser.enforceFuncScope = true;
		parser.addToFunc = true;
		
		parser.lastType = LineType.FUNCTIONDEF;
		parser.lastLine = lineNum;
		
		script.lastFuncLines.push(lineNum);
		script.addFunction(curFunc);
		
		/*
		if (first.isDataType()) {
			EDataType returnType = null;
			String funcName = null;
			EArrayList<ScriptObject> funcArgs = new EArrayList();
			
			//functions must be decalred in the following order: [ returnType, name, (, args <dataType and name>, ), { (optional on this line) ]
			//go until the conditions for a function definition are met, otherwise try to parse as a variable def
			//a function must be at least a minimum of 5 tokens
			if (tokens.size() >= 5) {
				
				//if the token at this point isn't an '(' then it's definitely not a function -- try to parse it as a variable def
				if (!tokens.get(2).equals("(")) { parser.variableDefHandler.handleVariableDef(first, tokens); return; }
				else {
					returnType = first.getDataType();
					funcName = tokens.get(1);
					
					StorageBoxHolder<EDataType, String> argsAndNames = new StorageBoxHolder();
					
					int i = 3;
					int numArgs = 0;
					int cur = 0;
					Keyword last = null;
					EDataType datatype = null;
					String argName = null;
					
					//grab the arguments from the function definition, if any
					for (; i < tokens.size(); i++) {
						String s = tokens.get(i);
						Keyword type = Keyword.getKeyword(s);
						
						//System.out.println("arg: " + s);
						
						//end of arguments -- time to check
						if (s.equals(")")) {
							
							break;
						}
						else if (s.equals(",")) {
							numArgs++;
						}
						else if (type != null) {
							if (last != null) { throw new ScriptError("Invalid script function argument definition! Cannot have a keyword following a keyword as an argument!", lineNum); }
							
							datatype = type.getDataType();
							last = type;
							cur++;
						}
						else {
							if (cur == 0) {
								datatype = EDataType.OBJECT;
								cur++;
							}
							else {
								argName = s;
								argsAndNames.add(datatype, argName);
								cur = 0;
								last = null;
							}
						}
					}
					
					for (StorageBox<EDataType, String> box : argsAndNames) {
						EDataType type = box.getObject();
						String name = box.getValue();
						
						if (type == null || name == null) {
							throw new ScriptError("Incomplete function argument declaration!", lineNum);
						}
						
						switch (type) {
						case BOOLEAN: funcArgs.add(new ScriptBoolean(script, name)); break;
						case LONG: funcArgs.add(new ScriptInt(script, name)); break;
						case DOUBLE: funcArgs.add(new ScriptDouble(script, name)); break;
						case STRING: funcArgs.add(new ScriptString(script, name)); break;
						case ARRAY: break;
						case ENUM: break;
						case OBJECT: funcArgs.add(new ScriptObject(script, name)); break;
						default: break;
						}
					}
					
					//System.out.println("here: " + (i + 1) + " " + tokens.size());
					if (i + 1 < tokens.size()) {
						
						if (!tokens.get(i + 1).equals("{")) {
							parser.enforceFuncScope = true;
						}
						else {
							script.openFuncScopes++;
							script.lastScopes.push(ScopeType.FUNCTION);
						}
						
						parser.lastType = LineType.FUNCTIONDEF;
						parser.lastLine = lineNum;
						script.lastFuncLines.push(lineNum);
						script.lastFuncReturnTypes.push(returnType);
					}
					
					if (script.getFunction(funcName) != null) { throw new ScriptError("Duplicate function with name '" + funcName + "' detected!", lineNum); }
					
					//don't think this is completely right
					ScriptFunction funcDef = new ScriptFunction(script, script.getCurFun(), returnType, funcName, script.getFuncDepth());
					funcDef.setArguments(funcArgs);
					script.pushFunc(script.lastFuncs.push(funcDef));
				}
			}
		}
		*/
	}
	
}
