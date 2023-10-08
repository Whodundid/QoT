package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class CMD_WhoAmI extends TerminalCommand {
	
	public CMD_WhoAmI() {
		setCategory("System");
		expectedArgLength = 0;
	}

	@Override public String getName() { return "whoami"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Provides user info on the current player."; }

	@Override
	public Object runCommand() {
	    expectNoArgs();
	    var user = Envision.getCurrentUser();
        writeln(((user.isDev()) ? EColors.purple : EColors.lgray), user);
        return user;
	}
	
}
