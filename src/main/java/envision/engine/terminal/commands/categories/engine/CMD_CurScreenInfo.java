package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;

public class CMD_CurScreenInfo extends TerminalCommand {
	
	public CMD_CurScreenInfo() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "screen"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the current screen"; }
	@Override public String getUsage() { return "ex: screen"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
		writeln(EColors.lgreen, Envision.getCurrentScreen());
		return null;
	}
	
}
