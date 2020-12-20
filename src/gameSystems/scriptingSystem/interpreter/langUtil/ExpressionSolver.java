package gameSystems.scriptingSystem.interpreter.langUtil;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.ArithmeticError;
import gameSystems.scriptingSystem.exceptions.errors.ExpressionError;
import gameSystems.scriptingSystem.exceptions.errors.NullVariableError;
import gameSystems.scriptingSystem.exceptions.errors.VariableNameError;
import gameSystems.scriptingSystem.util.VariableUtil;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptNullVariable;
import gameSystems.scriptingSystem.variables.ScriptObject;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptBoolean;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptString;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptDouble;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptInt;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptNumber;
import java.util.Stack;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class ExpressionSolver {
	
	public static ScriptVariable evaluateExpression(EScript scriptIn, EArrayList<String> args, int lineNum) throws ScriptError {
		return evaluateExpression(scriptIn, args, null, lineNum);
	}
	
	/** Attempts to parse through a series of string inputs to properly evauluate the result of an expression. */
	public static ScriptVariable evaluateExpression(EScript scriptIn, EArrayList<String> args, EDataType asType, int lineNum) throws ScriptError {
		
		if (args.isEmpty()) { throw new ExpressionError("Invalid expression declaration!", lineNum); }
		else if (args.hasOnlyOne()) { return evaluateSingleArg(scriptIn, args.getFirst(), asType, lineNum); }
		else if (args.size() == 2) { return evaluateTwoArg(scriptIn, args, asType, lineNum); }
		else if (args.size() > 2) { return evaluateMultiArg(scriptIn, args, asType, lineNum); }
		
		return null;
	}
	
	private static ScriptVariable evaluateSingleArg(EScript scriptIn, String arg, int lineNum) throws ScriptError { return evaluateSingleArg(scriptIn, arg, null, lineNum); }
	private static ScriptVariable evaluateSingleArg(EScript scriptIn, String arg, EDataType asType, int lineNum) throws ScriptError {
		
		ScriptObject var = scriptIn.getVar(arg);
		
		if (var instanceof ScriptVariable) {
			//if the type is null, don't cast it as anything
			if (asType == null) { return (ScriptVariable) var; }
			
			//otherwise cast it to the specified type
			return VariableUtil.cast(((ScriptVariable) var), asType, lineNum);
		}
		else if (VariableUtil.isNumber(arg) || VariableUtil.isString(arg) || VariableUtil.isBoolean(arg)) {
			
			//handle dynamic checking first
			if (asType == null) {
				
				//determine the datatype
				if (VariableUtil.isNumber(arg)) { return VariableUtil.createVar(EDataType.getNumberType(arg), arg); }
				else if (VariableUtil.isString(arg)) { return ScriptString.of(arg); }
				else if (VariableUtil.isBoolean(arg)) { return ScriptBoolean.of(arg); }
				else { ScriptString.of(arg); }
			}
			
			if (VariableUtil.isNumber(arg)) {
				Double val = Double.parseDouble(arg);
				
				return ScriptNumber.of(EDataType.castTo(val, asType));
			}
			else if (VariableUtil.isString(arg)) {
				switch (asType) {
				case INT:
				case LONG: return ScriptInt.of(arg);
				case FLOAT:
				case DOUBLE: return ScriptDouble.of(arg);
				case BOOLEAN: return ScriptBoolean.of(arg);
				case STRING: return ScriptString.of(arg);
				default: throw new ScriptError("Invalid cast type '" + asType + "'!", lineNum);
				}
			}
			else if (VariableUtil.isBoolean(arg)) {
				switch (asType) {
				case INT:
				case LONG:
					if (arg.equals("false")) { return ScriptInt.ZERO(); }
					else if (arg.equals("true")) { return ScriptInt.of(1); }
					else { return ScriptInt.of(-1); }
				case FLOAT:
				case DOUBLE:
					if (arg.equals("false")) { return ScriptDouble.ZERO(); }
					else if (arg.equals("true")) { return ScriptDouble.of(1.0); }
					else { return ScriptDouble.of(-1.0); }
				case BOOLEAN:
					return ScriptBoolean.of(arg);
				case STRING:
					return ScriptString.of(arg);
				default: throw new ScriptError("Invalid cast type '" + asType + "'!", lineNum);
				}
			}
			else {
				throw new ScriptError("Error! Do not know how to cast: " + arg + " to '" + asType + "'!", lineNum);
			}
		}
		
		return null;
	}
	
	private static ScriptVariable evaluateTwoArg(EScript scriptIn, EArrayList<String> args, int lineNum) throws ScriptError {
		return evaluateTwoArg(scriptIn, args, null, lineNum);
	}
	
	private static ScriptVariable evaluateTwoArg(EScript scriptIn, EArrayList<String> args, EDataType asType, int lineNum) throws ScriptError {
		String arg1 = args.get(0);
		String arg2 = args.get(1);
		
		boolean pre = true;
		Keyword key = Keyword.getKeyword(arg1);
		String varName = arg2;
		
		//determine which is the keyword
		if (key == null) {
			pre = false;
			key = Keyword.getKeyword(arg2);
			varName = arg1;
		}
		
		if (varName == null || varName.isEmpty()) { throw new NullVariableError(varName, lineNum); }
		if (Character.isDigit(varName.charAt(0))) { throw new VariableNameError(varName, lineNum); }
		
		ScriptObject var = scriptIn.getVar(varName);
		if (var == null) { throw new NullVariableError(varName, lineNum); }
		
		//there should only be 2 functions that can possibly be performed with only one variable and one keyword argument
		
		if (key != null && var instanceof ScriptVariable) {
			//make sure the variable is a number
			if (!var.getDataType().isNumber() || !(var instanceof ScriptNumber)) {
				throw new ArithmeticError("The variable '" + varName + "' cannot have the expression operation " + key + " performed on it!", lineNum);
			}
			
			switch (key) {
			case INCREMENT: return increment((ScriptVariable) var, lineNum, pre);
			case DECREMENT: return decrement((ScriptVariable) var, lineNum, pre);
			default: throw new ScriptError("Invalid expression operation!", lineNum);
			}
		}
		
		return null;
	}
	
	private static ScriptVariable evaluateMultiArg(EScript scriptIn, EArrayList<String> args, int lineNum) throws ScriptError {
		return evaluateMultiArg(scriptIn, args, null, lineNum);
	}
	
	private static ScriptVariable evaluateMultiArg(EScript scriptIn, EArrayList<String> args, EDataType asType, int lineNum) throws ScriptError {
		System.out.println("multi args: " + args + " [" + lineNum + "]");
		
		String name = args.get(0);
		
		ScriptObject varo = scriptIn.getVar(name);
		
		Keyword keyword = Keyword.getKeyword(args.get(1));
		
		//check to see if this expression is a variable assignment
		if (varo instanceof ScriptVariable && keyword != null) {
			ScriptVariable var = (ScriptVariable) varo;
			EArrayList<String> argsStored = new EArrayList(args);
			args = new EArrayList<String>(args.subList(2, args.size()));
			
			//System.out.println(var + " args: " + args);
			
			switch (keyword) {
			case ASSIGN: return var.set(evaluate(scriptIn, args, asType, lineNum).get());
			case ADD_ASSIGN: return addAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case SUBTRACT_ASSIGN: return subtractAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case MULTIPLY_ASSIGN: return multiplyAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case DIVIDE_ASSIGN: return divideAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case MODULUS_ASSIGN: return modulusAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case BITWISE_AND_ASSIGN: return andAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case BITWISE_OR_ASSIGN: return orAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case BITWISE_XOR_ASSIGN: return xorAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case SHIFT_LEFT_ASSIGN: return leftShiftAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			case SHIFT_RIGHT_ASSIGN: return rightShiftAssign(var, evaluate(scriptIn, args, asType, lineNum), asType, lineNum);
			default: args = argsStored; break;
			}
			
		}
		
		//if it's not a variable assignment, try to evaluate it as an expression
		return evaluate(scriptIn, args, asType, lineNum);
	}
	
	public static ScriptVariable evaluate(EScript scriptIn, EArrayList<String> args, int lineNum) throws ScriptError { return evaluate(scriptIn, args, null, lineNum); }
	public static ScriptVariable evaluate(EScript scriptIn, EArrayList<String> args, EDataType asType, int lineNum) throws ScriptError {
		//System.out.println("EVALUATE INPUT: " + args + " Size: " + args.size() + " [" + lineNum + "]");
		
		int total = args.size();
		
		if (args.contains("(")) {
			if (args.notContains(")")) { throw new ScriptError("Incomplete expression! Missing one or more ')'!", lineNum); }
			
			Stack<Integer> positions = new Stack();
			int funcStart = -1;
			
			//go through each token
			for (int i = 0; i < total; i++) {
				String s = args.get(i);
				
				//System.out.println("i: " + i + " " + s);
				
				ScriptFunction func = scriptIn.getFunction(s);
				
				//check for function calls
				if (func != null) {
					funcStart = i;
					
					//System.out.println("func: " + func + " " + funcStart);
					
					//gather the arguments
					EArrayList<String> funcArgs = ArgumentParser.isolateArgs(args, funcStart);
					EArrayList<EArrayList<String>> params = ArgumentParser.isolateParams(funcArgs);
					EArrayList<ScriptObject> funcParams = new EArrayList();
					
					for (EArrayList<String> p : params) {
						funcParams.add(ExpressionSolver.evaluate(scriptIn, p, lineNum));
					}
					
					//System.out.println("func args: " + funcArgs + " " + argEnd);
					
					ScriptObject funcResult = FunctionRunner.runFunction(scriptIn, func, funcParams);
					Object toAdd = funcResult.getObjectHash();
					
					if (funcResult instanceof ScriptVariable) { toAdd = ((ScriptVariable) funcResult).get(); }
					
					int argEnd = ArgumentParser.findArgEnd(args, funcStart);
					
					args.removeFrom(i, argEnd + 1);
					total -= (argEnd - i);
					i -= 1;
					args.add(i + 1, String.valueOf(toAdd));
					
					//System.out.println("func result: " + toAdd);
					//System.out.println("AFTER FUNCTION REMOVE: " + args + " " + i + " " + total);
					
					if (args.contains(")")) { continue; }
					else { return evaluate(scriptIn, args, asType, lineNum); }
				}
				
				else {
					//Parentheses
					if (s.equals("(")) {
						positions.push(i);
						//System.out.println("PUSHING: " + positions + " " + i);
					}
					if (s.equals(")")) {
						
						int start = positions.pop();
						
						//System.out.println("POPPING: " + positions + " " + start);
						
						//gather the tokens inbetween the parentheses
						EArrayList<String> subExpression = new EArrayList(args, start + 1, i);
						
						args.remove(i);
						total--;
						//i--;
						
						System.out.println("SUB: " + subExpression + " " + subExpression.size());
						
						ScriptVariable subResult = evaluate(scriptIn, subExpression, asType, lineNum);
						
						args.set(start, String.valueOf(subResult.get()));
						for (int j = start + 1; j < i; j++) {
							String a = args.remove(start + 1);
							//System.out.println("REMOVING: " + a);
						}
						total -= (subExpression.size() + 3);
						i = start;
						
						//System.out.println("AFTER REMOVE: " + args + " " + i + " " + total);
						
						if (args.contains(")")) { continue; }
						else { return evaluate(scriptIn, args, asType, lineNum); }
					}
				}
			}
		}
		else {
			if (args.hasOnlyOne()) {
				String s = args.get(0);
				
				//first check for string
				if (VariableUtil.isString(s)) {
					switch (asType) {
					case STRING: return ScriptString.of(s);
					default: throw new ScriptError("Cannot cast the given string '" + s + "' to a '" + asType + "'!", lineNum);
					}
				}
				else if (VariableUtil.isNumber(s)) {
					EDataType type = asType;
					
					//dynamically determine number datatype
					if (type == null) {
						type = EDataType.getNumberType(s);
					}
					
					return VariableUtil.createVar(type, EDataType.castTo(EDataType.parseNumber(s), type));
				}
				//check for string
				else if (VariableUtil.isBoolean(s)) {
					return ScriptBoolean.of(s.toLowerCase());
				}
				
				//if it's not any of those, check if it's a variable
				ScriptObject obj = scriptIn.getVar(s);
				
				if (obj != null) {
					if (obj instanceof ScriptVariable) {
						ScriptVariable var = (ScriptVariable) obj;
						
						if (asType == null) { return var; }
						return VariableUtil.createVar(asType, EDataType.castTo(var.get(), asType));
					}
				}
			}
			else if (args.size() > 1) {
				if (asType == null || asType.isNumber()) {
					pemdas(scriptIn, args, asType, "md", lineNum);
					pemdas(scriptIn, args, asType, "as", lineNum);
					
					EDataType returnType = (asType != null) ? asType : EDataType.getNumberType(args.getFirst());
					
					//System.out.println(args.getFirst());
					
					return VariableUtil.createVar(returnType, args.getFirst());
				}
				else if (asType == EDataType.STRING) {
					System.out.println("the args: " + args);
				}
				else if (asType == EDataType.BOOLEAN) {
					String first = args.getFirst();
					
					//handle universal true
					if ("true".equals(first) && Keyword.OR.keyword.equals(args.get(1))) {
						return ScriptBoolean.TRUE();
					}
					
					//handle universal false
					if ("false".equals(first) && Keyword.AND.keyword.equals(args.get(1))) {
						return ScriptBoolean.FALSE();
					}
					
					//evaluate boolean expression
					evalBool(scriptIn, args, "and", lineNum);
					evalBool(scriptIn, args, "or", lineNum);
					
					return ScriptBoolean.of(args.getFirst());
				}
			}
		}
		
		return new ScriptNullVariable();
	}
	
	private static void pemdas(EScript scriptIn, EArrayList<String> args, EDataType asType, String action, int lineNum) throws ScriptError {
		Keyword last = null;
		Keyword next = null;
		int total = args.size();
		EDataType outType = asType;
		
		//System.out.println(action + " IN: " + args);
		
		for (int i = 0; i < total; i++) {
			String s = args.get(i);
			
			if (s == null || s.isEmpty()) { throw new ScriptError("Expression argument is somehow null or empty!", lineNum); }
			
			ScriptObject var = scriptIn.getVar(s);
			Keyword key = Keyword.getKeyword(s);
			
			//System.out.println(s + " " + var);
			
			//System.out.println("key: " + key);
			
			//catch illegal keywords at the expression's start (literally anything that isn't increment or decrement)
			if (i == 0 && key != null) {
				switch (key) {
				case INCREMENT: case DECREMENT: case SUBTRACT: break;
				default: throw new ScriptError("Invalid expression starting argument '" + key + "'!", lineNum);
				}
			}
			
			Object value = null;
			
			//check for increment/decrement keywords
			if (var instanceof ScriptVariable) {
				
				//System.out.println(s + " var: " + var);
				
				//determine datatype dynamically
				if (outType == null) {
					outType = var.getDataType();
				}
				
				if (last != null) {
					switch (last) {
					case INCREMENT: value = increment((ScriptVariable) var, lineNum, true); args.remove(i - 1); i--; total--; break;
					case DECREMENT: value = decrement((ScriptVariable) var, lineNum, true); args.remove(i - 1); i--; total--; break;
					default: break;
					}
				}
				else {
					if (i + 1 < args.size()) {
						next = Keyword.getKeyword(args.get(i + 1));
						
						if (next != null) {
							switch (next) {
							case INCREMENT: value = increment((ScriptVariable) var, lineNum, false); args.remove(i); total--; break;
							case DECREMENT: value = decrement((ScriptVariable) var, lineNum, false); args.remove(i); total--; break;
							default: break;
							}
						}
					}
				}
				
				value = (value == null) ? ((ScriptVariable) var).get() : value;
			} // if (var != null)
			
			//System.out.println("VALUE1: " + value + " " + last + " " + asType);
			
			if (value == null && Character.isDigit(s.charAt(0))) {
				
				//determine datatype dynamically
				if (outType == null) {
					outType = EDataType.getNumberType(s);
				}
				
				switch (outType) {
				case BOOLEAN: break;
				case BYTE:
				case SHORT:
				case INT:
				case LONG: value = (value == null) ? Long.parseLong(s) : value; break;
				case FLOAT:
				case DOUBLE: value = (value == null) ? Double.parseDouble(s) : value; break;
				case STRING: break;
				default: break;
				}
			}
			
			//replace the variable with the value
			if (key == null) {
				args.set(i, String.valueOf(value));
			}
			
			//actually handle multiplcation and division
			if (Keyword.isArithmetic(last)) {
				//System.out.println("THE FUNC: " + func);
				switch (action) {
				case "md":
					if (last == Keyword.MULTIPLY || last == Keyword.DIVIDE || last == Keyword.MODULUS) {
						//System.out.println("values to " + last + ": " + args.get(i - 2) + " " + args.get(i));
					}
					
					switch (last) {
					case MODULUS: value = modulus(args.get(i - 2), args.get(i), EDataType.getNumberType(args.get(i - 2)), lineNum); break;
					case MULTIPLY: value = multiply(args.get(i - 2), args.get(i), EDataType.getNumberType(args.get(i - 2)), lineNum); break;
					case DIVIDE: value = divide(args.get(i - 2), args.get(i), EDataType.getNumberType(args.get(i - 2)), lineNum); break;
					default: break;
					}
					
					break;
				case "as":
					if (last == Keyword.ADD || last == Keyword.SUBTRACT) {
						//System.out.println("values to " + last + ": " + args.get(i - 2) + " " + args.get(i));
					}
					
					switch (last) {
					case ADD: value = add(args.get(i - 2), args.get(i), EDataType.getNumberType(args.get(i - 2)), lineNum); break;
					case SUBTRACT: value = subtract(args.get(i - 2), args.get(i), EDataType.getNumberType(args.get(i - 2)), lineNum); break;
					default: break;
					}
					
					//System.out.println("the val: " + value);
					
					break;
				}
				
				//System.out.println("VALUE2: " + value + " " + last);
				
				boolean isMD = action.equals("md") && (last == Keyword.MULTIPLY || last == Keyword.DIVIDE || last == Keyword.MODULUS);
				boolean isAS = action.equals("as") && (last == Keyword.ADD || last == Keyword.SUBTRACT);
				
				if (isAS || isMD) {
					//System.out.println(isAS + " " + isMD);
					
					int start = i - 2;
					args.set(start, String.valueOf(value));
					for (int j = start; j < i; j++) {
						String a = args.remove(start + 1);
						//System.out.println("REMOVING: " + a);
					}
					total -= 2;
					i = start;
				}
				
			}
			
			last = key;
		}
		
		//System.out.println("POST: " + args);
	}
	
	private static void evalBool(EScript scriptIn, EArrayList<String> args, String action, int lineNum) {
		//check through all args
		for (int i = 0; i < args.size(); i++) {
			String s = args.get(i);
			
			ScriptObject obj = scriptIn.getVar(s);
			ScriptFunction func = scriptIn.getFunction(s);
		}
	}
	
	private static String stringConcat(EScript scriptIn, EArrayList<String> args, int lineNum) throws ScriptError {
		
		String out = "";
		
		for (int i = 0; i < args.size(); i++) {
			String s = args.get(i);
			
			if (EDataType.isNumber(s)) { }
		}
		
		return out;
	}
	
	//------------------------------------
	//ScriptArithmetic Increment/Decrement
	//------------------------------------
	
	/** Increments the given variable by one (if possible). */
	public static ScriptVariable increment(ScriptVariable var, int lineNum, boolean pre) throws ScriptError { return increment(var, 1, lineNum, pre); }
	public static ScriptVariable increment(ScriptVariable var, double amount, int lineNum, boolean pre) throws ScriptError {
		ScriptVariable preVal = ScriptNumber.of(var.get());
		
		if (var instanceof ScriptInt) { var.set(((long) var.get()) + (long) amount); return (!pre) ? preVal : ScriptNumber.of(var.get()); }
		else if (var instanceof ScriptDouble) { var.set(((double) var.get()) + amount); return (!pre) ? preVal : ScriptNumber.of(var.get()); }
		
		throw new ScriptError("Cannot increment value: " + var + " Line: " + lineNum);
	}
	
	/** Increments the given variable by one (if possible). */
	public static ScriptVariable decrement(ScriptVariable var, int lineNum, boolean pre) throws ScriptError { return increment(var, -1, lineNum, pre); }
	public static ScriptVariable decrement(ScriptVariable var, double amount, int lineNum, boolean pre) throws ScriptError {
		ScriptVariable preVal = ScriptNumber.of(var.get());
		
		if (var instanceof ScriptInt) { var.set(((long) var.get()) - (double) amount); return (!pre) ? preVal : ScriptNumber.of(var.get()); }
		else if (var instanceof ScriptDouble) { var.set(((double) var.get()) - amount); return (!pre) ? preVal : ScriptNumber.of(var.get()); }
		
		throw new ScriptError("Cannot decrement value: " + var + " Line: " + lineNum);
	}
	
	/** Increments the given variable by one (if possible). */
	public static ScriptVariable negate(ScriptVariable var, int lineNum) throws ScriptError {
		if (var instanceof ScriptInt) { var.set(~ ((long) var.get())); return var; }
		else if (var instanceof ScriptDouble) { var.set(~ ((long) var.get())); return var; }
		
		throw new ScriptError("Cannot negate value: " + var + " Line: " + lineNum);
	}
	
	//-------------------------------------
	//ScriptArithmetic Assignment Functions
	//-------------------------------------
	
	public static ScriptVariable addAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "+=", lineNum); }
	public static ScriptVariable subtractAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "-=", lineNum); }
	public static ScriptVariable multiplyAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "*=", lineNum); }
	public static ScriptVariable divideAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "/=", lineNum); }
	public static ScriptVariable modulusAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "%=", lineNum); }
	public static ScriptVariable andAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "&=", lineNum); }
	public static ScriptVariable orAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "|=", lineNum); }
	public static ScriptVariable xorAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "^=", lineNum); }
	public static ScriptVariable leftShiftAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, "<<=", lineNum); }
	public static ScriptVariable rightShiftAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, int lineNum) throws ScriptError { return doAssign(var, value, typeIn, ">>=", lineNum); }
	
	private static ScriptVariable doAssign(ScriptVariable var, ScriptVariable value, EDataType typeIn, String func, int lineNum) throws ScriptError {
		if (func.equals("/=") && value != null && value.get().equals(0)) { throw new ScriptError("Divide by zero error! Line: " + lineNum); }
		
		if (var instanceof ScriptInt) {
			long cur = (long) var.get();
			long lVal = 0;
			double dVal = 0;
			
			switch (typeIn) {
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
				lVal = (long) value.get();
				switch (func) {
				case "+=": return var.set(cur + lVal);
				case "-=": return var.set(cur - lVal);
				case "*=": return var.set(cur * lVal);
				case "/=": return var.set(cur / lVal);
				case "%=": return var.set(cur % lVal);
				case "&=": return var.set(cur & lVal);
				case "|=": return var.set(cur | lVal);
				case "^=": return var.set(cur ^ lVal);
				case "<<=": return var.set(cur << lVal);
				case ">>=": return var.set(cur >> lVal);
				}
			case FLOAT:
			case DOUBLE:
				dVal = (double) value.get();
				lVal = (long) (double) value.get();
				
				switch (func) {
				case "+=": return var.set(cur + dVal);
				case "-=": return var.set(cur - dVal);
				case "*=": return var.set(cur * dVal);
				case "/=": return var.set(cur / dVal);
				case "%=": return var.set(cur % dVal);
				case "&=": return var.set(cur & lVal);
				case "|=": return var.set(cur | lVal);
				case "^=": return var.set(cur ^ lVal);
				case "<<=": return var.set(cur << lVal);
				case ">>=": return var.set(cur >> lVal);
				}
			default: break;
			}
		}
		else if (var instanceof ScriptDouble) {
			double cur = (double) var.get();
			long lVal = 0;
			double dVal = 0;
			
			switch (typeIn) {
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
				lVal = (long) value.get();
				switch (func) {
				case "+=": return var.set(cur + lVal);
				case "-=": return var.set(cur - lVal);
				case "*=": return var.set(cur * lVal);
				case "/=": return var.set(cur / lVal);
				case "%=": return var.set(cur % lVal);
				case "&=": return var.set((long) cur & lVal);
				case "|=": return var.set((long) cur | lVal);
				case "^=": return var.set((long) cur ^ lVal);
				case "<<=": return var.set((long) cur << lVal);
				case ">>=": return var.set((long) cur >> lVal);
				}
			case FLOAT:
			case DOUBLE:
				dVal = (double) value.get();
				lVal = (long) (double) value.get();
				
				switch (func) {
				case "+=": return var.set(cur + dVal);
				case "-=": return var.set(cur - dVal);
				case "*=": return var.set(cur * dVal);
				case "/=": return var.set(cur / dVal);
				case "%=": return var.set(cur % dVal);
				case "&=": return var.set((long) cur & lVal);
				case "|=": return var.set((long) cur | lVal);
				case "^=": return var.set((long) cur ^ lVal);
				case "<<=": return var.set((long) cur << lVal);
				case ">>=": return var.set((long) cur >> lVal);
				}
			default: break;
			}
		}
		
		throw new ScriptError("Cannot assign the variable: " + var + " to the value: " + value + "! Line: " + lineNum);
	}
	
	//-------------------------------------
	//ScriptArithmetic Arithmetic Functions
	//-------------------------------------
	
	private static Object add(String one, String two, EDataType typeOut, int lineNum) throws ScriptError { return doMath(one, two, typeOut, "+", lineNum); }
	private static Object subtract(String one, String two, EDataType typeOut, int lineNum) throws ScriptError { return doMath(one, two, typeOut, "-", lineNum); }
	private static Object multiply(String one, String two, EDataType typeOut, int lineNum) throws ScriptError { return doMath(one, two, typeOut, "*", lineNum); }
	private static Object divide(String one, String two, EDataType typeOut, int lineNum) throws ScriptError { return doMath(one, two, typeOut, "/", lineNum); }
	private static Object modulus(String one, String two, EDataType typeOut, int lineNum) throws ScriptError { return doMath(one, two, typeOut, "%", lineNum); }
	
	private static Object doMath(String one, String two, EDataType typeOut, String func, int lineNum) throws ScriptError {
		if (one != null && two != null) {
			try {			
				Number oneN = EDataType.parseNumber(one);
				Number twoN = EDataType.parseNumber(two);
				
				EDataType aType = EDataType.getNumberType(one);
				EDataType bType = EDataType.getNumberType(two);
				
				Object val = null;
				
				if (oneN != null && twoN != null) {
					if (func.equals("/") && twoN.doubleValue() == 0) { throw new ScriptError("Divide by zero error! Line: " + lineNum); }
					
					switch (aType) {
					case BYTE:
					case SHORT:
					case INT:
					case LONG:
						switch (bType) {
						case BYTE:
						case SHORT:
						case INT:
						case LONG:
							switch (func) {
							case "*": return EDataType.castTo(oneN.intValue() * twoN.intValue(), typeOut);
							case "/": return EDataType.castTo(oneN.intValue() / twoN.intValue(), typeOut);
							case "+": return EDataType.castTo(oneN.intValue() + twoN.intValue(), typeOut);
							case "-": return EDataType.castTo(oneN.intValue() - twoN.intValue(), typeOut);
							case "%": return EDataType.castTo(oneN.intValue() % twoN.intValue(), typeOut);
							}
						case FLOAT:
						case DOUBLE:
							switch (func) {
							case "*": return EDataType.castTo(oneN.intValue() * twoN.doubleValue(), typeOut);
							case "/": return EDataType.castTo(oneN.intValue() / twoN.doubleValue(), typeOut);
							case "+": return EDataType.castTo(oneN.intValue() + twoN.doubleValue(), typeOut);
							case "-": return EDataType.castTo(oneN.intValue() - twoN.doubleValue(), typeOut);
							case "%": return EDataType.castTo(oneN.intValue() % twoN.doubleValue(), typeOut);
							}
						default: break;
						}
					case FLOAT:
					case DOUBLE:
						switch (bType) {
						case BYTE:
						case SHORT:
						case INT:
						case LONG:
							switch (func) {
							case "*": return EDataType.castTo(oneN.doubleValue() * twoN.intValue(), typeOut);
							case "/": return EDataType.castTo(oneN.doubleValue() / twoN.intValue(), typeOut);
							case "+": return EDataType.castTo(oneN.doubleValue() + twoN.intValue(), typeOut);
							case "-": return EDataType.castTo(oneN.doubleValue() - twoN.intValue(), typeOut);
							case "%": return EDataType.castTo(oneN.doubleValue() % twoN.intValue(), typeOut);
							}
						case FLOAT:
						case DOUBLE:
							switch (func) {
							case "*": return EDataType.castTo(oneN.doubleValue() * twoN.doubleValue(), typeOut);
							case "/": return EDataType.castTo(oneN.doubleValue() / twoN.doubleValue(), typeOut);
							case "+": return EDataType.castTo(oneN.doubleValue() + twoN.doubleValue(), typeOut);
							case "-": return EDataType.castTo(oneN.doubleValue() - twoN.doubleValue(), typeOut);
							case "%": return EDataType.castTo(oneN.doubleValue() % twoN.doubleValue(), typeOut);
							}
						default: break;
						}
					default: break;
					}
				}
			}
			catch (ScriptError e) { throw e; }
			catch (Exception e) {
				throw new ScriptError("Cannot perform the following operation '" + func + "' on the given values: '" + one + ", " + two + "'!", lineNum);
			}
		}
		return null;
	}
	
	//--------------------------------
	//ScriptArithmetic Logic Functions
	//--------------------------------
	
	
}
