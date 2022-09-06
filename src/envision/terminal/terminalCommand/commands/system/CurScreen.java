package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

public class CurScreen extends TerminalCommand {
	
	public CurScreen() {
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
