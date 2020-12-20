package gameSystems.scriptingSystem.interpreter.langUtil.handlers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.FunctionRunner;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.ScriptHandler;
import util.storageUtil.EArrayList;

public class FunctionHandler extends ScriptHandler {
	
	public FunctionHandler(ScriptLineParser parserIn) {
		super(parserIn);
	}
	
	public void handleFunction(EArrayList<String> tokens) throws ScriptError {
		FunctionRunner.parseAndRunFunction(script, tokens, lineNum);
	}
	
}

/*
if (first != null) {
			switch (first) {
			case PRINT: PrintFunctions.handlePrint(script, isolateArgs(tokens), false, lineNum); break;
			case PRINTLN: PrintFunctions.handlePrint(script, isolateArgs(tokens), true, lineNum); break;
			default: break;
			}
		}
		else {
			String name = tokens.get(0);
			
			if (script.getFuncDepth() == 1) {
				ScriptFunction func = script.getFunction(name);
				
				Stack<Integer> positions = new Stack();
				int argStart = -1;
				
				if (func != null) {
					int argNum = 0;
					
					int total = tokens.size();
					
					for (int i = 1; i < total; i++) {
						String s = tokens.get(i);
						
						if (s.equals("(")) {
							//keep track of the start of the arguments
							if (argStart == -1) {
								//System.out.println("arg start: " + i);
								argStart = i;
							}
							positions.push(i);
						}
						else if (s.equals(")")) {
							if (positions.isEmpty()) { throw new ScriptError("Missing paired '(' for function definition!", lineNum); }
							if (positions.peek() == argStart) {
								//box the arguments into the function
								
								EArrayList<Object> args = new EArrayList();
								
								for (int j = argStart + 1; j < i; j++) {
									String sarg = tokens.get(j);
									ScriptObject obj = null;
									
									//System.out.println("CUR: " + tokens.get(j));
									
									int cur = 0;
									
									if (sarg.equals(",")) { cur++; }
									else {
										if (sarg.startsWith("\"")) { sarg = sarg.substring(1, sarg.length() - 1); }
										args.add(sarg);
									}
								}
								
								//System.out.println("boxing: " + i + " : " + args);
								
								FunctionRunner.runFunction(script, func, args);
								
							}
							else {
								//grab the arguments to build an expression
								int start = positions.pop();
								EArrayList<String> expression = new EArrayList();
								for (int j = start + 1; j < i; j++) {
									expression.add(tokens.get(j));
								}
								
								Object subResult = ExpressionSolver.evaluateExpression(script, expression, func.getParamTypes().get(argNum), lineNum);
								
								tokens.set(start, String.valueOf(subResult));
								for (int j = start + 1; j < i; j++) {
									String a = tokens.remove(start + 1);
									//System.out.println("REMOVING: " + a);
								}
								total -= (expression.size() + 3);
								i = start;
							}
						}
						else if (s.equals(",")) {
							//System.out.println("number of args inbetween: " + (i - (argStart + 1)));
							if (i - argStart >= 1) {
								
							}
						}
					} //for end
					
				}
			}
			//get function from script
		}

*/
