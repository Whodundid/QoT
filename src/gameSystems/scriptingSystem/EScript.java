package gameSystems.scriptingSystem;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.ScriptNameError;
import gameSystems.scriptingSystem.interpreter.ScriptInterpreter;
import gameSystems.scriptingSystem.systemScripts.ScriptSystem;
import gameSystems.scriptingSystem.util.ScriptRunner;
import gameSystems.scriptingSystem.util.StackFrame;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.util.enums.ScopeType;
import gameSystems.scriptingSystem.variables.ScriptFunction;
import gameSystems.scriptingSystem.variables.ScriptObject;
import java.util.Stack;
import util.EUtil;
import util.storageUtil.EArrayList;

public class EScript {
	
	/** The object manging how this script runs. */
	protected ScriptRunner runnerObject;
	
	protected String name;
	protected EArrayList<EScript> imports = new EArrayList();
	protected EArrayList<ScriptObject> staticVariables = new EArrayList();
	protected EArrayList<ScriptFunction> functions = new EArrayList();
	
	protected boolean dying = false;
	protected int funcDepth = 0;
	protected ScriptFunction curFunc = null;
	protected final ScriptSystem systemObject = new ScriptSystem(this);
	
	public Stack<ScriptFunction> lastFuncs = new Stack();
	public Stack<ScopeType> lastScopes = new Stack();
	public Stack<Integer> lastFuncLines = new Stack();
	public Stack<Integer> lastScriptLines = new Stack();
	public Stack<Integer> lastScopeLines = new Stack();
	public Stack<Integer> lastLoopLines = new Stack();
	
	public Stack<StackFrame> stackFrames = new Stack();
	
	public int openScriptScopes = 0;
	public int openFuncScopes = 0;
	public int openLoopScopes = 0;
	public int openScopes = 0;
	
	//--------------------
	//EScript Constructors
	//--------------------
	
	public EScript(String nameIn) throws ScriptError {
		name = nameIn;
		addStaticVariable(systemObject);
	}
	
	//----------------
	//EScript Builders
	//----------------
	
	/** Adds a script to which this script will reference. */
	public EScript addImport(EScript importIn) throws ScriptError {
		//add if there isn't already an import under the same name, otherwise, throw a duplicate errror
		if (!imports.addIf(!containsImport(importIn), importIn)) {
			throw new ScriptError("Duplicate import '" + importIn.getName() + "'!");
		}
		return this;
	}
	
	/** Adds a variable that all functions and imports will have access to. */
	public EScript addStaticVariable(ScriptObject varIn) throws ScriptError {
		//add if there isn't already a variable under the same name, otherwise, throw a duplicate errror
		if (!staticVariables.addIf(!containsGlobal(varIn), varIn)) {
			throw new ScriptError("Duplicate static variable '" + varIn.getName() + "'!");
		}
		return this;
	}
	
	/** Adds a function to this script. Functions can be nested. */
	public EScript addFunction(ScriptFunction funcIn) throws ScriptError { return addFunction(funcIn, false); }
	/** Adds a function to this script at the current function depth. */
	public EScript addFunction(ScriptFunction funcIn, boolean atDepth) throws ScriptError {
		if (atDepth) {
			EUtil.nullDo(curFunc, f -> f.addFunction(funcIn));
		}
		else {
			//add if there isn't already a function under the same name, otherwise, throw a duplicate errror
			if (!functions.addIf(!containsFunction(funcIn), funcIn)) {
				throw new ScriptError("Duplicate function '" + funcIn.getName() + "'!");
			}
		}

		return this;
	}
	
	//---------------
	//EScript Methods
	//---------------
	
	/** Returns the name of this script. */
	public String getName() { return name; }
	
	/** Returns true if the current scope is within a function. */
	public boolean isInFunction() { return funcDepth > 0; }
	
	/** Returns true if the current scope is not within a function. */
	public boolean isInMain() { return funcDepth == 0; }
	
	//** Frees the memory that this script's resources occupy. */
	public EScript free() {
		imports.clear();
		staticVariables.clear();
		
		ScriptFunction f = (functions.isNotEmpty()) ? functions.getFirst() : null;
		
		while (f != null) {
			f.getLocalVariables().clear();
			f.getLocalFunctions().clear();
			
			f = f.getParent();
		}
		
		return this;
	}
	
