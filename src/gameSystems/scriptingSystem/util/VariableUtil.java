package gameSystems.scriptingSystem.util;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.exceptions.errors.VariableNameError;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.variables.ScriptVariable;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptBoolean;
import gameSystems.scriptingSystem.variables.dataTypes.ScriptString;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptDouble;
import gameSystems.scriptingSystem.variables.dataTypes.numbers.ScriptInt;
import util.mathUtil.NumUtil;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class VariableUtil {
	
	public static ScriptVariable createVar(EDataType typeIn, Object val) throws ScriptError {
		switch (typeIn) {
		case BYTE:
		case SHORT:
		case INT:
		case LONG: return new ScriptInt(Long.parseLong(String.valueOf(val)));
		case FLOAT:
		case DOUBLE: return new ScriptDouble(Double.parseDouble(String.valueOf(val)));
		case STRING: return new ScriptString(String.valueOf(val));
		case BOOLEAN: return new ScriptBoolean(Boolean.parseBoolean(String.valueOf(val)));
		default: throw new ScriptError("Invalid variable type!");
		}
	}
	
	public static ScriptVariable createVar(String name, EDataType typeIn, Object val) throws ScriptError {
		
		//check for null names or not allowed names
		if (name == null || !Keyword.isAllowedName(name)) { throw new VariableNameError(name); }
		
		switch (typeIn) {
		case BYTE:
		case SHORT:
		case INT:
		case LONG: return new ScriptInt(name, Long.parseLong(String.valueOf(val)));
		case FLOAT:
		case DOUBLE: return new ScriptDouble(name, Double.parseDouble(String.valueOf(val)));
		case STRING: return new ScriptString(name, String.valueOf(val));
		case BOOLEAN: return new ScriptBoolean(name, Boolean.parseBoolean(String.valueOf(val)));
		default: throw new ScriptError("Invalid variable type!");
		}
	}
	
	public static boolean typesMatch(ScriptVariable varIn, EDataType typeIn) {
		return (varIn != null) ? varIn.getDataType() == typeIn : false;
	}
	
	public static ScriptVariable cast(ScriptVariable varIn, EDataType typeIn, int lineNum) throws ScriptError {
		if (varIn != null) {
			try {
				switch (varIn.getDataType()) {
				case BOOLEAN:
					switch (typeIn) {
					case BOOLEAN: return new ScriptBoolean((boolean) varIn.get());
					case INT:
					case LONG: return new ScriptInt(((boolean) varIn.get()) ? 1 : 0);
					case DOUBLE: return new ScriptDouble(((boolean) varIn.get()) ? 1.0 : 0.0);
					case STRING: return new ScriptString(String.valueOf(varIn.get()));
					default: throw new ScriptError("Invalid cast type: " + typeIn + "!", lineNum);
					}
				case INT:
					switch (typeIn) {
					case BOOLEAN: return new ScriptBoolean((Math.signum(NumUtil.clamp((int) varIn.get(), 0, Integer.MAX_VALUE)) == 1) ? true : false);
					case INT:
					case LONG: return new ScriptInt((long) varIn.get());
					case DOUBLE: return new ScriptDouble((double) varIn.get());
					case STRING: return new ScriptString(String.valueOf(varIn.get()));
					default: throw new ScriptError("Invalid cast type: " + typeIn + "!", lineNum);
					}
				case DOUBLE:
					switch (typeIn) {
					case BOOLEAN: return new ScriptBoolean((Math.signum(NumUtil.clamp((double) varIn.get(), 0, Double.MAX_VALUE)) == 1) ? true : false);
					case INT:
					case LONG: return new ScriptInt((long) varIn.get());
					case DOUBLE: return new ScriptDouble((double) varIn.get());
					case STRING: return new ScriptString(String.valueOf(varIn.get()));
					default: throw new ScriptError("Invalid cast type: " + typeIn + "!", lineNum);
					}
				case STRING:
					switch (typeIn) {
					case BOOLEAN: return new ScriptBoolean(((String) varIn.get()).isEmpty() ? false : true);
					case INT:
					case LONG: return new ScriptInt(((String) varIn.get()).isEmpty() ? 0 : 1);
					case DOUBLE: return new ScriptDouble(((String) varIn.get()).isEmpty() ? 0.0 : 1.0);
					case STRING: return new ScriptString(String.valueOf(varIn.get()));
					default: throw new ScriptError("Invalid cast type: " + typeIn + "!", lineNum);
					}
				default: throw new ScriptError("Cannot cast the variable: " + varIn.getName() + " to " + typeIn + "!", lineNum);
				}
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				throw new ScriptError("CAST ERROR: Cannot cast the variable: " + varIn.getName() + " to " + typeIn + "!", lineNum);
			}
		}
		return varIn;
	}
	
	public static boolean isString(String in) {
		if (in != null && in.contains("\"")) {
			return in.chars().filter(c -> c == '"').count() % 2 == 0;
		}
		return false;
	}
	
	public static boolean isNumber(String in) {
		if (in != null && !in.isEmpty()) {
			return EDataType.isNumber(in);
		}
		return false;
	}
	
	public static boolean isBoolean(String in) {
		if (in != null && !in.isEmpty()) {
			in = in.toLowerCase();
			return in.equals("false") || in.equals("true");
		}
		return false;
	}
	
	/** Used to isolate the base object datatype when the given object itself has parameters. */
	public static EDataType findBaseType(EArrayList<String> tokensIn) {
		if (tokensIn.isNotEmpty()) {
			
			//if there is only one token, try to parse the token as a datatype
			if (tokensIn.hasOnlyOne()) { return Keyword.getDataType(tokensIn.getFirst()); }
			
			//if there are only two args, this really shouldn't be a datatype
			if (tokensIn.size() == 2) { return EDataType.NULL; }
			
			int numTypes = 0;
			int argStart = -1;
			boolean hasTypes = false;
			
			for (int i = 0; i < tokensIn.size(); i++) {
				String s = tokensIn.get(i);
				
				if ("<".equals(s)) {
					hasTypes = true;
					if (numTypes++ == 0) { argStart = i; }
				}
				else if (">".equals(s)) {
					hasTypes = true;
					if (--numTypes == 0) {
						return Keyword.getDataType(tokensIn.get(argStart - 1));
					}
				}
			}
			
			if (!hasTypes) {
				//attempt to interpret the first token as a keyword
				return Keyword.getDataType(tokensIn.get(0));
			}
			else {
				//used to indicate that this is an error
				return EDataType.VOID;
			}
		}
		
		return EDataType.NULL;
	}
	
}
