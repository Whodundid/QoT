package envision.engine.terminal.commands.categories.windows;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

public class CMD_TermID extends TerminalCommand {
	
	public CMD_TermID() {
		setCategory("Windows");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "id"; }
	@Override public String getHelpInfo(boolean runVisually) { return "returns the window id for this terminal"; }
	@Override public String getUsage() { return "ex: id"; }
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		termIn.writeln(termIn.getObjectName() + EColors.lgray + " Window ID: " + termIn.getObjectID(), EColors.yellow);
	}
	
}
