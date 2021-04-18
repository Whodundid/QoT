package terminal.terminalCommand;

import storageUtil.EArrayList;
import terminal.window.ETerminal;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually);

}
