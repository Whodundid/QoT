package gameSystems.scriptingSystem.interpreter.langUtil.checkers;

import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.ScriptLineParser;
import gameSystems.scriptingSystem.util.Tokenizer;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.util.enums.LineType;
import java.util.Stack;
import util.miscUtil.EDataType;
import util.storageUtil.EArrayList;

public class SyntaxChecker {
	
	private static boolean inFunction = false;
	private static int funcDepth = 0;
	private static Stack<EDataType> funcReturnTypes = new Stack(); 
	private static int numLine = -1;
	private static int syntaxErrors = 0;
	private static Tokenizer tokenizer = new Tokenizer();
	
	/** Returns true if the parsed script does not contain any syntax errors. */
	public static boolean checkSyntax(EArrayList<String> lines) {
		numLine = 1;
		boolean errorFound = false;
		
		try {
			for (String s : lines) {
				
				//examine each line for syntax errors - if an error is found, let it get thrown so the line number can be shown
				for (String line : ScriptLineParser.separateLines(s)) {
					//if an error was found, increment the number of syntax errors total
					if (!checkLineSyntax(line)) {
						if (!errorFound) { errorFound = true; }
						syntaxErrors++;
					}
				}
				
				numLine++;
			}
		}
		catch (ScriptError e) {
			e.printStackTrace();
			//print the error --- somehow...
		}
		
		return !errorFound;
	}
	

	
	/** Returns true if the current line does not have any syntax errors. */
	private static boolean checkLineSyntax(String line) throws ScriptError {
		
		//System.out.println("LINE: " + line);
		
		//check for null lines
		if (line == null) { throw new ScriptError("Parsed line on line: (" + numLine + ") is somehow null! This shouldn't happen!"); }
		
		//ignore empty lines
		if (line.isEmpty()) { return true; }
		
		//if the line is only one character long, ensure that it is the end line character
		if (line.length() == 1 && (line.charAt(0) != ';' && line.charAt(0) != '}')) { throw new ScriptError("Unrecognized character '" + line.charAt(0) + "' at line: " + numLine + "!"); }
		
		//parse the line to read it into the script
		parseLine(line);
		 
		return true;
	}
	
	/** Attempts to find code symantic errors. Does not check for improper variable/function calls. */
	private static void parseLine(String line) throws ScriptError {
		EArrayList<String> tokens = tokenizer.tokenize(line, numLine);
		int len = tokens.size();
		
		//attempt to determine what kind of statement is being parsed
		LineType type = getLineType(tokens);
		Keyword first = (tokens.isNotEmpty()) ? Keyword.getKeyword(tokens.get(0)) : null;
		
		switch (type) {
		case RETURN:
			if (inFunction) {
				boolean hasDataType = false;
				funcDepth--;
			
			}
			break;
		
		case FUNCTION: break;
		case FUNCTIONDEF:
			if (first != null) {
				if (tokens.size() > 1) {
					Keyword second = Keyword.getKeyword(tokens.get(1));
					if (!Keyword.isNextKeywordAllowed(first, second)) { throw new ScriptError("Invalid function declaration keyword: " + second + " at line: " + numLine); }
				}
				
				//increment the function depth
				funcDepth++;
				funcReturnTypes.push(first.getDataType());
			}
			
			break;
		case LOGIC: break;
		case LOOP: break;
		case SCRIPTDEF: break;
		case VARIABLE: break;
		case VARIABLEDEF: break;
		default: break;
		}
	}
	
	public static LineType getLineType(EArrayList<String> tokensIn) throws ScriptError {
		//only care if there are tokens to work with
		if (tokensIn.isNotEmpty()) {
			String t = tokensIn.get(0);
			Keyword k = Keyword.getKeyword(t);
			
			//parse through possible line types
			if (k != null) {
				
				if (k.equals(Keyword.SCRIPT)) { return LineType.SCRIPTDEF; }
				else if (k == Keyword.VOID) { return LineType.FUNCTIONDEF; }
				else if (k.isComment()) { return LineType.COMMENT; }
				else if (k.isReturn()) { return LineType.RETURN; }
				else if (k.isArithmetic()) { throw new ScriptError("Invalid arithmetic operator at line start! Line: " + numLine); }
				else if (k.isArithmeticOperator()) { return LineType.ARITHMETIC; }
				else if (k.isLogic()) { return LineType.LOGIC; }
				else if (k.isLoop()) { return LineType.LOOP; }
				else if (k.isOperator()) { throw new ScriptError("Invalid logical operator at line start! Line: " + numLine); }
				
				else if (k.isDataType()) {
					//first check for improper variable declarations
					if (1 < tokensIn.size()) {
						String t2 = tokensIn.get(1);
						
						//if (Keyword.isKeyword(t2)) { throw new ScriptError("Invalid variable declaration at line: '" + numLine + "' -- a keyword should not follow a datatype!"); }
						
						//check if it could be a list
						if (t2.equals("[")) { return LineType.VARIABLEDEF; }
						
						//next check if the input is a function declaration
						else if (2 < tokensIn.size()) {
							String t3 = tokensIn.get(2);
							if (t3.equals("(")) { return LineType.FUNCTIONDEF; }
							else { return LineType.VARIABLEDEF; }
						}
						else { return LineType.VARIABLEDEF; }
					}
					else { throw new ScriptError("Incomplete variable declaration at line: '" + numLine + "' --  Variable name expected!"); }
				}
			}
			else {
				if (t.equals("{")) { return LineType.SCOPE_OPEN; }
				else if (t.equals("}")) { return LineType.SCOPE_CLOSE; }
				else {
					if (1 < tokensIn.size()) {
						String t2 = tokensIn.get(1);
						
						if (t2.equals("(")) { return LineType.FUNCTION; }
						else { return LineType.VARIABLE; }
					}
					return LineType.VARIABLE;
				}
			}
		}
		return LineType.EMPTY;
	}

}
