package terminal.terminalCommand.commands.system;

import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class OpenGui extends TerminalCommand {
	
	public OpenGui() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = -1;
	}
	
	@Override public String getName() { return "opengui"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("gui", "og"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening guis."; }
	@Override public String getUsage() { return "ex: og settings"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
	}
	
}
