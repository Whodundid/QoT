package gameSystems.scriptingSystem.interpreter.langUtil;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.NullVariableError;
import gameSystems.scriptingSystem.util.VariableUtil;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptObject;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptList;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class PrintFunctions {

	public static void handlePrint(EScript scriptIn, EArrayList<String> args, boolean newLine, int lineNum) throws ScriptError {
		if (args.isNotEmpty()) {
			
			EArrayList<Object> printArgs = new EArrayList();
			int expStart = -1;
			int parens = 0;
			int total = args.size();
			
			for (int i = 0; i < total; i++) {
				String s = args.get(i);
				
				if ("(".equals(s)) {
					if (parens == 0) { expStart = i + 1; }
					parens++;
				}
				else if (")".equals(s)) {
					parens--;
					if (parens == 0 && expStart > 0) {
						EArrayList<String> expArgs = new EArrayList(args, expStart, i);
						
						//evaluate the expression
						printArgs.add(ExpressionSolver.evaluateExpression(scriptIn, expArgs, lineNum).get());
					}
				}
				//add all strings
				else if (VariableUtil.isString(s)) {
					//remove the starting and ending " chars
					printArgs.add(s.substring(1, s.length() - 1));
				}
				//ignore + character 'not sure if should do this! :)'
				else if ("+".equals(s) && parens == 0) { continue; }
				//get mad at any other annoying characters
				//else if ("-".equals(s) && parens == 0) { throw new ScriptError("Invalid character: ':" + s + "' in print statement", lineNum); }
				//check for numbers and variables
				else if (parens == 0) {
					if (VariableUtil.isNumber(s)) { printArgs.add(s); }
					else if (VariableUtil.isBoolean(s)) { printArgs.add(s); }
					//check for variables
					else {
						ScriptObject var = scriptIn.getVar(s);
						//boolean isFunc = false;
						
						//check for function call
						if (var == null) {
							var = scriptIn.getFunction(s);
							//if (var != null) { isFunc = true; }
						}
						
						//if the variable is null, throw error
						if (var == null) { throw new NullVariableError(s, lineNum); }
						
						if (var instanceof ScriptVariable) {
							String varVal = String.valueOf(((ScriptVariable) var).get());
							if (VariableUtil.isString(varVal)) { varVal = varVal.substring(1, varVal.length() - 1); }
							
							printArgs.add(varVal);
						}
						else if (var instanceof ScriptList) {
							ScriptList list = (ScriptList) var;
							
							if (i + 2 < args.size() && ".".equals(args.get(i + 1))) {
								String input = args.get(i + 2);
								EArrayList<String> functionArgs = new EArrayList();
								
								if (i + 3 < args.size() && "(".equals(args.get(i + 3))) {
									int funcStart = i + 4;
									int numParens = 1;
									int end = -1;
									
									for (int j = i + 4; j < args.size(); j++) {
										String s1 = args.get(j);
										
										if ("(".equals(s1)) { numParens++; }
										if (")".equals(s1)) {
											if (--numParens == 0) { end = j; }
										}
									}
									
									functionArgs.addFrom(args, funcStart, end);
									args.removeFrom(funcStart - 4, end + 1);
									total -= functionArgs.size() + 5;
									i -= 1;
									//System.out.println(args + " " + i + " " + total);
								}
								else {
									args.removeFrom(i, i + 3);
									total -= 3;
									i -= 1;
									//System.out.println(args + " " + i + " " + total);
								}
								
								//System.out.println("funcArgs: " + functionArgs);
								total -= functionArgs.size();
								i -= functionArgs.size();
								
								//HERE
								//System.out.println("the input: " + input);
								StorageBox<Object, EDataType> result = list.handleFunction(input, functionArgs);
								
								printArgs.add(result.getA());
							}
							//if this is the last token or the next token isn't an array bracket, just print the toString representation of the list
							else if (i + 1 == args.size() || !"[".equals(args.get(i + 1))) {
								//System.out.println(args + " " + i + " " + total);
								printArgs.add(list.toString());
							}
							else {
								int start = -1;
								int brackets = 0;
								int index = -1;
								
								for (int j = i + 1; j < args.size(); j++) {
									String s1 = args.get(j);
									if ("[".equals(s1)) {
										if (brackets++ == 0) { start = j + 1; }
									}
									else if ("]".equals(s1)) {
										if (--brackets == 0) {
											EArrayList<String> exp = new EArrayList(args, start, j);
											args.removeFrom(start - 2, j + 1); // remove the array from the args
											total -= exp.length() + 3; // decrement the total number of args from the loop
											i -= exp.length(); // decrement the current position back to before the array
											
											index = (int) (long) ((ScriptVariable) ExpressionSolver.evaluate(scriptIn, exp, EDataType.INT, lineNum)).get();
											Object obj = list.get(index);
											
											if (VariableUtil.isString(String.valueOf(obj))) {
												obj = ((String) obj).substring(1, ((String) obj).length() - 1);
											}
											
											printArgs.add(obj);
											break;
										}
									}
								}
								
							}
						}
						else if (var instanceof ScriptFunction) {
							ScriptFunction func = (ScriptFunction) var;
							
							//check if the function's return type is void
							if (func.getReturnType() == EDataType.VOID) { throw new ScriptError("Cannot print a void value!", lineNum); }
							
							EArrayList<String> funcArgs = ArgumentParser.isolateArgs(args, i);
							EArrayList<EArrayList<String>> params = ArgumentParser.isolateParams(funcArgs);
							EArrayList<ScriptObject> funcParams = new EArrayList();
							
							for (EArrayList<String> p : params) {
								funcParams.add(ExpressionSolver.evaluate(scriptIn, p, lineNum));
							}
							
							ScriptObject funcResult = FunctionRunner.runFunction(scriptIn, func, funcParams);
							
							//remove the function call from the print args
							int num = 0, end = -1;
							
							for (int j = i; j < args.size(); j++) {
								String s1 = args.get(j);
								
								if ("(".equals(s1)) { num++; }
								else if (")".equals(s1)) {
									if (--num == 0) { end = j + 1; break; }
								}
							}
							
							total -= (end - i);
							args.removeFrom(i, end);
							
							//print the function's result
							if (funcResult instanceof ScriptVariable) {
								ScriptVariable result = (ScriptVariable) funcResult;
								printArgs.add(result.get());
							}
							else if (funcResult != null) {
								printArgs.add(funcResult);
							}
						}
						else {
							printArgs.add(var.getObjectHash());
						}
						
					}
				}
			}
			
			print(printArgs, newLine);
		}
		else if (newLine) { System.out.println(); }
		else { System.out.print(""); }
	}
	
	public static void print(EArrayList<Object> args, boolean newLine) throws ScriptError {
		String out = "";
		for (Object o : args) {
			out += (o != null) ? o.toString() : "null";
		}
		
		//System.out.println("the args to be printed: " + args);
		
		if (newLine) { System.out.println(out); }
		else { System.out.print(out); }
	}
	
}
