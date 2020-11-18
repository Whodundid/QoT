package terminal.terminalCommand.commands.system;

import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.renderUtil.EColors;
import util.storageUtil.EArrayList;

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
		//Envision.displayWindow(new CalculatorWindow());
	}
	
}
