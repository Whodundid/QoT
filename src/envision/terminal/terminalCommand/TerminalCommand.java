package envision.terminal.terminalCommand;

import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
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
	
	private CommandType type = CommandType.NORMAL;
	private String category = "none";
	protected EArrayList<String> acceptedModifiers = new EArrayList();
	protected EArrayList<String> modifiers = new EArrayList();
	protected int numArgs = 0;
	protected boolean shouldRegister = true;
	
	//--------------
	// Constructors
	//--------------
	
	public TerminalCommand() {
		type = CommandType.NORMAL;
	}
	
	public TerminalCommand(CommandType typeIn) {
		type = typeIn;
	}
	
	//------------------
	// Abstract Methods
	//------------------
	
	public abstract String getName();
	public abstract void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually);
	
	public boolean showInHelp() { return true; }
	public EArrayList<String> getAliases() { return new EArrayList<>(); }
	public String getHelpInfo(boolean runVisually) { return "(No help info for '" + getName() + "'!"; }
	public String getUsage() { return "(No usage info for '" + getName() + "'!"; }
	public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {}
	
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
	public void preRun(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		//extract '-[x]' args
		var it = args.iterator();
		while (it.hasNext()) {
			var s = it.next();
			if (s.startsWith("-") && s.length() > 1) {
				if (acceptedModifiers.contains(s)) modifiers.add(s);
				it.remove();
			}
		}
		
		runCommand(termIn, args, runVisually);
	}
	
	//---------
	// Getters
	//---------
	
	public CommandType getType() { return type; }
	public EArrayList<String> getModifiers() { return modifiers; }
	public String getCategory() { return category; }
	public int getNumArgs() { return numArgs; }
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
	
	protected void errorUsage(ETerminal term, String errorReason) {
		term.errorUsage(errorReason, getUsage());
	}
	
	/**
	 * Returns true if the specified modifier was parsed at command start.
	 * 
	 * @param in
	 * @return
	 */
	protected boolean hasModifier(String in) {
		return in != null && in.length() >= 1 && modifiers.contains(in);
	}
	
	protected void basicTabComplete(ETerminal termIn, EArrayList<String> args, String... completionsIn) {
		basicTabComplete(termIn, args, new EArrayList<String>(completionsIn));
	}
	
	protected void basicTabComplete(ETerminal termIn, EArrayList<String> args, EArrayList<String> completionsIn) {
		int limit = numArgs == -1 ? args.size() : ENumUtil.clamp(args.size(), 0, numArgs);
		
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
	
	protected void error(ETerminal termIn, Throwable e) {
		StackTraceElement[] trace = e.getStackTrace();
		String errLoc = (trace != null && trace[0] != null) ? "\n" + trace[0].toString() : null;
		termIn.javaError(e.toString() + errLoc);
		e.printStackTrace();
	}
	
}
