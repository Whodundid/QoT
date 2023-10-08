package envision.engine.terminal.commands;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Collection;
import java.util.List;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.terminal.terminalUtil.ArgHelper;
import envision.engine.terminal.terminalUtil.TermArgLengthException;
import envision.engine.terminal.terminalUtil.TermArgParsingException;
import envision.engine.terminal.terminalUtil.TerminalCommandError;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.terminal.window.termParts.TerminalTextField;
import envision.engine.windows.windowObjects.advancedObjects.textArea.TextAreaLine;
import envision.engine.windows.windowObjects.advancedObjects.textArea.WindowTextArea;
import envision.engine.windows.windowTypes.interfaces.ITopParent;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

/**
 * The base terminal command for which all terminal commands extend from.
 * 
 * @author Hunter Bragg
 */
public abstract class TerminalCommand {
	
	public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	public static final File USER_DIR = new File(System.getProperty("user.dir"));
	public static final File ROOT_DIR = new File(System.getenv("SystemDrive"));
	
	public static final String ERROR_NO_ARGS = "This command does not take any arguments!";
	public static final String ERROR_TOO_MANY = "Too many arguments!";
	public static final String ERROR_NOT_ENOUGH = "Not enough arguments!";
	public static final String ERROR_NULL = "The given value is null!";
	
	public static final String ERROR_EXPECTED_INT = "Expected an integer value!";
	public static final String ERROR_EXPECTED_NUMBER = "Expected a numeric value!";
	
	static final String ERROR_EXPECTED_AT_LEAST = "Expected at least '%d' argument(s)!";
	static final String ERROR_EXPECTED_NO_MORE_THAN = "Expected no more than '%d' argument(s)!";
	static final String ERROR_EXPECTED_BETWEEN = "Expected between %d to %d arguments!";
	static final String ERROR_EXPECTED_EXACT_ARGUMENTS = "Expected exactly '%d' argument(s)!";
	
	protected ArgHelper argHelper;
	private String category = "none";
	private final EList<String> acceptedModifiers = EList.newList();
	private final EList<String> parsedModifiers = EList.newList();
	protected int expectedArgLength = 0;
	protected boolean shouldRegister = true;
	protected boolean allowAnyModifier = false;
	
	protected ETerminalWindow term;
	
	//--------------
	// Constructors
	//--------------
	
	protected TerminalCommand() {}
	
	//==================
	// Abstract Methods
	//==================
	
	/** Returns the string by which this command is called. */
	public abstract String getName();
	
	/** While overridable, this function is intended to only be called by the terminal command handler. */
	public Object runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		try {
			term = termIn;
			argHelper = new ArgHelper(termIn, args, runVisually);
			return runCommand();
		}
		catch (TermArgParsingException e) {
		    e.display(termIn);
		}
		catch (TermArgLengthException e) {
			errorUsage(e.getMessage());
		}
		catch (Exception e) {
			error(e);
		}
		
