package debug.terminal.terminalCommand.commands.system;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.storage.EArrayList;

//Author: Hunter Bragg

public class Version extends TerminalCommand {
	
	public Version() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v core"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("0.0.1");
	}
	
}
