package gameSystems.scriptingSystem.util.enums;

import util.EUtil;
import util.miscUtil.EDataType;

/** The set of reserved keywords in the Envision Scripting Language. */
public enum Keyword {
	
	//script definition
	SCRIPT("Script"),
	
	//script base
	IMPORT("import"),
	STATIC("static"),
	FINAL("final"),
	THIS("this"),
	NEW("new"),
	
	//comment
	COMMENT("//"),
	COMMENT_START("/*"),
	COMMENT_END("*/"),
	
	//return
	RETURN("return"),
	BREAK("break"),
	CONTINUE("continue"),
	
	//function
	//SYSTEM("System"),
	
	//data types
	BOOLEAN("boolean"),
	INT("int"),
	DOUBLE("double"),
	STRING("string"),
	NUMBER("number"),
	LIST("list"),
	VOID("void"),
	TRUE("true"),
	FALSE("false"),
	NULL("null"),
	ENUM("enum"),
	
	//loops
	FOR("for"),
	DO("do"),
	WHILE("while"),
	
	//logic statements
	IF("if"),
	ELSE("else"),
	SWITCH("switch"),
	CASE("case"),
	DEFAULT("default"),
	TERNARY("?"),
	
	//exceptions
	TRY("try"),
	CATCH("catch"),
	FINALLY("finally"),
	THROW("throw"),
	THROWS("throws"),
	
	//logical operators
	EQUALS("=="),
	NOT_EQUALS("!="),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_THAN_EQUALS("<="),
	GREATER_THAN_EQUALS(">="),
	AND("&&"),
	OR("||"),
	BITWISE_AND("&"),
	BITWISE_OR("|"),
	BITWISE_XOR("^"),
	BITWISE_NOT("~"),
	
