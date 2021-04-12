package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import storageUtil.EArrayList;

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
