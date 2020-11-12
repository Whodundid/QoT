package util.miscUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/** An enum to keep track of java datatypes. Mainly used for generics at runtime. */
public enum EDataType {
	VOID,
	OBJECT,
	BOOL,
	CHAR,
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	STRING,
	METHOD,
	ARRAY,
	ENUM,
	NULL;
	
	/** Returns true if the given dataType is a number. */
	public boolean isNumber() {
		switch (this) {
		case CHAR: case BYTE: case SHORT: case INT: case LONG: case FLOAT: case DOUBLE: return true;
		default: return false;
		}
	}
	
	/** Returns true if the given dataType is a number. */
	public static boolean isNumber(EDataType typeIn) { return (typeIn != null) ? typeIn.isNumber() : false; }
	
	//SHOULD BE MOVED TO NUMBER UTIL
	/** Returns true if the given String can be cast as a number. */
	public static boolean isNumber(String in) { return parseNumber(in) != null; }
	
	//SHOULD BE MOVED TO NUMBER UTIL
	public static Number parseNumber(String in) {
		if (in != null) {
			boolean decimal = false;
			
			for (int i = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				
				if (c == '.') {
					//if there is more than one decimal, it's not a number
					if (decimal) { return null; }
					decimal = true;
				}
				else if (!Character.isDigit(c)) { return null; }
			}
			
			try {
				return Double.valueOf(in);
			}
			catch (NumberFormatException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	/** Returns the dataType of the given Number. If the input is null, null is returned instead. */
	public static EDataType getNumberType(Number in) {
		if (in instanceof Byte) { return BYTE; }
		if (in instanceof Short) { return SHORT; }
		if (in instanceof Integer) { return INT; }
		if (in instanceof Long) { return LONG; }
		if (in instanceof Float) { return FLOAT; }
		if (in instanceof Double) { return DOUBLE; }
		return NULL;
	}
	
	public static EDataType getDataType(Object in) {
		if (in == null) { return NULL; }
		if (in instanceof Boolean) { return BOOL; }
		if (in instanceof Character) { return CHAR; }
		if (in instanceof Byte) { return BYTE; }
		if (in instanceof Short) { return SHORT; }
		if (in instanceof Integer) { return INT; }
		if (in instanceof Long) { return LONG; }
		if (in instanceof Float) { return FLOAT; }
		if (in instanceof Double) { return DOUBLE; }
		if (in instanceof String) { return STRING; }
		if (in instanceof Method) { return METHOD; }
		if (in instanceof Array) { return ARRAY; }
		if (in instanceof Enum) { return ENUM; }
		return OBJECT;
	}
	
	/** Returns the dataType of the given String.
	 *  If the input is found to not be a number, null is returned instead.
	 *  If the input is null, null is returned instead. */
	public static EDataType getNumberType(String in) {
		return getNumberType(parseNumber(in));
	}
	
	public static Object castTo(Number in, EDataType typeOut) {
		if (in != null) {
			if (typeOut.isNumber()) {
				switch (typeOut) {
				case BYTE: return in.byteValue();
				case SHORT: return in.shortValue();
				case INT: return in.intValue();
				case LONG: return in.longValue();
				case FLOAT: return in.floatValue();
				case DOUBLE: return in.doubleValue();
				default: break;
				}
			}
		}
		return null;
	}
	
	public static Object castTo(Object in, EDataType typeOut) {
		if (in != null) {
			if (in instanceof String) { return castTo(parseNumber((String) in), typeOut); }
			if (in instanceof Number) { return castTo((Number) in, typeOut); }
		}
		return null;
	}
	
}