	//arithmetic
	ADD("+"),
	SUBTRACT("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	MODULUS("%"),
	
	//arithmetic operators
	ASSIGN("="),
	ADD_ASSIGN("+="),
	SUBTRACT_ASSIGN("-="),
	MULTIPLY_ASSIGN("*="),
	DIVIDE_ASSIGN("/="),
	MODULUS_ASSIGN("%="),
	BITWISE_AND_ASSIGN("&="),
	BITWISE_OR_ASSIGN("|="),
	BITWISE_XOR_ASSIGN("^="),
	SHIFT_LEFT_ASSIGN("<<="),
	SHIFT_RIGHT_ASSIGN(">>="),
	INCREMENT("++"),
	DECREMENT("--"),
	SHIFT_LEFT("<<"),
	SHIFT_RIGHT(">>");
	
	//------------------------------------------
	
	public String keyword;
	
	private Keyword(String keywordIn) {
		keyword = keywordIn;
	}
	
	//------------------------------------------
	
	/** Returns true if this keyword is a script declaration, member, or parameter. */
	public boolean isScriptBase() {
		switch (this) {
		case IMPORT: case STATIC: case FINAL: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is a comment. */
	public boolean isComment() {
		switch (this) {
		case COMMENT: case COMMENT_START: case COMMENT_END: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is a return statement. */
	public boolean isReturn() {
		switch (this) {
		case RETURN: case BREAK: case CONTINUE: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is a datatype. */
	public boolean isDataType() {
		switch (this) {
		case BOOLEAN: case INT: case DOUBLE: case STRING: case NUMBER: case LIST: case VOID: case TRUE: case FALSE: case NULL: case ENUM: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is loop function. */
	public boolean isLoop() {
		switch (this) {
		case FOR: case DO: case WHILE: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is logical statement. */
	public boolean isLogic() {
		switch (this) {
		case IF: case ELSE: case SWITCH: case CASE: case DEFAULT: case TERNARY: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is related to exception handling. */
	public boolean isException() {
		switch (this) {
		case TRY: case CATCH: case FINALLY: case THROW: case THROWS: return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is a logical operator. */
	public boolean isOperator() {
		switch (this) {
		case EQUALS: case NOT_EQUALS:
		case LESS_THAN: case GREATER_THAN: case LESS_THAN_EQUALS: case GREATER_THAN_EQUALS:
		case AND: case OR:
		case BITWISE_AND: case BITWISE_OR: case BITWISE_XOR: case BITWISE_NOT:
			return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is an arithmetic function. */
	public boolean isArithmetic() {
		switch (this) {
		case ADD: case SUBTRACT: case MULTIPLY: case DIVIDE: case MODULUS:
			return true;
		default: return false;
		}
	}
	
	/** Returns true if this keyword is an arithmetic operator. */
	public boolean isArithmeticOperator() {
		switch (this) {
		case ASSIGN:
		case ADD_ASSIGN: case SUBTRACT_ASSIGN: case MULTIPLY_ASSIGN: case DIVIDE_ASSIGN: case MODULUS_ASSIGN:
		case BITWISE_AND_ASSIGN: case BITWISE_OR_ASSIGN: case BITWISE_XOR_ASSIGN:
		case SHIFT_LEFT_ASSIGN: case SHIFT_RIGHT_ASSIGN:
		case INCREMENT: case DECREMENT:
		case SHIFT_LEFT: case SHIFT_RIGHT:
			return true;
		default: return false;
		}
	}
	
	/** Returns the associated EDataType type of this keyword. If this keyword is not a datatype, EDataType.NULL is returned instead. */
	public EDataType getDataType() {
		switch (this) {
		case BOOLEAN: return EDataType.BOOLEAN;
		case INT: return EDataType.INT;
		case DOUBLE: return EDataType.DOUBLE;
		case STRING: return EDataType.STRING;
		case LIST: return EDataType.ARRAY;
		case ENUM: return EDataType.ENUM;
		case VOID: return EDataType.VOID;
		default: return EDataType.NULL;
		}
	}
	
	//------------------------------------------
	
	/** Returns a keyword from the given input String. If no keywords match, null is returned instead. */
	public static Keyword getKeyword(String in) {
		if (in != null) {
			for (Keyword k : values()) {
				if (k.keyword.equals(in)) { return k; }
			}
		}
		return null;
	}
	
	//------------------------------------------
	
	/** Returns true if the given string is a keyword. */
	public static boolean isKeyword(String in) {
		if (in != null) {
			for (Keyword k : values()) {
				if (k.keyword.equals(in)) { return true; }
			}
		}
		return false;
	}
	
	/** Returns true if the given char is the first letter to the start of a keyword. */
	public static boolean isKeywordStart(char c) {
		for (Keyword k : values()) {
			if (k.keyword.charAt(0) == c) { return true; }
		}
		return false;
	}
	
	/** Returns true if the current 'soFar' String + next is still part of a keyword. */
	public static boolean isStillKeyword(String soFar, char next) {
		if (soFar != null) {
			for (Keyword k : values()) {
				String word = k.keyword;
				if (word.startsWith(soFar) && (word.length() > soFar.length()) && word.charAt(soFar.length()) == next) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Returns true if the given name is allowed to be a script variable or function name. */
	public static boolean isAllowedName(String nameIn) {
		if (nameIn != null) {
			//don't allow names to start with numbers
			if (Character.isDigit(nameIn.charAt(0))) { return false; }
			
			//StringUtils.containsAny(cs, searchChars)
			String regex = ".*[=!?^&|~<>;:\\[\\]+\\-*/%(){}#@.,$\\\"\\'\\\\].*";
			
			//don't allow special characters in names
			if (nameIn.matches(regex)) { return false; }
			
			//don't allow any names that are the same as a keyword
			for (Keyword k : values()) {
				if (k.keyword.equals(nameIn)) { return false; }
			}
			
			return true;
		}
		return false;
	}
	
	/** Checks if the next keyword can logically follow the previous. Under most circumstances this will return false. */
	public static boolean isNextKeywordAllowed(Keyword current, Keyword next) {
		if (current != null) {
			if (next == null) { return true; }
			if (current == STATIC) { return next == FINAL || isDataType(next); }
			if (current.isArithmeticOperator()) { return (next == INCREMENT || next == DECREMENT); }
			if (current == RETURN) { return (next == INCREMENT || next == DECREMENT); }
			if (current == ASSIGN) { return (next == INCREMENT || next == DECREMENT); }
			if (current == EQUALS) { return (next == INCREMENT || next == DECREMENT); }
		}
		return false;
	}
	
	/** Converts datatypes to script equivalents. */
	public static EDataType getScriptDataType(EDataType in) {
		switch (in) {
		case CHAR:
		case BYTE:
		case SHORT:
		case INT:
		case LONG: return EDataType.LONG;
		case FLOAT:
		case DOUBLE: return EDataType.DOUBLE;
		default: return in;
		}
	}
	
	//------------------------------------------
	
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isScriptBase(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isScriptBase(), false); }
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isComment(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isComment(), false); }
	/** Returns true if the specified string is a return keyword. */
	public static boolean isReturn(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isReturn(), false); }
	/** Returns true if the specified string is a datatype keyword. */
	public static boolean isDataType(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isDataType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoop(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isLoop(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isLogic(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isLogic(), false); }
	/** Returns true if the specified string is an exception statement keyword. */
	public static boolean isException(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isException(), false); }
	/** Returns true if the specified string is a logical operator keyword. */
	public static boolean isOperator(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isOperator(), false); }
	/** Returns true if the specified string is an arithmetic function keyword. */
	public static boolean isArithmetic(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isArithmetic(), false); }
	/** Returns true if the specified string is a arithmetic operator keyword. */
	public static boolean isArithmeticOperator(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isArithmeticOperator(), false); }
	/** Returns the EDataType equivalent if this keyword is a datatype. */
	public static EDataType getDataType(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.getDataType(), EDataType.NULL); }
	
	//------------------------------------------
	
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isScriptBase(Keyword in) { return EUtil.nullApplyR(in, k -> k.isScriptBase(), false); }
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isComment(Keyword in) { return EUtil.nullApplyR(in, k -> k.isComment(), false); }
	/** Returns true if the specified string is a return keyword. */
	public static boolean isReturn(Keyword in) { return EUtil.nullApplyR(in, k -> k.isReturn(), false); }
	/** Returns true if the specified string is a datatype keyword. */
	public static boolean isDataType(Keyword in) { return EUtil.nullApplyR(in, k -> k.isDataType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoop(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLoop(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isLogic(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLogic(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isException(Keyword in) { return EUtil.nullApplyR(in, k -> k.isException(), false); }
	/** Returns true if the specified string is a logical operator keyword. */
	public static boolean isOperator(Keyword in) { return EUtil.nullApplyR(in, k -> k.isOperator(), false); }
	/** Returns true if the specified string is an arithmetic function keyword. */
	public static boolean isArithmetic(Keyword in) { return EUtil.nullApplyR(in, k -> k.isArithmetic(), false); }
	/** Returns true if the specified string is an arithmetic operator keyword. */
	public static boolean isArithmeticOperator(Keyword in) { return EUtil.nullApplyR(in, k -> k.isArithmeticOperator(), false); }
	/** Returns the EDataType equivalent if this keyword is a datatype. */
	public static EDataType getDataType(Keyword in) { return EUtil.nullApplyR(in, k -> k.getDataType(), EDataType.NULL); }
	
}
