package debug.terminal.terminalCommand.commands.system;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.storage.EArrayList;
import windowLib.windowObjects.windows.CalculatorWindow;

public class CalcCommand extends TerminalCommand {
	
	public CalcCommand() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "calculator"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("calc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: calc"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Opening Calculator", EColors.lgreen);
		termIn.getTopParent().displayWindow(new CalculatorWindow());
	}
	
}
