package gameSystems.scriptingSystem.variables.dataTypes;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;

public class ScriptBoolean extends ScriptVariable {

	public ScriptBoolean() { this(false); }
	public ScriptBoolean(boolean val) { super(EDataType.BOOLEAN, val); }
	public ScriptBoolean(String nameIn) { this(nameIn, false); }
	public ScriptBoolean(String nameIn, boolean val) {
		super(nameIn, EDataType.BOOLEAN, val);
	}
	
	@Override
	public StorageBox<Object, EDataType> handleFunction(String in, EArrayList<String> args) throws ScriptError {
		switch (in) {
		case "valueOf": return new StorageBox(Boolean.valueOf(args.get(0)), EDataType.BOOLEAN);
		case "get": return new StorageBox(get(), EDataType.BOOLEAN);
		case "set": set(args.get(0)); return new StorageBox(this, EDataType.THIS);
		case "getAndSet": boolean val = (boolean) get(); set(args.get(0)); return new StorageBox(val, EDataType.BOOLEAN);
		default:
			return super.handleFunction(in, args);
		}
		
	}
	
	public static ScriptBoolean TRUE() { return new ScriptBoolean(true); }
	public static ScriptBoolean FALSE() { return new ScriptBoolean(false); }
	public static ScriptBoolean of(boolean val) { return new ScriptBoolean(val); }
	public static ScriptBoolean of(String val) { return new ScriptBoolean(Boolean.parseBoolean(val)); }
	
}
