package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ClearTerminal extends TerminalCommand {
	
	public CMD_ClearTerminal() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "clear"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: clr"; }
	
	@Override
	public void runCommand() {
		clearTerm();
	}
	
}
