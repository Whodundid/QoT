package engine.scriptingEngine.scriptBuilder;

public abstract class ScriptAction {
	
	public ScriptAction() {}
	
	public abstract void runAction(Object... args) throws Exception;
	
}
