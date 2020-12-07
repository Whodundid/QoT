package envisionEngine.terminal.terminalCommand;

import envisionEngine.terminal.window.ETerminal;
import util.storageUtil.EArrayList;

public interface IListableCommand {
	
	public void list(ETerminal termIn, EArrayList<String> args, boolean runVisually);

}
