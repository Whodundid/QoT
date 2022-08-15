package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;

public class CurScreen extends TerminalCommand {
	
	public CurScreen() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "screen"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the current screen"; }
	@Override public String getUsage() { return "ex: screen"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln(QoT.getCurrentScreen(), EColors.lgreen);
	}
	
}
