package gameSystems.scriptingSystem.variables.dataTypes.numbers;

import util.miscUtil.EDataType;

/** A script variable representing a number with a decimal point. */
public class ScriptDouble extends ScriptNumber {

	public ScriptDouble() { this(0.0); }
	public ScriptDouble(double valIn) { super(EDataType.DOUBLE, valIn); }
	public ScriptDouble(String nameIn) { this(nameIn, 0.0); }
	public ScriptDouble(String nameIn, double valIn) {
		super(nameIn, EDataType.DOUBLE, valIn);
	}
	
	public static ScriptDouble ZERO() { return new ScriptDouble(); }
	public static ScriptDouble of(double val) { return new ScriptDouble(val); }
	public static ScriptDouble of(String val) { return new ScriptDouble(Double.parseDouble(val)); }
	
}
