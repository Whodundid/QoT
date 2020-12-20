package gameSystems.scriptingSystem.interpreter.langUtil;

import gameSystems.scriptingSystem.EScript;
import gameSystems.scriptingSystem.exceptions.ScriptError;
import gameSystems.scriptingSystem.interpreter.langUtil.checkers.SyntaxChecker;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.ConditionHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.FunctionDefHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.FunctionHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.ReturnHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.ScopeCloseHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.ScopeOpenHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.ScriptDefHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.VariableDefHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.VariableHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.logic.LogicHandler;
import gameSystems.scriptingSystem.interpreter.langUtil.handlers.loops.LoopHandler;
import gameSystems.scriptingSystem.util.Tokenizer;
import gameSystems.scriptingSystem.util.enums.Keyword;
import gameSystems.scriptingSystem.util.enums.LineType;
import util.storageUtil.EArrayList;

/** An intermediary line parser which separates line and determines what kind of function is to be performed on each line. */
public class ScriptLineParser {

	protected EScript script;
	protected Tokenizer tokenizer;
	
	//-------------------------
	//ScriptLineParser Handlers
	//-------------------------
	
	public LogicHandler logicHandler;
	public LoopHandler loopHandler;
	public ConditionHandler conditionHandler;
	public FunctionDefHandler functionDefHandler;
	public FunctionHandler functionHandler;
	public ReturnHandler returnHandler;
	public ScopeCloseHandler closeHandler;
	public ScopeOpenHandler openHandler;
	public VariableDefHandler variableDefHandler;
	public VariableHandler variableHandler;
	public ScriptDefHandler scriptDefHandler;
	
	public ScriptLineParser(EScript scriptIn) {
		script = scriptIn;
		tokenizer = new Tokenizer();
		
		logicHandler = new LogicHandler(this);
		loopHandler = new LoopHandler(this);
		conditionHandler = new ConditionHandler(this);
		functionDefHandler = new FunctionDefHandler(this);
		functionHandler = new FunctionHandler(this);
		returnHandler = new ReturnHandler(this);
		closeHandler = new ScopeCloseHandler(this);
		openHandler = new ScopeOpenHandler(this);
		variableDefHandler = new VariableDefHandler(this);
		variableHandler = new VariableHandler(this);
		scriptDefHandler = new ScriptDefHandler(this);
	}
	
	//--------------------------
	//ScriptLineParser Variables
	//--------------------------
	
	protected String line;
	protected int lineNum;
	
	public LineType lastType = null;
	public boolean enforceFuncScope = false;
	public boolean enforceScriptScope = false;
	public boolean addToFunc = false;
	public int lastLine = 0;
	
	//------------------------
	//ScriptLineParser Methods
	//------------------------
	
	public void parseLine(String lineIn, int lineNumIn) throws ScriptError {
		line = lineIn;
		lineNum = lineNumIn;
		
		//break the line into individual statements
		EArrayList<String> lines = new EArrayList(separateLines(lineIn));
		
		//parse each line
		for (String s : lines) {
			EArrayList<String> tokens = tokenizer.tokenize(s, lineNum);
			determineLine(tokens);
		}
	}
	
	public void parseLine(EArrayList<String> tokensIn, int lineNumIn) throws ScriptError {
		lineNum = lineNumIn;
		
		if (tokensIn != null) {
			determineLine(new EArrayList(tokensIn));
		}
	}
	
	/** Breaks a line into sub lines separated by the ';' character. Also accounts for loops to not break those up. */
	public static EArrayList<String> separateLines(String line) {
		EArrayList<String> parsed = new EArrayList();
		
		boolean inLoop = false;
		int loopParens = 0;
		
		String cur = "";
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			
			if (c == '{') {
				parsed.add(cur.trim());
				parsed.add("" + c);
				cur = "";
				continue;
			}
			
			cur += c;
			if (!inLoop && Keyword.FOR.keyword.equals(cur)) {
				inLoop = true;
			}
			
			if (inLoop) {
				if (c == '(') { loopParens++; }
				if (c == ')') {
					loopParens--;
					if (loopParens == 0) { inLoop = false; }
				}
			}
			
			//if the current character is a ';' end the current line and add it to the list of tokenized lines
			if (!inLoop && c == ';' || c == '}') {
				parsed.add(cur.trim());
				cur = "";
			}
		}
		
		if (!cur.isEmpty()) { parsed.add(cur); }
		
		//System.out.println("PARSED: " + parsed);
		
		return parsed;
	}
	
	private void determineLine(EArrayList<String> tokens) throws ScriptError {
		//attempt to determine what kind of statement is being parsed
		LineType type = SyntaxChecker.getLineType(tokens);
		
		//String tokes = "";
		//for (String s : tokens) { tokes += s + " "; }
		//if (tokens.isNotEmpty()) { tokes = tokes.substring(0, tokes.length() - 1); }
		
		//if there are scopes to be created, enforce them
		
		//System.out.printf("%2d:      %-60s Scopes: %d            %s\n", lineNum, tokens, script.lastScopes.size(), type);
		
		if (enforceFuncScope) {
			if (tokens.isNotEmpty() && !tokens.get(0).equals("{")) {
				throw new ScriptError("Incomplete function declaration at line: " + script.lastFuncLines.peek() + ", must have '{' to open!", lineNum);
			}
			
			enforceFuncScope = false;
		}
		
		if (enforceScriptScope && tokens.isNotEmpty() && !tokens.get(0).equals("{")) {
			throw new ScriptError("Incomplete script declaration at line: " + script.lastScriptLines.peek() + ", must have '{' to open!", lineNum);
		}
		
		//otherwise, continue parsing the line
		
		if (addToFunc) {
			functionDefHandler.addToFunction(tokens);
		}
		else {
			//System.out.printf("%2d - %s     %s\n", lineNum, type, tokens);
			switch (type) {
			case ARITHMETIC: ExpressionSolver.evaluateExpression(script, tokens, lineNum); break;
			case SCRIPTDEF: scriptDefHandler.handleScriptDef(tokens); break;
			case RETURN: returnHandler.handleReturn(tokens); break;
			case FUNCTION: functionHandler.handleFunction(tokens); break;
			case FUNCTIONDEF: functionDefHandler.handleFunctionDef(tokens); break;
			case LOGIC: logicHandler.handleLogic(tokens); break;
			case LOOP: loopHandler.handleLoop(tokens); break;
			case VARIABLE: variableHandler.handleVariable(tokens); break;
			case VARIABLEDEF: variableDefHandler.handleVariableDef(tokens); break;
			case SCOPE_OPEN: openHandler.handleScopeOpen(tokens); break;
			case SCOPE_CLOSE: closeHandler.handleScopeClose(tokens); break;
			default: break;
			}
		}
		
		
	}

	//------------------------
	//ScriptLineParser Getters
	//------------------------
	
	public EScript getScript() { return script; }
	public String getLine() { return line; }
	public int getLineNumber() { return lineNum; }
	
}
