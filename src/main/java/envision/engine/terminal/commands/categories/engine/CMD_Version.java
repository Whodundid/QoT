package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_Version extends TerminalCommand {
	
	public CMD_Version() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v"; }
	
	@Override
	public void runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		termIn.writeln(EColors.seafoam, "Envision Game Engine");
		termIn.writeln(EColors.yellow, "Build Date: ", EColors.skyblue, Envision.VERSION_DATE);
		termIn.writeln(EColors.yellow, "Build Num: ", EColors.mc_lightpurple, Envision.VERSION_BUILD);
	}
	
}
