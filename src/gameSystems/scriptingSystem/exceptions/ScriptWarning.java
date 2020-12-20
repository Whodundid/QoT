package gameSystems.scriptingSystem.exceptions;

/** A ScriptWarning is an problem that does not halt script execution and is reported to the user. */
public class ScriptWarning extends Exception {

	public ScriptWarning(String message) {
		super(message);
	}
	
	public ScriptWarning(String message, int lineNum) {
		super(message + " Line: " + lineNum);
	}
	
}
