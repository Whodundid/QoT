package gameSystems.scriptingSystem.gameScripting;

import storageUtil.EArrayList;

public abstract class ScriptAction {
	
	protected String action;
	protected EArrayList<Object> args;
	
	public ScriptAction(String actionIn, Object... argsIn) {
		action = actionIn;
		args = new EArrayList().add(argsIn);
	}
	
	public void runAction() throws Exception { runAction(args.toArray()); }
	public abstract void runAction(Object... args) throws Exception;
	
	public ScriptAction setAction(String in) { action = in; return this; }
	public ScriptAction setArgs(Object... in) { args.clear(); args.add(in); return this; }
	
	public String getAction() { return action; }
	public EArrayList<Object> getArgs() { return args; }
	
}
