package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

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
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		Envision.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
}
