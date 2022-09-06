package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

//Author: Hunter Bragg

public class ClearTerminalHistory extends TerminalCommand {
	
	public ClearTerminalHistory() {
		setCategory("System");
		numArgs = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		QoT.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
}
