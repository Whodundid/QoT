package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.eWindow.windowObjects.windows.CalculatorWindow;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import main.Game;
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
		Game.displayWindow(new CalculatorWindow());
	}
	
}
