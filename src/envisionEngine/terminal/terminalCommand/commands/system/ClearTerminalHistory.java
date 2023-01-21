package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class ClearTerminalHistory extends TerminalCommand {
	
	public ClearTerminalHistory() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		QoT.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
}
