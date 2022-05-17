package engine.terminal.terminalCommand;

import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually);

}
