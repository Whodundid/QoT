package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.CommandType;
import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

public class ClearTerminal extends TerminalCommand {
	
	public ClearTerminal() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "clear"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("clr", "cls"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: clr"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.clear();
	}
	
}
