package gameSystems.scriptingSystem.variables.dataTypes.numbers;

import util.miscUtil.EDataType;

/** A script variable representing a number without a decimal place. Encoded with Java Longs. */
public class ScriptInt extends ScriptNumber {

	public ScriptInt() { this(0); }
	public ScriptInt(long valIn) { super(EDataType.LONG, valIn); }
	public ScriptInt(String nameIn) { this(nameIn, 0); }
	public ScriptInt(String nameIn, long valIn) {
		super(nameIn, EDataType.LONG, valIn);
	}
	
	public static ScriptInt ZERO() { return new ScriptInt(); }
	public static ScriptInt of(int val) { return new ScriptInt(val); }
	public static ScriptInt of(String val) { return new ScriptInt(Long.parseLong(val)); }
	
}
