package envision.engine.terminal.commands;

import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.util.EList;

public interface IListableCommand {
	
	public void list(ETerminalWindow termIn, EList<String> args, boolean runVisually);

}
