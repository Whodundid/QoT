package gameSystems.scriptingSystem.exceptions;

/** A ScriptError marks a point where script execution cannot continue as some form of logic/syntax error has been detected. */
public class ScriptError extends Exception {
	
	public ScriptError(String message) {
		super("ScriptError: " + message);
	}
	
	public ScriptError(String message, int lineNumIn) {
		super(message + " Line: " + lineNumIn);
	}
	
}
