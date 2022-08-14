package engine.terminal.terminalCommand;

import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.math.NumberUtil;

/**
 * The base terminal command for which all terminal commands extend from.
 * 
 * @author Hunter Bragg
 */
public abstract class TerminalCommand {
	
	public static final String ERROR_NO_ARGS = "This command takes no arguments!";
	public static final String ERROR_TOO_MANY = "Too many arguments!";
	public static final String ERROR_NOT_ENOUGH = "Not enough arguments!";
	
	private CommandType type = CommandType.NORMAL;
	private String category = "none";
	protected EList<String> acceptedModifiers = new EArrayList<>();
	protected EList<String> modifiers = new EArrayList<>();
	protected int numArgs = 0;
	protected boolean shouldRegister = true;
	
	//--------------
	// Constructors
	//--------------
	
	public TerminalCommand(CommandType typeIn) {
		type = typeIn;
	}
	
	//------------------
	// Abstract Methods
	//------------------
	
	public abstract String getName();
	public boolean showInHelp() { return true; }
	public EList<String> getAliases() { return new EArrayList<>(); }
	public abstract String getHelpInfo(boolean runVisually);
	public String getUsage() { return "[no default usage]"; }
	public void handleTabComplete(ETerminal conIn, EList<String> args) {}
	public abstract void runCommand(ETerminal termIn, EList<String> args, boolean runVisually);
	
	//---------
	// Methods
	//---------
	
	public void preRun(ETerminal termIn, EList<String> args, boolean runVisually) {
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
	public EList<String> getModifiers() { return modifiers; }
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
	
	/**
	 * Returns true if the specified modifier was parsed at command start.
	 * 
	 * @param in
	 * @return
	 */
	protected boolean hasModifier(String in) {
		return in != null && in.length() >= 1 && modifiers.contains(in);
	}
	
	protected void basicTabComplete(ETerminal termIn, EList<String> args, EList<String> completionsIn) {
		int limit = numArgs == -1 ? args.size() : NumberUtil.clamp(args.size(), 0, numArgs);
		
		if (args.isEmpty()) {
			termIn.buildTabCompletions(completionsIn);
		}
		else if (args.size() == limit) {
			if (!termIn.getTab1()) {
				int arg = termIn.getCurrentArg() - 1;
				try {
					String input = args.get(arg);
				
					EList<String> options = new EArrayList();
				
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
