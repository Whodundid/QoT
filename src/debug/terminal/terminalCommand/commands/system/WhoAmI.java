package debug.terminal.terminalCommand.commands.system;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.storage.EArrayList;

//Author: Hunter Bragg

public class WhoAmI extends TerminalCommand {
	
	public WhoAmI() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }
	@Override public String getUsage() { return null; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}

	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.writeln("User");
		}
		else { termIn.error("This command does not take arguments!"); }
	}
	
}
