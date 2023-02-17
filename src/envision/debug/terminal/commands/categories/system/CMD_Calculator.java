package envision.debug.terminal.commands.categories.system;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.windows.bundledWindows.CalculatorWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class CMD_Calculator extends TerminalCommand {
	
	public CMD_Calculator() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "calculator"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("calc"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the terminal"; }
	@Override public String getUsage() { return "ex: calc"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		termIn.writeln("Opening Calculator", EColors.lgreen);
		termIn.getTopParent().displayWindow(new CalculatorWindow());
	}
	
}
