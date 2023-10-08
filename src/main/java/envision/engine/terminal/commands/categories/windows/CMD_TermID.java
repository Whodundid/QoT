package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;

public class CMD_TermID extends TerminalCommand {
	
	public CMD_TermID() {
		setCategory("Windows");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "id"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the window id for this terminal"; }
	@Override public String getUsage() { return "ex: id"; }
	
	@Override
	public Object runCommand() {
		writeln(EColors.yellow, term().getObjectName(), EColors.lgray, " Window ID: ", term().getObjectID());
		return term().getObjectID();
	}
	
}
