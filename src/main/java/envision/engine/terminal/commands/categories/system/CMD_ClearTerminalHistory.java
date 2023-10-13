package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ClearTerminalHistory extends TerminalCommand {
	
	public CMD_ClearTerminalHistory() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public EList<String> getAliases() { return EList.of("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	
	@Override
	public void runCommand() {
	    expectNoArgs();
		Envision.getTerminalHandler().clearHistory();
		writeln(EColors.green, "Terminal history cleared..");
	}
	
}
