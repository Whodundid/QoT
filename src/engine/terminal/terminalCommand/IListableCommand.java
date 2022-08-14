package engine.terminal.terminalCommand;

import engine.terminal.window.ETerminal;
import eutil.datatypes.EList;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EList<String> args, boolean runVisually);

}
