package envision.engine.terminal.terminalUtil;

import java.io.File;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public class ArgHelper {
	
	private final ETerminalWindow term;
	private final EList<String> arguments;
	private final boolean runVisually;
	protected final int size;
	protected final File curDir;
	
	public ArgHelper(ETerminalWindow termIn, EList<String> argsIn, boolean runVisuallyIn) {
		term = termIn;
		arguments = EList.unmodifiableList(argsIn);
		runVisually = runVisuallyIn;
		curDir = term.getDir();
		size = argsIn.size();
	}
	
	public boolean visually() { return runVisually; }
	public int length() { return size; }
	public File curDir() { return curDir; }
	public String first() { return arguments.getFirst(); }
	public String last() { return arguments.getLast(); }
	public String arg(int index) { return (index >= 0 && index < size) ? arguments.get(index) : null; }
	public EList<String> args() { return arguments; }
	
}
