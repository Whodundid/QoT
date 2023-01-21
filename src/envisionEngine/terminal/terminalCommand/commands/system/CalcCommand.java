package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import envision.windowLib.bundledWindows.CalculatorWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;

public class CalcCommand extends TerminalCommand {
	
	public CalcCommand() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "calculator"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("calc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: calc"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Opening Calculator", EColors.lgreen);
		termIn.getTopParent().displayWindow(new CalculatorWindow());
	}
	
}
