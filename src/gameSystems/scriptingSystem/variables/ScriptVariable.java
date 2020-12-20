package gameSystems.scriptingSystem.variables;

import util.miscUtil.EDataType;

/** Generic super class for script objects. */
public class ScriptVariable extends ScriptObject {

	protected boolean isFinal = false;
	protected Object value;
	
	//---------------------------
	//ScriptVariable Constructors
	//---------------------------
	
	public ScriptVariable(EDataType typeIn, Object valIn) {
		super(typeIn);
		value = valIn;
	}
	
	public ScriptVariable(String nameIn, EDataType typeIn, Object valIn) {
		super(typeIn, nameIn);
		value = valIn;
	}
	
	@Override public String toString() { return String.valueOf("[" + type + " : " + get() + "]"); }
	
	//----------------------
	//ScriptVariable Getters
	//----------------------
	
	public Object get() { return value; }
	public boolean isFinal() { return isFinal; }
	
	//----------------------
	//ScriptVariable Setters
	//----------------------
	
	public ScriptVariable set(Object valIn) { value = valIn; return this; }
	public ScriptVariable setFinal(boolean val) { isFinal = val; return this; }
	
}
