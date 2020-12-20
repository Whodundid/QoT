package gameSystems.scriptingSystem.variables;

import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBoxHolder;

public class ScriptFunction extends ScriptObject {

	private StorageBoxHolder<Integer, EArrayList<String>> actions = new StorageBoxHolder();
	private EArrayList<ScriptVariable> variables = new EArrayList();
	private EArrayList<ScriptFunction> functions = new EArrayList();
	private ScriptFunction parent;
	private final StorageBoxHolder<EDataType, String> params = new StorageBoxHolder();
	private final EDataType returnType;
	private final int funcDepth;
	private int lineReturnPointer = -1;
	
	public ScriptFunction(ScriptFunction parentFunction, EDataType returnTypeIn, String nameIn, int funcDepthIn) {
		this(parentFunction, returnTypeIn, nameIn, new StorageBoxHolder(), funcDepthIn);
	}
	
	public ScriptFunction(ScriptFunction parentFunction, EDataType returnTypeIn, String nameIn, StorageBoxHolder<EDataType, String> paramsIn, int funcDepthIn) {
		super(EDataType.METHOD, nameIn);
		parent = parentFunction;
		returnType = returnTypeIn;
		setParams(paramsIn);
		funcDepth = funcDepthIn;
	}
	
	//Methods
	
	public ScriptFunction addActionLine(int lineNum, EArrayList<String> lineIn) { actions.add(lineNum, lineIn); return this; }
	public ScriptFunction addVariable(ScriptVariable varIn) { variables.addIfNotNull(varIn); return this; }
	public ScriptFunction addFunction(ScriptFunction varIn) { functions.addIfNotNull(varIn); return this; }
	
	//Getters
	
	/** Retrives all of the ScriptVariables which exist within this function's scope. */
	public EArrayList<ScriptVariable> getScopeVariables() {
		EArrayList<ScriptVariable> vars = new EArrayList();
		
		//collect all variables from each parent function
		ScriptFunction cur = this;
		while (cur != null) {
			vars.addAll(variables);
			cur = cur.parent;
		}
		
		return vars;
	}
	
	public StorageBoxHolder<Integer, EArrayList<String>> getActionLines() { return actions; }
	public EArrayList<ScriptVariable> getLocalVariables() { return variables; }
	public EArrayList<ScriptFunction> getLocalFunctions() { return functions; }
	
	public EDataType getReturnType() { return returnType; }
	public StorageBoxHolder<EDataType, String> getParams() { return params; }
	public EArrayList<EDataType> getParamTypes() { return params.getAVals(); }
	public EArrayList<String> getParamNames() { return params.getBVals(); }
	public ScriptFunction getParent() { return parent; }
	public int getDepth() { return funcDepth; }
	public int getLineReturnPointer() { return lineReturnPointer; }
	
	//Setters
	
	public ScriptFunction setParent(ScriptFunction in) { parent = in; return this; }
	public ScriptFunction setLineReturnPointer(int in) { lineReturnPointer = in; return this; }
	
	public ScriptFunction setParams(StorageBoxHolder<EDataType, String> paramsIn) { params.clear(); params.addAll(paramsIn); return this; }
	
}
