package envision.debug.terminal.commands.categories.system;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class CMD_ClearTerminalHistory extends TerminalCommand {
	
	public CMD_ClearTerminalHistory() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		QoT.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
}