	/** Nests a function within the current scope and sets the current function to function specified. */
	public void pushFunc(ScriptFunction funcIn) throws ScriptError {
		if (funcIn != null) {
			
			//add function to top level
			if (funcDepth == 0) { addFunction(funcIn); }
			//otherwise add it as a nested function to the current function scope
			else if (curFunc != null) { curFunc.setParent(funcIn); }
			
			curFunc = funcIn;
			funcDepth++;
		}
	}
	
	/** Effectively sets the current function to the previous depth.
	 * 	If the depth is already 0, no action is performed.
	 */
	public void popFunc() {
		if (funcDepth > 0) {
			curFunc = curFunc.getParent();
			funcDepth--;
		}
	}
	
	/** Sets this script to be removed on the next pass. */
	public void killScript() {
		dying = true;
	}
	
	//---------------
	//EScript Getters
	//---------------
	
	/** Returns the scripts that this script references. */
	public EArrayList<EScript> getImports() { return imports; }
	
	/** Returns the global variables of this script. */
	public EArrayList<ScriptObject> getStaticVariables() { return staticVariables; }
	
	/** Returns the functions of this script. */
	public EArrayList<ScriptFunction> getFunctions() { return functions; }
	
	public ScriptObject getVar(String name) {
		ScriptObject obj = getStaticVar(name);
		if (obj == null) { obj = stackFrames.peek().load(name); }
		return obj;
	}
	
	public ScriptObject getStaticVar(String name) {
		for (ScriptObject v : staticVariables) {
			if (v.getName().equals(name)) { return v; }
		}
		return null;
	}
	
	/** Returns a top level function name.
	 * 
	 *  Note: this does not retrieve nested functions.
	 *  Nested functions can only be found recursively from a starting function.
	 */
	public ScriptFunction getFunction(String name) {
		for (ScriptFunction f : functions) {
			if (f.getName().equals(name)) { return f; }
		}
		return null;
	}
	
	/** Returns the depth number indicating how many nested functions deep the current scope is.
	 * 	If the depth is 0, the current scope is not in a function but rather the global script itself.
	 * 	If the depth is >= 1, the current scope is within an existing function.
	 *  @return int (depth)
	 */
	public int getFuncDepth() { return funcDepth; }
	
	/** Returns the current function. If the current function depth scope is 0, null is returned instead. */
	public ScriptFunction getCurFun() { return curFunc; }
	
	/** Returns true if this script is in the process of stopping. */
	public boolean isDying() { return dying; }
	
	/** Returns the runner object managing this script's execution. */
	public ScriptRunner getRunner() { return runnerObject; }
	
	/** Returns the interpreter object which is managing line parsing for this script. */
	public ScriptInterpreter getInterpreter() { return (runnerObject != null) ? runnerObject.getInterpreter() : null; }
	
	/** Returns the current line being parsed on this script. */
	public int getCurrentLine() { return getInterpreter().getLineParser().getLineNumber(); }
	
	public ScriptSystem getSystem() { return systemObject; }
	
	//---------------
	//EScript Setters
	//---------------
	
	public EScript setName(String nameIn) throws ScriptError {
		if (Keyword.isAllowedName(nameIn)) { name = nameIn; }
		else { throw new ScriptNameError(nameIn, getInterpreter().getLineParser().getLineNumber()); }
		return this;
	}
	
	/** Sets the object which manages this script's execution. */
	public EScript setRunner(ScriptRunner in) { runnerObject = in; return this; }
	
	public EScript setCurFunc(ScriptFunction in) { curFunc = in; return this; }
	
	//-------------------------------
	//EScript Internal Builder Checks
	//-------------------------------
	
	/** Checks if the specified import already exists and that it is not null. */
	private boolean containsImport(EScript scriptIn) throws ScriptError {
		if (scriptIn != null) {
			return EUtil.forEachTest(imports, s -> s.getName().equals(scriptIn.getName()), true, false);
		}
		throw new ScriptError("Script import is null!");
	}
	
	/** Checks if the specified variable already exists and that it is not null. */
	private boolean containsGlobal(ScriptObject varIn) throws ScriptError {
		if (varIn != null) {
			return EUtil.forEachTest(staticVariables, s -> s.getName().equals(varIn.getName()), true, false);
		}
		throw new ScriptError("Global variable is null!");
	}
	
	/** Checks if the specified function already exists and that it is not null. */
	private boolean containsFunction(ScriptFunction funcIn) throws ScriptError {
		if (funcIn != null) {
			return EUtil.forEachTest(functions, s -> s.getName().equals(funcIn.getName()), true, false);
		}
		throw new ScriptError("Function is null!");
	}
	
}
