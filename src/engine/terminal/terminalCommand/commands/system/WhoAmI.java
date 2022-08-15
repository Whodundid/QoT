package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

public class WhoAmI extends TerminalCommand {
	
	public WhoAmI() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }

	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) termIn.writeln("User");
		else errorUsage(termIn, ERROR_NO_ARGS);
	}
	
}
