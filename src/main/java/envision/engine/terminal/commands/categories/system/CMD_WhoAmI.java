package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_WhoAmI extends TerminalCommand {
	
	public CMD_WhoAmI() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }

	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			var user = Envision.getCurrentUser();
			termIn.writeln(((user.isDev()) ? EColors.purple : EColors.lgray), user);
		}
		else errorUsage(termIn, ERROR_NO_ARGS);
	}
	
}
