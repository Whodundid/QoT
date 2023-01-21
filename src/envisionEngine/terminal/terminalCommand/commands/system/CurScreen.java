package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

public class CurScreen extends TerminalCommand {
	
	public CurScreen() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "screen"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the current screen"; }
	@Override public String getUsage() { return "ex: screen"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		termIn.writeln(QoT.getCurrentScreen(), EColors.lgreen);
	}
	
}
