package gameSystems.scriptingSystem.util;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.DuplicateVariableError;
import gameSystems.scriptingSystem.exceptions.errors.NullVariableError;
import gameSystems.scriptingSystem.variables.ScriptObject;
import util.storageUtil.EArrayList;

public class StackFrame {

	private final int frameStartLine;
	private EArrayList<ScriptObject> frameVars = new EArrayList();
	private ScriptObject rax = null;
	
	//-----------------------
	//StackFrame Constructors
	//-----------------------
	
	public StackFrame(int lineStart) {
		frameStartLine = lineStart;
	}
	
	//------------------
	//StackFrame Methods
	//------------------
	
	/** Attempts to store each given value on this frame. */
	public StackFrame storeEach(EArrayList<ScriptObject> in) throws ScriptError {
		for (ScriptObject o : in) { store(o); }
		return this;
	}
	
	/** Attempts to store each given value on this frame. */
	public StackFrame storeEach(ScriptObject... in) throws ScriptError {
		for (ScriptObject o : in) { store(o); }
		return this;
	}
	
	/** Attempts to store a value on this frame. */
	public StackFrame store(ScriptObject in) throws ScriptError {
		if (in == null) { throw new NullVariableError(); }
		
		for (ScriptObject o : frameVars) {
			if (o.equals(in)) { throw new DuplicateVariableError(in.getName()); }
		}
		
		frameVars.add(in);
		return this;
	}
	
	/** Attempts to retrieve a stored value on this frame. */
	public ScriptObject load(String nameIn) {
		for (ScriptObject o : frameVars) {
			if (o.nameEquals(nameIn)) { return o; }
		}
		
		return null;
	}
	
	/** Prints out the contents of this stack frame. */
	public void display() {
		System.out.println("Frame: " + frameStartLine);
		
		for (ScriptObject o : frameVars) {
			System.out.println("|- " + o.getName() + " " + o.getObjectHash());
		}
		
		System.out.println("|- RAX: " + rax);
	}
	
	//------------------
	//StackFrame Getters
	//------------------
	
	public ScriptObject getRAX() { return rax; }
	public int getFrameStart() { return frameStartLine; }
	
	//------------------
	//StackFrame Setters
	//------------------
	
	public StackFrame setRAX(ScriptObject in) { rax = in; return this; }
	
}
