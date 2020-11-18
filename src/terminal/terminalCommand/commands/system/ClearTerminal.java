package terminal.terminalCommand.commands.system;

import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearTerminal extends TerminalCommand {
	
	public ClearTerminal() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "clear"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: clr"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.clear();
	}
	
}