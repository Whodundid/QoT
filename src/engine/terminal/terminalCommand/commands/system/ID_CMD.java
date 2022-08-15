package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

public class ID_CMD extends TerminalCommand {
	
	public ID_CMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "id"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the window id for this terminal"; }
	@Override public String getUsage() { return "ex: id"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln(termIn.getObjectName() + EColors.lgray + " Window ID: " + termIn.getObjectID(), EColors.yellow);
	}
	
}
