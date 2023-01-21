package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder;

public abstract class ScriptAction {
	
	public ScriptAction() {}
	
	public abstract void runAction(Object... args) throws Exception;
	
}
