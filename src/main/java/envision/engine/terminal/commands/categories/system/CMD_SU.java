package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_SU extends TerminalCommand {
	
	public CMD_SU() {
		setCategory("System");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "su"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to switch between user profiles"; }
	@Override public String getUsage() { return "ex: su dev"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, Envision.getProfileRegistry().getRegisteredProfileNames());
	}
	
	@Override
	public void runCommand() {
		expectExactly(1, "Error! No username specified!");
		
		final var username = firstArg();
		final var registry = Envision.getProfileRegistry();
		final var profile = registry.getProfile(username);
		
		var result = registry.setCurrentUser(profile);
		EColors color = Boolean.TRUE.equals(result.getA()) ? EColors.green : EColors.lred;
		
		term.writeln(color, result.getB());
	}
	
}