		return null;
	}
	
	/** The intended override target for commands. */
	protected Object runCommand() throws Exception {
	    return null;
	}
	
	//=================
	// Utility Methods
	//=================
	
	public File curDir() { return argHelper.curDir(); }
	public boolean runVisually() { return argHelper.visually(); }
	public String firstArg() { return argHelper.firstString(); }
	public String secondArg() { return argHelper.arg(1); }
	public String thirdArg() { return argHelper.arg(2); }
	public String lastArg() { return argHelper.lastString(); }
	public String arg(int index) { return argHelper.stringArg(index); }
	public EList<String> args() { return argHelper.stringArgs(); }
	public int argLength() { return argHelper.stringLength(); }
	public ETerminalWindow term() { return term; }
	public boolean noArgs() { return argHelper.stringLength() == 0; }
	public boolean oneArg() { return argHelper.stringLength() == 1; }
	public boolean twoArgs() { return argHelper.stringLength() == 2; }
	public boolean threeArgs() { return argHelper.stringLength() == 3; }
	
    /**
     * Returns true if the given input either matches this command's name
     * or one of its aliases.
     * 
     * @param input The input to test for
     * 
     * @return true if this command name or alias(es) match
     */
	public boolean matchesNameOrAlias(String input) {
	    if (input == null) return false;
	    if (EUtil.isEqual(getName(), input)) return true;
	    if (getAliases().contains(input)) return true;
	    return false;
	}
	
	public ITopParent getTopParent() { return term().getTopParent(); }
	public GameScreen displayScreen(GameScreen screenIn) { return Envision.displayScreen(screenIn); }
	public IWindowParent displayWindow(IWindowParent windowIn) { return getTopParent().displayWindow(windowIn); }
	public IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus) { return getTopParent().displayWindow(windowIn, transferFocus); }
	public IWindowParent displayWindow(IWindowParent windowIn, ObjectPosition position) { return getTopParent().displayWindow(windowIn, position); }
	public IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus, ObjectPosition position) {
	    return getTopParent().displayWindow(windowIn, transferFocus, position);
	}
	
	public boolean showInHelp() { return true; }
	public EList<String> getAliases() { return new EArrayList<>(); }
	public String getHelpInfo(boolean runVisually) { return "(No help info for '" + getName() + "'!"; }
	public String getUsage() { return "(No usage info for '" + getName() + "'!"; }
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Intended to be called by the TerminalHandler specifically to
	 * initially parse through arguments.
	 * 
	 * @param termIn
	 * @param args
	 * @param runVisually
	 */
	public void preRun(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		//clear out old modifiers
		parsedModifiers.clear();
		
		//extract '-[x]' args
		var it = args.iterator();
		while (it.hasNext()) {
			var s = it.next();
			if (s.startsWith("-") && s.length() > 1) {
			    // check if input is a negative number
			    String numberPortion = s.substring(1);
			    if (ENumUtil.isNumber(numberPortion)) {
			        continue;
			    }
			    
			    // if it's not a number, check if it's a valid modifier
			    // and remove it from arguments if so
				if (allowAnyModifier || acceptedModifiers.contains(s)) {
				    parsedModifiers.add(s);
				    it.remove();
				}
			}
		}
		
		runCommand_i(termIn, args, runVisually);
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the list of parsed modifiers when this command was executed. */
	public EList<String> getParsedModifiers() { return parsedModifiers; }
	/** Returns the list of modifiers accepted by this command (ex: '-a', 'r', 'rf', etc...) */
	public EList<String> getAcceptedModifiers() { return acceptedModifiers; }
	/** The type category of this terminal command -- used to group commands by category. */
	public String getCategory() { return category; }
	/** Returns the total number of arguments that this command is expected to read in. */
	public int getExpectedArgLength() { return expectedArgLength; }
	/** Returns true if this command should actually be allowed to register within a TerminalHandler. */
	public boolean shouldRegister() { return shouldRegister; }
	
	//---------
	// Setters
	//---------
	
	public TerminalCommand setAcceptedModifiers(String... in) { acceptedModifiers.addA(in); return this; }
	public TerminalCommand setCategory(String in) { category = in; return this; }
	public TerminalCommand setShouldRegister(boolean val) { shouldRegister = val; return this; }
	
	//-------------------
	// Protected Methods
	//-------------------
	
	public void onConfirmation(String response) {}
	
	protected void usage() { info(getUsage()); }
	
	protected void errorUsage(String errorReason) { errorUsage(term, errorReason); }
	protected void errorUsage(ETerminalWindow term, String errorReason) {
		term.errorUsage(errorReason, getUsage());
	}
	
	/**
	 * Returns true if the specified modifier was parsed at command start.
	 * 
	 * @param in The modifier to check for
	 * @return true if input contained modifier
	 */
	protected boolean hasModifier(String in) {
		return in != null && in.length() >= 1 && parsedModifiers.contains(in);
	}
	
	/** Basic 'true/false' tab complete options. */
	protected void tabCompleteTF(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, "true", "false");
	}
	
	protected void basicTabComplete(ETerminalWindow termIn, EList<String> args, String... completionsIn) {
		basicTabComplete(termIn, args, EList.of(completionsIn));
	}
	
	protected void basicTabComplete(ETerminalWindow termIn, List<String> args, Collection<String> completionsIn) {
		if (args.isEmpty()) {
			termIn.buildTabCompletions(completionsIn);
			return;
		}
		
		int limit = (expectedArgLength == -1) ? args.size() : ENumUtil.clamp(args.size(), 0, expectedArgLength);
		
		if (args.size() == limit && !termIn.getTab1()) {
		    int arg = termIn.getCurrentArg() - 1;
	        try {
	            String input = args.get(arg);
	        
	            EList<String> options = EList.newList();
	        
	            for (String s : completionsIn) {
	                if (s.startsWith(input)) options.add(s);
	            }
	        
	            termIn.buildTabCompletions(options);
	        }
	        catch (IndexOutOfBoundsException e) {}
	        catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	protected void error(Throwable e) { error(term, e); }
	protected void error(ETerminalWindow termIn, Throwable e) {
		StackTraceElement[] trace = e.getStackTrace();
		String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
		termIn.javaError(e.toString() + errLoc);
		e.printStackTrace();
	}
	
	//==================
	// Argument Helpers
	//==================
	
	/** Throws a default error if the given object was null. */
	protected void expectNotNull(Object obj) { expectNotNull(obj, ERROR_NULL); }
	/** Throws a customized error if the given object was null. */
	protected void expectNotNull(Object obj, String message) {
	    if (obj != null) return;
	    if (message != null) throw new TerminalCommandError(message);
	    throw new TerminalCommandError(ERROR_NULL);
	}
	
	/** Throws a default error if there were more than zero args passed. */
    protected void expectNoArgs() { expectNoArgs(ERROR_NO_ARGS); }
    /** Throws a customized error if there were more than zero args passed. */
    protected void expectNoArgs(String message) {
        if (argLength() == 0) return;
        if (message != null) throw new TermArgLengthException(message);
        throw new TermArgLengthException(ERROR_NO_ARGS);
    }
	
    /** Throws a default error if there were less than the expected number of arguments passed. */
	protected void expectAtLeast(int amount) { expectAtLeast(amount, null); }
	/** Throws a customized error if there were less than the expected number of arguments passed. */
	protected void expectAtLeast(int amount, String message) {
		checkAtLeast(amount, message);
	}
	
	/** Throws a default error if there were more than the expected number of arguments passed. */
	protected void expectNoMoreThan(int amount) { expectNoMoreThan(amount, null); }
	/** Throws a customized error if there were more than the expected number of arguments passed. */
	protected void expectNoMoreThan(int amount, String message) {
		checkNoMoreThan(amount, message);
	}
	
    /** Throws a default error if the number of expected arguments is not between the given range. */
    protected void expectBetween(int low, int high) { expectBetween(low, high, null); }
    /** Throws a customized error if the number of expected arguments is not between the given range. */
    protected void expectBetween(int low, int high, String message) {
        checkBetween(low, high, message);
    }
	
	/** Throws a default error if the number of arguments passed is not the exact expected amount. */
	protected void expectExactly(int amount) { expectExactly(amount, null); }
	/** Throws a customized error if the number of arguments passed is not the exact expected amount. */
	protected void expectExactly(int amount, String message) {
		checkExact(amount, message);
	}
	
	private void checkAtLeast(int amount, String message) {
		if (argLength() >= amount) return;
		if (message != null) throw new TermArgLengthException(message);
		throw new TermArgLengthException(String.format(ERROR_EXPECTED_AT_LEAST, amount));
	}
	
	private void checkNoMoreThan(int amount, String message) {
		if (argLength() <= amount) return;
		if (message != null) throw new TermArgLengthException(message);
		throw new TermArgLengthException(String.format(ERROR_EXPECTED_NO_MORE_THAN, amount));
	}
    
    private void checkBetween(int low, int high, String message) {
        if (argLength() >= low && argLength() <= high) return;
        if (message != null) throw new TermArgLengthException(message);
        throw new TermArgLengthException(String.format(ERROR_EXPECTED_BETWEEN, low, high));
    }
	
	private void checkExact(int amount, String message) {
		if (argLength() == amount) return;
		if (message != null) throw new TermArgLengthException(message);
		throw new TermArgLengthException(String.format(ERROR_EXPECTED_EXACT_ARGUMENTS, amount));
	}
	
	//==========================
	// Argument Parsing Helpers
	//==========================
    
	protected int parseInt(String input) { return parseInt(input, ERROR_EXPECTED_INT); }
    protected int parseInt(String input, String onErrorString) {
        try { return Integer.parseInt(input); }
        catch (Exception e) { throw new TermArgParsingException(e, onErrorString); }
    }
    protected int parseInt(String input, int defaultValue, String onErrorString) {
        try { return Integer.parseInt(input); }
        catch (Exception e) { error(e, onErrorString); }
        return defaultValue;
    }
    
    protected double parseDouble(String input) { return parseDouble(input, ERROR_EXPECTED_NUMBER); }
    protected double parseDouble(String input, String onErrorString) {
        try { return Double.parseDouble(input); }
        catch (Exception e) { throw new TermArgParsingException(e, onErrorString); }
    }
    protected double parseDouble(String input, double defaultValue, String onErrorString) {
        try { return Double.parseDouble(input); }
        catch (Exception e) { error(e, onErrorString); }
        return defaultValue;
    }
    
	//==================
	// Terminal Helpers
	//==================
	
	protected void clearTerm() { term.clear(); }
	protected void clearTabData() { term.clearTabData(); }
	protected void clearTabCompletions() { term.clearTabCompletions(); }
	protected void resetTab() { term.resetTab(); }
	
	protected int getLastUsed() { return term.getLastUsed(); }
	protected int getHistoryLine() { return term.getHistoryLine(); }
	protected WindowTextArea<String> getTextArea() { return term.getTextArea(); }
	protected TerminalTextField getInputField() { return term.getInputField(); }
	protected EList<TextAreaLine<String>> getInfoLines() { return term.getInfoLines(); }
	protected int getTabPos() { return term.getTabPos(); }
	protected boolean getTab1() { return term.getTab1(); }
	protected String getTextTabBegin() { return term.getTextTabBegin(); }
	protected int getTabArgStart() { return term.getTabArgStart(); }
	protected String getTabBase() { return term.getTabBase(); }
	protected File dir() { return term.getDir(); }
	protected File parentDir() { return (dir().getParentFile() == null) ? dir() : dir().getParentFile(); }

	protected File dirFull() { return getCanonicalFile(dir()); }
	protected File parentDirFull() { return getCanonicalFile(parentDirFull()); }
	
	protected static File getCanonicalFile(File f) {
	    try { return f.getCanonicalFile(); }
	    catch (Exception e) { return f; }
	}
	
	protected ETerminalWindow setDir(File dirIn) { return term.setDir(dirIn); }
	protected ETerminalWindow setInputEnabled(boolean val) { return term.setInputEnabled(val); }
	protected ETerminalWindow setLastUsed(int in) { return term.setLastUsed(in); }
	protected ETerminalWindow setHistoryLine(int in) { return term.setHistoryLine(in); }
	protected ETerminalWindow setTabPos(int in) { return term.setTabPos(in); }
	protected ETerminalWindow setTab1(boolean val) { return term.setTab1(val); }
	protected ETerminalWindow setTextTabBeing(String in) { return term.setTextTabBeing(in); }
	protected ETerminalWindow setTabBase(String in) { return term.setTabBase(in); }
	
	protected void requireConfirmation(String message) { term.setRequiresCommandConfirmation(this, message, args(), runVisually()); }
	protected void clearConfirmation() { term.clearConfirmationRequirement(); }
	
	protected ETerminalWindow writeln() { return term.writeln(); }
	protected ETerminalWindow writeln(Object objIn) { return term.writeln(objIn); }
	protected ETerminalWindow writeln(Object objIn, EColors colorIn) { return term.writeln(objIn, colorIn); }
	protected ETerminalWindow writeln(Object objIn, int colorIn) { return term.writeln(objIn, colorIn); }
	protected ETerminalWindow writeln(EColors color, Object... arguments) { return term.writeln(color, arguments); }
	protected ETerminalWindow writeln(Integer color, Object... arguments) { return term.writeln(color, arguments); }
	protected ETerminalWindow writeln(Object... arguments) { return term.writeln(arguments); }
	protected ETerminalWindow errorUsage(String error, String usage) { return term.errorUsage(error, usage); }
	protected ETerminalWindow info(Object... msgIn) { return term.info(msgIn); }
	protected ETerminalWindow warn(Object... msgIn) { return term.warn(msgIn); }
	protected ETerminalWindow error(Object... msgIn) { return term.error(msgIn); }
	protected ETerminalWindow javaError(Object... msgIn) { return term.javaError(msgIn); }
	
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, EColors colorIn) { return term.writeLink(msgIn, linkTextIn, colorIn); }
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, int colorIn) { return term.writeLink(msgIn, linkTextIn, colorIn); }
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, boolean isWebLink, EColors colorIn) { return term.writeLink(msgIn, linkTextIn, isWebLink, colorIn); }
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, boolean isWebLink, int colorIn) { return term.writeLink(msgIn, linkTextIn, isWebLink, colorIn); }
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, EColors colorIn) { return term.writeLink(msgIn, linkTextIn, linkObjectIn, isWebLink, colorIn); }
	protected ETerminalWindow writeLink(String msgIn, String linkTextIn, Object linkObjectIn, boolean isWebLink, int colorIn) { return term.writeLink(msgIn, linkTextIn, linkObjectIn, isWebLink, colorIn); }
	
}
