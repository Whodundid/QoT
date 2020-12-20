package gameSystems.scriptingSystem.interpreter.langUtil;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.NullFunctionError;
import gameSystems.scriptingSystem.exceptions.errors.NullVariableError;
import gameSystems.scriptingSystem.util.StackFrame;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptObject;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.EUtil;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

public class FunctionRunner {
	
	public static ScriptObject parseAndRunFunction(EScript scriptIn, EArrayList<String> tokens, int lineNum) throws ScriptError {
		if (tokens.hasOne()) {
			ScriptFunction func = scriptIn.getFunction(tokens.getFirst());
			
			//if a function of the same name cannot be found within the current scope, throw an error
			if (func == null) { throw new NullFunctionError(tokens.getFirst()); }
			
			EArrayList<String> isolatedArgs = ArgumentParser.isolateArgs(tokens);
			EArrayList<EArrayList<String>> args = ArgumentParser.isolateParams(isolatedArgs);
			EArrayList<ScriptObject> evalArgs = new EArrayList();
			
			//check to make sure the number of parsed args matches the number of expected function args
			if (args.size() != func.getParamTypes().size()) {
				throw new ScriptError("Function '" + func.getName() + "' expected " + func.getParamTypes().size() + " argument(s), found " + args.size() + " instead!", scriptIn.getCurrentLine());
			}
			
			//evaluate any expressions within the args and store each expression's result within the evalArgs list
			for (int i = 0; i < args.size(); i++) {
				EArrayList<String> exp = args.get(i);
				EDataType type = func.getParamTypes().get(i);
				
				ScriptVariable val = ExpressionSolver.evaluateExpression(scriptIn, exp, scriptIn.getCurrentLine());
				evalArgs.add(val.setName(func.getParamNames().get(i)));
			}
			
			System.out.println(evalArgs);
			
			return runFunction(scriptIn, func, evalArgs);
		}
		
		return null;
	}
	
	public static ScriptObject runFunction(EScript scriptIn, ScriptFunction funcIn, EArrayList<ScriptObject> params) throws ScriptError {
		return runFunctionI(scriptIn, funcIn, boxParams(scriptIn, funcIn, params));
	}
	
	private static ScriptObject runFunctionI(EScript scriptIn, ScriptFunction funcIn, EArrayList<ScriptObject> params) throws ScriptError {
		//ensure that the script and function in are not null
		if (EUtil.notNull(scriptIn, funcIn)) {
			
			//System.out.println("func '" + funcIn.getName() + "' passed params: " + params);
			
			//create a new stack frame and add the function's parameters as values
			StackFrame frame = new StackFrame(scriptIn.getCurrentLine());
			frame.storeEach(params);
			
			//push the fame
			scriptIn.stackFrames.push(frame);
			scriptIn.lastFuncs.push(funcIn);
			scriptIn.openFuncScopes++;
			
			//System.out.println();
			//frame.display();
			//System.out.println();
			
			StorageBoxHolder<Integer, EArrayList<String>> funcLines = funcIn.getActionLines();
			
			for (int i = 0; i < funcLines.size(); i++) {
				StorageBox<Integer, EArrayList<String>> line = funcLines.get(i);
				
				int lineNum = line.getA();
				EArrayList<String> unparsedLine = line.getB();
				
				scriptIn.getInterpreter().getLineParser().parseLine(unparsedLine, lineNum);
			}
			
			//System.out.println();
			//frame.display();
			//System.out.println();
			
			//get the function's RAX
			ScriptObject rax = scriptIn.stackFrames.peek().getRAX();
			
			//pop the frame
			scriptIn.stackFrames.pop();
			scriptIn.lastFuncs.pop();
			scriptIn.openFuncScopes--;
			
			return rax;
		}
		
		return null;
	}
	
	/** Pairs each given value to a parameter name for a given function in order. If the number of parameters for the given function does not
	 *  match the given number of values, an error is thrown.
	 *  
	 * @param funcIn
	 * @param params
	 * @return StorageBoxHolder<String, Object>
	 * @throws ScriptError
	 */
	public static EArrayList<ScriptObject> boxParams(EScript scriptIn, ScriptFunction funcIn, EArrayList<ScriptObject> params) throws ScriptError {
		EArrayList<ScriptObject> boxed = new EArrayList();
		
		if (funcIn.getParamNames().size() == params.size()) {
			
			for (int i = 0; i < params.size(); i++) {
				EDataType type = funcIn.getParamTypes().get(i);
				String name = funcIn.getParamNames().get(i);
				ScriptObject value = params.get(i);
				
				ScriptObject obj = scriptIn.getVar(name);
				
				if (obj != null) { boxed.add(obj); }
				else { boxed.add(value.setName(funcIn.getParamNames().get(i))); }
			}
		}
		else { throw new ScriptError("The expected number of function parameters (" + funcIn.getParams().size() + ") does not match the given number of values (" + params.size() + ") ! "); }
		
		return boxed;
	}
	
	/** Attempts to return the correct associated value for the given value variable name. This checks across the current script's function and global scopes.
	 *  If the value can not be found, or if the value has not been initialized, or it simply hasn't been declared within this scope, a script error will be thrown. */
	public static StorageBox<EDataType, Object> getValue(String valueIn, EScript scriptIn, ScriptFunction funcIn, StorageBoxHolder<String, Object> params) throws ScriptError {
		
		//first check to see if the value is a passed function parameter
		if (params.contains(valueIn)) {
			EArrayList<String> names = funcIn.getParamNames();
			EArrayList<EDataType> types = funcIn.getParamTypes();
			int pos = -1;
			
			for (int i = 0; i < names.size(); i++) {
				if (names.get(i).equals(valueIn)) { pos = i; break; }
			}
			
			if (pos > 0) {
				EDataType type = types.get(pos);
				Object val = params.getBoxWithA(valueIn);
				return new StorageBox(type, val);
			}
			else { throw new NullVariableError(valueIn, scriptIn.getCurrentLine()); }
		}
		
		//otherwise check to see if it is a declared variable within scope
		ScriptObject obj = scriptIn.getVar(valueIn);
		
		//if the object is a variable, return the variable's type and value
		if (obj instanceof ScriptVariable) {
			ScriptVariable var = (ScriptVariable) obj;
			return new StorageBox(var.getDataType(), var.get());
		}
		//otherwise, return the object's hash string
		else if (obj != null) {
			return new StorageBox(EDataType.STRING, obj.getObjectHash());
		}
		
		//if no variable can be paried against the given name, throw a null variable error
		throw new NullVariableError(valueIn, scriptIn.getCurrentLine());
	}

}
