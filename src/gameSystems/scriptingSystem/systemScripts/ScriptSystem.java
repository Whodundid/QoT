package gameSystems.scriptingSystem.systemScripts;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ArgumentParser;
import gameSystems.scriptingSystem.interpreter.langUtil.PrintFunctions;
import gameSystems.scriptingSystem.variables.ScriptObject;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class ScriptSystem extends ScriptObject {

	EScript script;
	
	public ScriptSystem(EScript scriptIn) {
		super(EDataType.OBJECT, "System");
		script = scriptIn;
		setFinal(true);
		setStatic(true);
	}
	
	@Override
	public StorageBox<Object, EDataType> handleFunction(String in, EArrayList<String> args) throws ScriptError {
		switch (in) {
		case "print": print(args); return new StorageBox(null, EDataType.VOID);
		case "println": println(args); return new StorageBox(null, EDataType.VOID);
		case "timeMillis": return new StorageBox(millis(), EDataType.LONG);
		case "timeNanos": return new StorageBox(nano(), EDataType.LONG);
		default:
			return super.handleFunction(in, args);
		}
	}
	
	public void print(EArrayList<String> args) throws ScriptError {
		PrintFunctions.handlePrint(script, ArgumentParser.isolateArgs(args), false, script.getCurrentLine());
	}
	
	public void println(EArrayList<String> args) throws ScriptError {
		PrintFunctions.handlePrint(script, ArgumentParser.isolateArgs(args), true, script.getCurrentLine());
	}
	
	public long millis() {
		return System.currentTimeMillis();
	}
	
	public long nano() {
		return System.nanoTime();
	}
	
}
