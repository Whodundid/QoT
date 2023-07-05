package envision.engine.terminal.commands.categories.system;

import envision.engine.terminal.commands.TerminalCommand;
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
	public void runCommand() {
		writeln("Opening Calculator", EColors.lgreen);
		displayWindow(new CalculatorWindow());
	}
	
}
