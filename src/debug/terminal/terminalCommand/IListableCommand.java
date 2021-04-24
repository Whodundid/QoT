package debug.terminal.terminalCommand;

import debug.terminal.window.ETerminal;
import storageUtil.EArrayList;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually);

}
