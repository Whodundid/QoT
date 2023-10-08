package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_Version extends TerminalCommand {
	
	public CMD_Version() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public EList<String> getAliases() { return EList.of("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v"; }
	
	@Override
	public Object runCommand() {
		writeln(EColors.seafoam, "Envision Game Engine");
		writeln(EColors.yellow, "Build Date: ", EColors.skyblue, Envision.VERSION_DATE);
		writeln(EColors.yellow, "Build Num: ", EColors.mc_lightpurple, Envision.VERSION_BUILD);
		return Envision.VERSION_DATE + " " + Envision.VERSION_BUILD;
	}
	
}
