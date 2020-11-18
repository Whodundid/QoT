package terminal.terminalCommand.commands.system;

import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

public class ID_CMD extends TerminalCommand {
	
	public ID_CMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "id"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>(); }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the window id for this terminal"; }
	@Override public String getUsage() { return "ex: id"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln(termIn.getObjectName() + EColors.lgray + " Window ID: " + termIn.getObjectID(), EColors.yellow);
	}
	
}
