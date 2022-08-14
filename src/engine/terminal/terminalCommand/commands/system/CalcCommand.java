package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.windowLib.bundledWindows.CalculatorWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

public class CalcCommand extends TerminalCommand {
	
	public CalcCommand() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "calculator"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("calc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: calc"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		termIn.writeln("Opening Calculator", EColors.lgreen);
		termIn.getTopParent().displayWindow(new CalculatorWindow());
	}
	
}
