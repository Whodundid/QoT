package engine.scripting.builder;

public abstract class ScriptAction {
	
	public ScriptAction() {}
	
	public abstract void runAction(Object... args) throws Exception;
	
}
