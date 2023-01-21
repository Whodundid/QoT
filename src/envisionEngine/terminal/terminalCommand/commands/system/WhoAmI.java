package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class WhoAmI extends TerminalCommand {
	
	public WhoAmI() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }

	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.writeln("User");
		else errorUsage(termIn, ERROR_NO_ARGS);
	}
	
}
