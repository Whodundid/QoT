package envision.debug.terminal.commands;

import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

/**
 * The base terminal command for which all terminal commands extend from.
 * 
 * @author Hunter Bragg
 */
public abstract class TerminalCommand {
	
	public static final String ERROR_NO_ARGS = "This command does not take any arguments!";
	public static final String ERROR_TOO_MANY = "Too many arguments!";
	public static final String ERROR_NOT_ENOUGH = "Not enough arguments!";
	
	private String category = "none";
	private final EList<String> acceptedModifiers = EList.newList();
	private final EList<String> parsedModifiers = EList.newList();
	protected int expectedArgLength = 0;
	protected boolean shouldRegister = true;
	protected boolean allowAnyModifier = false;
	
	//--------------
	// Constructors
	//--------------
	
	public TerminalCommand() {}
	
	//------------------
	// Abstract Methods
	//------------------
	
	public abstract String getName();
	public abstract void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually);
	
	public boolean showInHelp() { return true; }
	public EList<String> getAliases() { return new EArrayList<>(); }
	public String getHelpInfo(boolean runVisually) { return "(No help info for '" + getName() + "'!"; }
	public String getUsage() { return "(No usage info for '" + getName() + "'!"; }
	public void handleTabComplete(ETerminalWindow conIn, EList<String> args) {}
	
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
				if (allowAnyModifier || acceptedModifiers.contains(s)) parsedModifiers.add(s);
				it.remove();
			}
		}
		
		runCommand(termIn, args, runVisually);
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
	
	protected void errorUsage(ETerminalWindow term, String errorReason) {
		term.errorUsage(errorReason, getUsage());
	}
	
	/**
	 * Returns true if the specified modifier was parsed at command start.
	 * 
	 * @param in
	 * @return
	 */
	protected boolean hasModifier(String in) {
		return in != null && in.length() >= 1 && parsedModifiers.contains(in);
	}
	
	/** Basic 'true/false' tab complete options. */
	protected void tabCompleteTF(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, "true", "false");
	}
	
	protected void basicTabComplete(ETerminalWindow termIn, EList<String> args, String... completionsIn) {
		basicTabComplete(termIn, args, new EArrayList<String>(completionsIn));
	}
	
	protected void basicTabComplete(ETerminalWindow termIn, EList<String> args, EList<String> completionsIn) {
		int limit = (expectedArgLength == -1) ? args.size() : ENumUtil.clamp(args.size(), 0, expectedArgLength);
		
		if (args.isEmpty()) {
			termIn.buildTabCompletions(completionsIn);
		}
		else if (args.size() == limit) {
			if (!termIn.getTab1()) {
				int arg = termIn.getCurrentArg() - 1;
				try {
					String input = args.get(arg);
				
					EArrayList<String> options = new EArrayList();
				
					for (String s : completionsIn) {
						if (s.startsWith(input)) { options.add(s); }
					}
				
					termIn.buildTabCompletions(options);
				}
				catch (IndexOutOfBoundsException e) {}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		
	}
	
	protected void error(ETerminalWindow termIn, Throwable e) {
		StackTraceElement[] trace = e.getStackTrace();
		String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
		termIn.javaError(e.toString() + errLoc);
		e.printStackTrace();
	}
	
}
