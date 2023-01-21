package envisionEngine.terminal.terminalCommand;

import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.util.EList;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EList<String> args, boolean runVisually);

}
