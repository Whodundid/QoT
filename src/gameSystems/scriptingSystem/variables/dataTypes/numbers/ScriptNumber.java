package gameSystems.scriptingSystem.variables.dataTypes.numbers;

import gameSystems.scriptingSystem.variables.ScriptVariable;
import util.miscUtil.EDataType;

public abstract class ScriptNumber extends ScriptVariable {

	protected ScriptNumber(EDataType typeIn, Object valIn) {
		super(typeIn, valIn);
	}
	
	protected ScriptNumber(String nameIn, EDataType typeIn, Object valIn) {
		super(nameIn, typeIn, valIn);
	}
	
	public int intVal() { return (int) get(); }
	public double doubleVal() { return (double) get(); }
	
	public static ScriptNumber of(Object in) {
		if (in instanceof String) {
			EDataType type = EDataType.getNumberType((String) in);
			switch (type) {
			case BYTE:
			case SHORT:
			case INT:
			case LONG: return ScriptInt.of((String) in);
			case FLOAT:
			case DOUBLE: return ScriptDouble.of((String) in);
			default: break;
			}
		}
		
		if (in instanceof Number) {
			return of((Number) in);
		}
		
		throw new RuntimeException("Cannot create number from given type: " + in);
	}
	
	public static ScriptNumber of(Number in) {
		if (in instanceof Byte) { return new ScriptInt(in.longValue()); }
		if (in instanceof Short) { return new ScriptInt(in.longValue()); }
		if (in instanceof Integer) { return new ScriptInt(in.longValue()); }
		if (in instanceof Long) { return new ScriptInt(in.longValue()); }
		if (in instanceof Float) { return new ScriptDouble(in.longValue()); }
		if (in instanceof Double) { return new ScriptDouble(in.longValue()); }
		throw new RuntimeException("Failed to create number! This shouldn't be possible! " + in);
	}
	
}
